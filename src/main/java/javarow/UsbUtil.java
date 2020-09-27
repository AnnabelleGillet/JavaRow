package javarow;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.usb4java.BufferUtils;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

class UsbUtil {
	private static byte IN_ENDPOINT = (byte) 0x81;
	private static byte OUT_ENDPOINT = (byte) 0x02;
	public static byte STANDARD_START_FLAG = (byte) 0xF1;
	public static byte EXTENDED_START_FLAG = (byte) 0xF0;
	public static byte END_FLAG = (byte) 0xF2;
	public static byte STUFFING_FLAG = (byte) 0xF3;
	private static Map<Byte, Byte> MAP_UNSTUFFING_FLAG = new HashMap<>();
	static {
		MAP_UNSTUFFING_FLAG.put((byte) 0x00, (byte) 0xF0);
		MAP_UNSTUFFING_FLAG.put((byte) 0x01, (byte) 0xF1);
		MAP_UNSTUFFING_FLAG.put((byte) 0x02, (byte) 0xF2);
		MAP_UNSTUFFING_FLAG.put((byte) 0x03, (byte) 0xF3);
	}
	public static Map<Byte, Byte> MAP_STUFFING_FLAG = new HashMap<>();
	static {
		MAP_STUFFING_FLAG.put((byte) 0xF0, (byte) 0x00);
		MAP_STUFFING_FLAG.put((byte) 0xF1, (byte) 0x01);
		MAP_STUFFING_FLAG.put((byte) 0xF2, (byte) 0x02);
		MAP_STUFFING_FLAG.put((byte) 0xF3, (byte) 0x03);
	}
	public static byte CSAFE_SETUSERCFG1_CMD = (byte) 0x1A;
	
	/** The USB communication timeout. */
    private static final int TIMEOUT = 2000;
    private static final int INTER_FRAME_GAP = 50;
    private static long timeLastFrameSend = 0;
	
	private static Context context = new Context();
	private static Map<DeviceHandle, Integer> attachedDeviceHandles = new HashMap<DeviceHandle, Integer>();

	/**
	 * Initialize the {@link LibUsb} context. To call before performing USB operations.
	 */
	public static void init() {
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);
	}

	/**
	 * Find a device with the vendor id.
	 * 
	 * @param vendorId the vendor id used to find the device
	 * @return the first device corresponding to the vendor id
	 */
	public static Device findDevice(short vendorId) {
		// Read the USB device list
		DeviceList list = new DeviceList();
		int result = LibUsb.getDeviceList(context, list);
		if (result < 0) throw new LibUsbException("Unable to get device list", result);

		try {
			// Iterate over all devices and scan for the right one
			for (Device device: list) {
				DeviceDescriptor descriptor = new DeviceDescriptor();
				result = LibUsb.getDeviceDescriptor(device, descriptor);
				if (result != LibUsb.SUCCESS) {
					throw new LibUsbException("Unable to read device descriptor", result);
				}
				if (descriptor.idVendor() == vendorId) {
					return device;
				}
			}
		} finally {
			// Ensure the allocated device list is freed
			LibUsb.freeDeviceList(list, true);
		}

		// Device not found
		return null;
	}

	/**
	 * Get the device handle from the device.
	 * 
	 * @param device the device for which to get the device handle
	 * @return the device handle of the device
	 */
	public static DeviceHandle getDeviceHandle(Device device) {
		DeviceHandle handle = new DeviceHandle();
		int result = LibUsb.open(device, handle);
		if (result != LibUsb.SUCCESS) {
			throw new LibUsbException("Unable to open USB device", result);
		}

		return handle;
	}
	
	/**
	 * Close the device.
	 * 
	 * @param handle the device to close
	 */
	public static void closeDeviceHandle(DeviceHandle handle) {
		LibUsb.close(handle);
	}

	/**
	 * Claim the interface of the device to be able to use it.
	 * 
	 * @param handle the device for which to claim the interface
	 */
	public static synchronized void claimInterface(DeviceHandle handle) {
		if (!attachedDeviceHandles.containsKey(handle)) {
			// Check if kernel driver is attached to the interface
			int attached = LibUsb.kernelDriverActive(handle, 1);
			if (attached < 0) {
				throw new LibUsbException("Unable to check kernel driver active", attached);
			}
			attachedDeviceHandles.put(handle, attached);

			System.out.println("Kernel driver active : " + attached);
			
			int result = attached;
			if (attached >= 0) {
				// Detach kernel driver from interface 0 and 1. This can fail if
				// kernel is not attached to the device or operating system
				// doesn't support this operation. These cases are ignored here.
				result = LibUsb.detachKernelDriver(handle, attached);
				if (result != LibUsb.SUCCESS &&
						result != LibUsb.ERROR_NOT_SUPPORTED &&
						result != LibUsb.ERROR_NOT_FOUND) {
					throw new LibUsbException("Unable to detach kernel driver", result);
				}
			}

			// Claim interface
			result = LibUsb.claimInterface(handle, 0);
			IntBuffer transferred = BufferUtils.allocateIntBuffer();
			LibUsb.getConfiguration(handle, transferred);
			System.out.println("Configuration: " + transferred.get(0));
			LibUsb.setConfiguration(handle, 1);
			if (result != LibUsb.SUCCESS) {
				throw new LibUsbException("Unable to claim interface", result);
			}
		}
	}

	/**
	 * Release the interface claimed by the device.
	 * 
	 * @param handle the handle for which to release the interface
	 */
	public static synchronized void releaseInterface(DeviceHandle handle) {
		// Release the interface
		int result = LibUsb.releaseInterface(handle, 0);
		if (result != LibUsb.SUCCESS) {
			throw new LibUsbException("Unable to release interface", result);
		}

		// Re-attach kernel driver if needed
		if (attachedDeviceHandles.containsKey(handle))	{
			LibUsb.attachKernelDriver(handle, attachedDeviceHandles.get(handle));
			attachedDeviceHandles.remove(handle);
			if (result != LibUsb.SUCCESS) {
				throw new LibUsbException("Unable to re-attach kernel driver", result);
			}
		}
	}
	
	/**
	 * Close the {@link LibUsb} context. To call before exiting the application.
	 */
	public static void close() {
		LibUsb.exit(context);
	}
	
	/**
	 * Send a command to the device and wait for the response.
	 * 
	 * @param handle the device to which send the command
	 * @param command the command to send
	 * @return the response message of the device
	 */
	public static ByteBuffer sendCommand(DeviceHandle handle, Command command) {
		byte[] message = command.getMessage();
		ByteBuffer buffer = BufferUtils.allocateByteBuffer(message.length);
		buffer.put(message);
		buffer.rewind();
        IntBuffer transferred = BufferUtils.allocateIntBuffer();
        if (System.currentTimeMillis() - timeLastFrameSend < INTER_FRAME_GAP) {
        	try {
				Thread.sleep(INTER_FRAME_GAP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        timeLastFrameSend = System.currentTimeMillis();
        int result = LibUsb.interruptTransfer(handle, OUT_ENDPOINT, buffer, transferred, TIMEOUT);

        if (result < 0) {
        	throw new LibUsbException("Interrupt transfer failed", result);
        }
        
        return readCommand(handle, message.length);
	}

	/**
	 * Get monitor responses contain in a message.
	 * 
	 * @param message the message sent by the monitor
	 * @return the individual responses
	 * @throws Exception 
	 */
	public static UsbResponse getResponse(ByteBuffer message) throws Exception {
		UsbResponse response = new UsbResponse();
		
		int messageStart = UsbUtil.findMessageStart(message);
		int messageEnd = UsbUtil.findMessageEnd(message, messageStart);
		
		byte[] content = new byte[messageEnd - messageStart];
		for (int i = 0; i < content.length; i++) {
			content[i] = message.get(messageStart + i);
		}
		int checkSumValid = 0;
		while (checkSumValid < 1) {
			try {
				content = checkSumAndUnstuff(content);
				checkSumValid = 1;
			} catch (Exception e) {
				checkSumValid--;
				if (checkSumValid < -5) {
					throw e;
				}
			}
		}

		// Get status
		response.csafeResponses.put(Response.CODE.CSAFE_GETSTATUS_CMD, Response.CSAFE_GETSTATUS_CMD(new byte[] {content[0]}));
		
		// Get other responses
		int index = 1;
		int endSpecificCommands = 0;
		while (index < content.length) {
			byte code = content[index++];
			int length = 0;
			if (code == CSAFE_SETUSERCFG1_CMD) {
				int nbBytesSpecificCommands = content[index++];
				endSpecificCommands = index + nbBytesSpecificCommands;
				if (nbBytesSpecificCommands > 0) {
					code = content[index++];
					length = content[index++];
				}
			} else {
				if (index < content.length - 1) {
					length = content[index++];
				} else {
					length = 0;
				}
			}
			if (index <= endSpecificCommands) {
				response.specificPMResponses.put(code, Response.getResponseSpecificPM(code, Arrays.copyOfRange(content, index, index + length)));
			} else {
				response.csafeResponses.put(code, Response.getResponse(code, Arrays.copyOfRange(content, index, index + length)));
			}
			index += length;
		}
		
		return response;
	}
	
	/**
	 * Get the response of the monitor in a ByteBuffer if their is one available.
	 * 
	 * @param handle the device from which to read the response
	 * @param size the expected size of the response
	 * @return the response of the device.
	 */
	private static ByteBuffer readCommand(DeviceHandle handle, int size) {
        ByteBuffer buffer = BufferUtils.allocateByteBuffer(size).order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer transferred = BufferUtils.allocateIntBuffer();
        buffer.rewind();
        int result = LibUsb.interruptTransfer(handle, IN_ENDPOINT, buffer, transferred, TIMEOUT);
        buffer.rewind();
        if (result != LibUsb.SUCCESS) {
        	throw new LibUsbException("Unable to read data", result);
        }
        
        return buffer;
    }
	
	/**
	 * Find the index of the start flag byte (standard or extended) in a message from the monitor.
	 * 
	 * @param message the message in which to find the start index
	 * @return the start index
	 */
	private static int findMessageStart(ByteBuffer message) {
		int messageStart = 0;
		while (message.get(messageStart) != STANDARD_START_FLAG && message.get(messageStart) != EXTENDED_START_FLAG) {
			messageStart++;
		}
		if (message.get(messageStart) == EXTENDED_START_FLAG) {
			// Skip destination and source
			messageStart += 2;
		}
		// Start of the message
		messageStart++;
		return messageStart;
	}
	
	/**
	 * Find the index of the end flag byte in a message from the monitor.
	 * 
	 * @param message the message in which to find the end index
	 * @return the end index
	 */
	private static int findMessageEnd(ByteBuffer message, int messageStart) {
		int messageEnd = messageStart;
		while (message.get(messageEnd) != END_FLAG 
				|| (message.get(messageEnd) == END_FLAG && message.get(messageEnd - 1) == STUFFING_FLAG)) {
			messageEnd++;
		}
		return messageEnd;
	}
	
	/**
	 * Perform a checksum (XOR between all bytes) and compare it with the checksum contained
	 * in the message. Also unstuff bytes when needed.
	 * 
	 * @param content the message on which to perform the checksum and the unstuffing
	 * @return the message without the checksum byte and with unstuffing performed
	 * @throws Exception 
	 */
	private static byte[] checkSumAndUnstuff(byte[] content) throws Exception {
		List<Byte> validBytes = new ArrayList<>();
		byte checkSumInMessage = content[content.length - 1];
		int end = content.length - 1;
		if (content[content.length - 2] == STUFFING_FLAG) {
			checkSumInMessage = MAP_UNSTUFFING_FLAG.get(content[content.length - 1]);
			end = content.length - 2;
		}
		byte checkSumCalculated = (byte) 0x00;
		
		for (int i = 0; i < end; i++) {
			if (content[i] == STUFFING_FLAG) {
				validBytes.add(MAP_UNSTUFFING_FLAG.get(content[++i]));
			} else {
				validBytes.add(content[i]);
			}
			checkSumCalculated ^= validBytes.get(validBytes.size() - 1);
		}
		
		if (checkSumCalculated != checkSumInMessage) {
			throw new Exception("Difference in checksum, received: " + Byte.toUnsignedInt(checkSumInMessage) + 
					", calculated: " + Byte.toUnsignedInt(checkSumCalculated));
		}
		
		byte[] result = new byte[validBytes.size()];
		int i = 0;
		for (byte element: validBytes) {
			result[i++] = element;
		}
		return result;
	}
}
