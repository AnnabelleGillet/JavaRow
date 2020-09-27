package javarow;

import java.nio.ByteBuffer;
import java.time.Duration;

import org.usb4java.Device;
import org.usb4java.DeviceHandle;

import javarow.LongCommand.SplitDuration;
import javarow.entity.ForcePlot;
import javarow.entity.HeartBeatPlot;
import javarow.entity.Monitor;
import javarow.entity.Status;
import javarow.entity.Stroke;
import javarow.entity.Workout;

public class Rower {
	private Device device = null;
	private DeviceHandle deviceHandle = null;
	private static Rower instance = new Rower();
	
	private int manifacturerId;
	private int cid;
	private int model;
	private int hardwareVersion;
	private int softwareVersion;
	private String serialId;
	private int maxRxFrame;
	private int maxTxFrame;
	private int minInterframe;
	
	private Status status;
	
	/** The vendor ID. */
    private static final short VENDOR_ID = 0x17a4;
    
    private Rower() {
    	UsbUtil.init();
    	// Find the rower
    	device = UsbUtil.findDevice(VENDOR_ID);
    	deviceHandle = UsbUtil.getDeviceHandle(device);
    	UsbUtil.claimInterface(deviceHandle);
    	getRowerInformations();
    }
    
    public static Rower getInstance() {
    	return instance;
    }
    
    public int getManifacturerId() {
		return manifacturerId;
	}

    public int getCid() {
		return cid;
	}
    
	public int getModel() {
		return model;
	}

	public int getHardwareVersion() {
		return hardwareVersion;
	}

	public int getSoftwareVersion() {
		return softwareVersion;
	}

	public String getSerialId() {
		return serialId;
	}

	public int getMaxRxFrame() {
		return maxRxFrame;
	}

	public int getMaxTxFrame() {
		return maxTxFrame;
	}

	public int getMinInterframe() {
		return minInterframe;
	}
	
	public Status getStatus() {
		return status;
	}

	/**
     * Send a command to the rower, and return its response.
     * 
     * @param command The command to send to the rower
     * @return The list of responses of the rower according to the command sent.
     */
    public UsbResponse sendCommand(Command command) {
    	ByteBuffer message = UsbUtil.sendCommand(deviceHandle, command);
    	UsbResponse response;
		try {
			response = UsbUtil.getResponse(message);
		} catch (Exception e) {
			response = new UsbResponse();
			e.printStackTrace();
		}
    	Response.CSAFE_GETSTATUS_CMD status = (Response.CSAFE_GETSTATUS_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETSTATUS_CMD);
    	if (status != null) {
    		this.status = Status.getFromCode(status.status);
    	}
    	return response;
    }
    
    
    /**
     * Update the informations of the monitor not related to workout.
     */
    public void getRowerInformations() {
    	Command command = new Command();
    	command.addCommand(ShortCommand.CSAFE_GETVERSION_CMD);
    	command.addCommand(ShortCommand.CSAFE_GETSERIAL_CMD);
    	command.addCommand(LongCommand.CSAFE_GETCAPS_CMD(0));
    	
    	UsbResponse response = sendCommand(command);
    	Response.CSAFE_GETVERSION_CMD version = (Response.CSAFE_GETVERSION_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETVERSION_CMD);
    	if (version != null) {
	    	this.manifacturerId = version.manifacturerId;
	    	this.cid = version.cid;
	    	this.model = version.model;
	    	this.hardwareVersion = version.hwVersion;
	    	this.softwareVersion = version.swVersion;
    	}
    	Response.CSAFE_GETSERIAL_CMD serial = (Response.CSAFE_GETSERIAL_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETSERIAL_CMD);
    	if (serial != null) {
    		this.serialId = serial.serial;
    	}
    	Response.CSAFE_GETCAPS_CMD caps = (Response.CSAFE_GETCAPS_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETCAPS_CMD);
    	if (caps != null) {
	    	this.maxRxFrame = caps.maxRxFrame;
	    	this.maxTxFrame = caps.maxTxFrame;
	    	this.minInterframe = caps.minInterframe;
    	}
    }
    
    /**
     * Get information displayed by the monitor.
     * 
     * @return the information of the monitor.
     */
    public Monitor getMonitor() {
    	Command command = new Command();
    	command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKTIME);
    	command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKDISTANCE);
    	command.addCommand(ShortCommand.CSAFE_GETCADENCE_CMD);
    	command.addCommand(ShortCommand.CSAFE_GETPOWER_CMD);
    	command.addCommand(ShortCommand.CSAFE_GETPACE_CMD);
    	command.addCommand(ShortCommand.CSAFE_GETCALORIES_CMD);
    	command.addCommand(ShortCommand.CSAFE_GETHRCUR_CMD);
    	
    	UsbResponse response = sendCommand(command);
    	Response.CSAFE_PM_GET_WORKTIME workTime = (Response.CSAFE_PM_GET_WORKTIME) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKTIME);
    	Response.CSAFE_PM_GET_WORKDISTANCE workDistance = (Response.CSAFE_PM_GET_WORKDISTANCE) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKDISTANCE);
    	Response.CSAFE_GETCADENCE_CMD cadence = (Response.CSAFE_GETCADENCE_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETCADENCE_CMD);
    	Response.CSAFE_GETPOWER_CMD power = (Response.CSAFE_GETPOWER_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETPOWER_CMD);
    	Response.CSAFE_GETPACE_CMD pace = (Response.CSAFE_GETPACE_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETPACE_CMD);
    	Response.CSAFE_GETCALORIES_CMD calories = (Response.CSAFE_GETCALORIES_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETCALORIES_CMD);
    	Response.CSAFE_GETHRCUR_CMD heartRate = (Response.CSAFE_GETHRCUR_CMD) response.csafeResponses.get(Response.CODE.CSAFE_GETHRCUR_CMD);
    	
    	Monitor monitor = new Monitor(workTime, workDistance, cadence, power, pace, calories, heartRate);
    	return monitor;
    }
    
    /**
     * Get information about the strokes.
     * 
     * @return the information of the strokes.
     */
    public Stroke getStroke() {
    	Command command = new Command();
    	command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_STROKESTATE);
    	command.addCommand(LongCommand.CSAFE_PM_GET_STROKESTATS());
    	
    	UsbResponse response = sendCommand(command);
    	Response.CSAFE_PM_GET_STROKESTATE strokeState = (Response.CSAFE_PM_GET_STROKESTATE) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_STROKESTATE);
    	Response.CSAFE_PM_GET_STROKESTATS strokeStats = (Response.CSAFE_PM_GET_STROKESTATS) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_STROKESTATS);
    	
    	Stroke stroke = new Stroke(strokeState, strokeStats);
    	return stroke;
    }
    
    /**
     * Get information about the workout.
     * 
     * @return the information of the workout.
     */
    public Workout getWorkout() {
    	Command command = new Command();
    	command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKOUTSTATE);
    	command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKOUTTYPE);
    	command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_INTERVALTYPE);
    	command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKOUTINTERVALCOUNT);
    	
    	UsbResponse response = sendCommand(command);
    	Response.CSAFE_PM_GET_WORKOUTSTATE workoutState = (Response.CSAFE_PM_GET_WORKOUTSTATE) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTSTATE);
    	Response.CSAFE_PM_GET_WORKOUTTYPE workoutType = (Response.CSAFE_PM_GET_WORKOUTTYPE) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTTYPE);
    	Response.CSAFE_PM_GET_INTERVALTYPE intervalType = (Response.CSAFE_PM_GET_INTERVALTYPE) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_INTERVALTYPE);
    	Response.CSAFE_PM_GET_WORKOUTINTERVALCOUNT workoutIntervalCount = (Response.CSAFE_PM_GET_WORKOUTINTERVALCOUNT) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTINTERVALCOUNT);
    	
    	Workout workout = new Workout(workoutState,	workoutType, intervalType,	workoutIntervalCount);    	
    	return workout;
    }
    
    /**
     * The force plot for the 16 last samples.
     * 
     * @return the force plot.
     */
    public ForcePlot getForcePlot() {
    	Command command = new Command();
    	command.addCommand(LongCommand.CSAFE_PM_GET_FORCEPLOTDATA(16));
    	
    	UsbResponse response = sendCommand(command);
    	Response.CSAFE_PM_GET_FORCEPLOTDATA forcePlotData = (Response.CSAFE_PM_GET_FORCEPLOTDATA) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_FORCEPLOTDATA);
    	
    	ForcePlot forcePlot = new ForcePlot(forcePlotData);
    	return forcePlot;
    }
    
    /**
     * The heart beats plot for the 16 last samples.
     * 
     * @return the heart beats plot.
     */
    public HeartBeatPlot getHeartBeatPlot() {
    	Command command = new Command();
    	command.addCommand(LongCommand.CSAFE_PM_GET_HEARTBEATDATA(16));
    	
    	UsbResponse response = sendCommand(command);
    	Response.CSAFE_PM_GET_HEARTBEATDATA heartBeatPlotData = (Response.CSAFE_PM_GET_HEARTBEATDATA) response.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_HEARTBEATDATA);
    	
    	HeartBeatPlot heartBeatPlot = new HeartBeatPlot(heartBeatPlotData);
    	return heartBeatPlot;
    }
    
    /**
     * Go back to the menu screen on the monitor.
     */
    public void goToMenuScreen() {
    	Command command = new Command();
    	command.addCommand(ShortCommand.CSAFE_RESET_CMD);
    	command.addCommand(ShortCommand.CSAFE_GOFINISHED_CMD);
    	command.addCommand(ShortCommand.CSAFE_GOIDLE_CMD);
    	command.addCommand(ShortCommand.CSAFE_GOHAVEID_CMD);
    	sendCommand(command);
    }
    
    /**
     * Set a programmed workout on the monitor.
     * 
     * @param programNumber the number of the program to set.
     */
    public void setWorkoutProgram(int programNumber) {
    	assert(programNumber > 0);
    	assert(programNumber <= 15);
    	
    	Command command = new Command();
    	command.addCommand(LongCommand.CSAFE_SETPROGRAM_CMD(0));
    	command.addCommand(ShortCommand.CSAFE_GOINUSE_CMD);
    	sendCommand(command);
    }
    
    /**
     * Set a workout with a given distance.
     * 
     * @param distanceMeter the distance in meters.
     */
    public void setWorkoutDistance(int distanceMeter) {
    	setWorkoutDistance(distanceMeter, distanceMeter);
    }
    
    /**
     * Set a workout with a given distance and a given split.
     * 
     * @param distanceMeter the distance in meters.
     * @param split the split in meters.
     */
    public void setWorkoutDistance(int distanceMeter, int split) {
    	if (distanceMeter < 100) throw new IllegalArgumentException("The workout distance must be superior to 100m.");
    	if (distanceMeter > 50000) throw new IllegalArgumentException("The workout distance must be inferior to 50 000m.");
    	if (split < 100) throw new IllegalArgumentException("The split distance must be superior to 100m.");
    	if (split > distanceMeter) throw new IllegalArgumentException("The split distance must be inferior to the workout distance.");
    	
    	Command command = new Command();
    	command.addCommand(ShortCommand.CSAFE_GOFINISHED_CMD);
    	command.addCommand(ShortCommand.CSAFE_RESET_CMD);
    	sendCommand(command);
    	
    	command = new Command();
    	command.addCommand(LongCommand.CSAFE_SETHORIZONTAL_CMD(distanceMeter, LongCommand.DistanceUnits.Meters));
    	command.addCommand(LongCommand.CSAFE_PM_SET_SPLITDURATION(SplitDuration.DISTANCE, split));
    	command.addCommand(LongCommand.CSAFE_SETPROGRAM_CMD(0));
    	command.addCommand(ShortCommand.CSAFE_GOINUSE_CMD);
    	
    	sendCommand(command);
    }

    /**
     * Set a workout with a given time.
     * 
     * @param time the duration of the workout.
     */
    public void setWorkoutTime(Duration time) {
    	setWorkoutTime(time, time);
    }
    
    /**
     * Set a workout with a given time and a given split.
     * 
     * @param time the duration of the workout.
     * @param split the duration of the split.
     */
    public void setWorkoutTime(Duration time, Duration split) {
    	if (time.getSeconds() < 20) throw new IllegalArgumentException("The workout time must be superior to 20s.");
    	if (time.getSeconds() >= 36000) throw new IllegalArgumentException("The workout time must be inferior to 10h.");
    	if (split.getSeconds() < 20) throw new IllegalArgumentException("The split time must be superior to 20s.");
    	if (split.getSeconds() > time.getSeconds()) throw new IllegalArgumentException("The split time must be inferior to the workout time.");
    	
    	int hours = (int) time.toHours();
    	int minutes = (int) time.minusHours(hours).toMinutes();
    	int seconds = (int) time.minusHours(hours).minusMinutes(minutes).getSeconds();
    	
    	Command command = new Command();
    	command.addCommand(ShortCommand.CSAFE_GOFINISHED_CMD);
    	command.addCommand(ShortCommand.CSAFE_RESET_CMD);
    	sendCommand(command);
    	
    	command = new Command();
    	command.addCommand(LongCommand.CSAFE_SETTWORK_CMD(hours, minutes, seconds));
    	command.addCommand(LongCommand.CSAFE_PM_SET_SPLITDURATION(SplitDuration.TIME, (int) split.getSeconds() * 100));
    	command.addCommand(LongCommand.CSAFE_SETPROGRAM_CMD(0));
    	command.addCommand(ShortCommand.CSAFE_GOINUSE_CMD);
    	
    	sendCommand(command);
    }
    
    public void close() {
    	UsbUtil.releaseInterface(deviceHandle);
    	UsbUtil.closeDeviceHandle(deviceHandle);
    	UsbUtil.close();
    }
}
