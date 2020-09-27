package javarow;

import java.util.ArrayList;
import java.util.List;

public class Command {
	private final static int MAX_LENGTH_MESSAGE = 96;
	
	private static final int SHORT_INDEX = 0;
	private static final int LONG_INDEX = 1;
	private static final int SHORT_PM_INDEX = 2;
	private static final int LONG_PM_INDEX = 3;
	
	private List<ShortCommand> shortCommands;
	private List<LongCommand.CSAFE_LONG_COMMAND> longCommands;
	private List<ShortSpecificPMCommand> shortSpecificPMCommands;
	private List<LongCommand.PM_LONG_COMMAND> longSpecificPMCommands;
	
	private List<Integer> order;

	private int currentLengthResponse;
	private int currentLengthMessage;
	
	/**
	 * Object used to prepare a set of commands to send to the device.
	 */
	public Command() {
		this.shortCommands = new ArrayList<>();
		this.longCommands = new ArrayList<>();
		this.shortSpecificPMCommands = new ArrayList<>();
		this.longSpecificPMCommands = new ArrayList<>();
		this.order = new ArrayList<>();
	}
	
	/**
	 * Add a short command to the commands to send. Check if the length of the message can be handle by the device.
	 * 
	 * @param command the short command to add.
	 * @return true if the command has been added, false otherwise.
	 */
	public boolean addCommand(ShortCommand command) {
		if (currentLengthResponse + (command.responseLength * 2 + 1) < MAX_LENGTH_MESSAGE
				&& currentLengthMessage + 1 < MAX_LENGTH_MESSAGE) {
			currentLengthResponse += (command.responseLength * 2 + 1);
			currentLengthMessage += 1;
			shortCommands.add(command);
			order.add(SHORT_INDEX);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Add a short command specific to the PM to the commands to send. 
	 * Check if the length of the message can be handle by the device.
	 * 
	 * @param command the short command specific to the PM to add.
	 * @return true if the command has been added, false otherwise.
	 */
	public boolean addCommand(ShortSpecificPMCommand command) {
		int wrapper = 0;
		if (shortSpecificPMCommands.isEmpty() && longSpecificPMCommands.isEmpty()) {
			// Account wrapper
			wrapper = 2;
		}
		if (currentLengthResponse + (command.responseLength * 2 + 1 + wrapper) < MAX_LENGTH_MESSAGE
				&& currentLengthMessage + 1 < MAX_LENGTH_MESSAGE) {
			currentLengthResponse += (command.responseLength * 2 + 1 + wrapper);
			currentLengthMessage += 1 + wrapper;
			shortSpecificPMCommands.add(command);
			order.add(SHORT_PM_INDEX);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Add a long command to the commands to send. 
	 * Check if the length of the message can be handle by the device.
	 * 
	 * @param command the long command to add.
	 * @return true if the command has been added, false otherwise.
	 */
	public boolean addCommand(LongCommand.CSAFE_LONG_COMMAND command) {
		if (currentLengthResponse + (command.responseLength * 2 + 1) < MAX_LENGTH_MESSAGE
				&& currentLengthMessage + command.getParameters().length * 2 + 1 < MAX_LENGTH_MESSAGE) {
			currentLengthResponse += (command.responseLength * 2 + 1);
			currentLengthMessage += command.getParameters().length * 2 + 1;
			longCommands.add(command);
			order.add(LONG_INDEX);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Add a long command specific to the PM to the commands to send. 
	 * Check if the length of the message can be handle by the device.
	 * 
	 * @param command the long command specific to the PM to add.
	 * @return true if the command has been added, false otherwise.
	 */
	public boolean addCommand(LongCommand.PM_LONG_COMMAND command) {
		int wrapper = 0;
		if (shortSpecificPMCommands.isEmpty() && longSpecificPMCommands.isEmpty()) {
			// Account wrapper
			wrapper = 2;
		}
		if (currentLengthResponse + (command.responseLength * 2 + 1 + wrapper) < MAX_LENGTH_MESSAGE
				&& currentLengthMessage + command.getParameters().length * 2 + 1 + wrapper < MAX_LENGTH_MESSAGE) {
			currentLengthResponse += (command.responseLength * 2 + 1) + wrapper;
			currentLengthMessage += command.getParameters().length * 2 + 1 + wrapper;
			longSpecificPMCommands.add(command);
			order.add(LONG_PM_INDEX);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the message to send from the commands set.
	 * 
	 * @return the message.
	 */
	public byte[] getMessage() {
		List<Byte> message = new ArrayList<>();
		int responseSize = 3;
		
		int nbSpecificPM = 0;
		int indexSpecificPM = 0;
		
		int indexShort = 0;
		int indexLong = 0;
		int indexPMShort = 0;
		int indexPMLong = 0;
		
		for (Integer index: order) {
			switch (index) {
				case SHORT_INDEX: 
					nbSpecificPM = 0;
					indexSpecificPM = 0;
					ShortCommand commandShort = shortCommands.get(indexShort++);
					message.add(commandShort.code);
					responseSize += commandShort.responseLength * 2 + 1; // Consider max stuffing
					break;
				case LONG_INDEX: 
					nbSpecificPM = 0;
					indexSpecificPM = 0;
					LongCommand.CSAFE_LONG_COMMAND commandLong = longCommands.get(indexLong++);
					message.add(commandLong.code);
					responseSize += commandLong.responseLength * 2 + 1; // Consider max stuffing
					message.add((byte) commandLong.getParameters().length);
					for (byte parameter: commandLong.getParameters()) {
						message.add(parameter);
					}
					break;
				case SHORT_PM_INDEX: 
					ShortSpecificPMCommand commandPMShort = shortSpecificPMCommands.get(indexPMShort++);
					if (nbSpecificPM == 0) {
						// Create wrapper
						message.add(UsbUtil.CSAFE_SETUSERCFG1_CMD);
						message.add((byte) 0x01);
						nbSpecificPM++;
						indexSpecificPM = message.size() - 1;
					} else {
						message.set(indexSpecificPM, (byte) (message.get(indexSpecificPM) + 1));
						nbSpecificPM++;
					}
					message.add(commandPMShort.code);
					responseSize += commandPMShort.responseLength * 2 + 1; // Consider max stuffing
					break;
				case LONG_PM_INDEX: 
					LongCommand.PM_LONG_COMMAND commandPMLong = longSpecificPMCommands.get(indexPMLong++);
					if (nbSpecificPM == 0) {
						// Create wrapper
						message.add(UsbUtil.CSAFE_SETUSERCFG1_CMD);
						message.add((byte) (commandPMLong.getParameters().length + 2));
						nbSpecificPM++;
						indexSpecificPM = message.size() - 1;
					} else {
						message.set(indexSpecificPM, 
								(byte) (message.get(indexSpecificPM) + commandPMLong.getParameters().length + 2));
						nbSpecificPM++;
					}
					message.add(commandPMLong.code);
					responseSize += commandPMLong.responseLength * 2 + 1; // Consider max stuffing
					message.add((byte) commandPMLong.getParameters().length);
					for (byte parameter: commandPMLong.getParameters()) {
						message.add(parameter);
					}
					break;
			}
		}
		
		message.add(computeCheckSum(message));
		message = stuffing(message);
		
		message.add(0, UsbUtil.STANDARD_START_FLAG);
		message.add(UsbUtil.END_FLAG);
		
		int sizeOfMessage = 21;
		if (responseSize <= 21 && message.size() <= 21) {
			message.add(0, (byte) 0x01);
		} else if (responseSize <= 63 && message.size() <= 63) {
			message.add(0, (byte) 0x04);
			sizeOfMessage = 63;
		} else if (responseSize <= MAX_LENGTH_MESSAGE) {
			message.add(0, (byte) 0x02);
			sizeOfMessage = 121;
		} else {
			// TODO: throw exception
		}
		
		byte[] result = new byte[sizeOfMessage];
		int i = 0;
		for (byte element: message) {
			result[i++] = element;
		}
		return result;
	}
	
	/**
	 * Perform a checksum (XOR) on a list of bytes.
	 * 
	 * @param content the bytes on which to perform the checksum.
	 * @return the checksum for this list of bytes.
	 */
	private byte computeCheckSum(List<Byte> content) {
		byte checkSum = (byte) 0x00;
		
		for (int i = 0; i < content.size(); i++) {
			checkSum ^= content.get(i);
		}
		
		return checkSum;
	}
	
	/**
	 * Stuff the bytes of a list that need to be stuff.
	 * 
	 * @param content the list of bytes to stuff.
	 * @return the list with the bytes stuffed.
	 */
	private List<Byte> stuffing(List<Byte> content) {
		
		List<Byte> result = new ArrayList<>();
		for (byte element: content) {
			Byte stuffingByte = UsbUtil.MAP_STUFFING_FLAG.get(element);
			if (stuffingByte != null) {
				result.add(UsbUtil.STUFFING_FLAG);
				result.add(stuffingByte);
			} else {
				result.add(element);
			}
		}
		return result;
	}
}
