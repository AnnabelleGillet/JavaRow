package javarow;

public class LongCommand {
	private static LongCommand longCommand = new LongCommand();
	
	public enum DistanceUnits {
		Kilometers((byte) 0x21),
		Hectometers((byte) 0x22),
		Decameters((byte) 0x23),
		Meters((byte) 0x24);
		
		public final byte code;
		
		private DistanceUnits(byte code) {
			this.code = code;
		}
	}
	
	// Long commands
	public abstract class CSAFE_LONG_COMMAND {
		public byte code;
		public int responseLength;
		
		public CSAFE_LONG_COMMAND(byte code, int responseLength) {
			this.code = code;
			this.responseLength = responseLength;
		}
		
		public abstract byte[] getParameters();
	}
	
	public class CSAFE_AUTOUPLOAD_CMD extends CSAFE_LONG_COMMAND {
		public int configuration;
		
		public CSAFE_AUTOUPLOAD_CMD(int configuration) {
			super((byte) 0x01, 0);
			this.configuration = configuration;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) configuration};
		}
	}
	public static CSAFE_AUTOUPLOAD_CMD CSAFE_AUTOUPLOAD_CMD(int configuration) {
		return longCommand.new CSAFE_AUTOUPLOAD_CMD(configuration);
	}
	
	public class CSAFE_IDDIGITS_CMD extends CSAFE_LONG_COMMAND {
		public int nbDigits;
		
		public CSAFE_IDDIGITS_CMD(int nbDigits) {
			super((byte) 0x10, 0);
			this.nbDigits = nbDigits;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) nbDigits};
		}
	}
	public static CSAFE_IDDIGITS_CMD CSAFE_IDDIGITS_CMD(int nbDigits) {
		return longCommand.new CSAFE_IDDIGITS_CMD(nbDigits);
	}
	
	public class CSAFE_SETTIME_CMD extends CSAFE_LONG_COMMAND {
		public int hour;
		public int minute;
		public int second;
		
		public CSAFE_SETTIME_CMD(int hour, int minute, int second) {
			super((byte) 0x11, 0);
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) hour, (byte) minute, (byte) second};
		}
	}
	public static CSAFE_SETTIME_CMD CSAFE_SETTIME_CMD(int hour, int minute, int second) {
		return longCommand.new CSAFE_SETTIME_CMD(hour, minute, second);
	}
	
	public class CSAFE_SETDATE_CMD extends CSAFE_LONG_COMMAND {
		public int year;
		public int month;
		public int day;
		
		public CSAFE_SETDATE_CMD(int year, int month, int day) {
			super((byte) 0x12, 0);
			this.year = year - 1900;
			this.month = month;
			this.day = day;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) year, (byte) month, (byte) day};
		}
	}
	public static CSAFE_SETDATE_CMD CSAFE_SETDATE_CMD(int year, int month, int day) {
		return longCommand.new CSAFE_SETDATE_CMD(year, month, day);
	}
	
	public class CSAFE_SETTIMEOUT_CMD extends CSAFE_LONG_COMMAND {
		public int timeout;
		
		public CSAFE_SETTIMEOUT_CMD(int timeout) {
			super((byte) 0x13, 0);
			this.timeout = timeout;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) timeout};
		}
	}
	public static CSAFE_SETTIMEOUT_CMD CSAFE_SETTIMEOUT_CMD(int timeout) {
		return longCommand.new CSAFE_SETTIMEOUT_CMD(timeout);
	}
	
	public class CSAFE_SETTWORK_CMD extends CSAFE_LONG_COMMAND {
		public int hour;
		public int minute;
		public int second;
		
		public CSAFE_SETTWORK_CMD(int hour, int minute, int second) {
			super((byte) 0x20, 0);
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) hour, (byte) minute, (byte) second};
		}
	}
	public static CSAFE_SETTWORK_CMD CSAFE_SETTWORK_CMD(int hour, int minute, int second) {
		return longCommand.new CSAFE_SETTWORK_CMD(hour, minute, second);
	}
	
	public class CSAFE_SETHORIZONTAL_CMD extends CSAFE_LONG_COMMAND {
		public int distance;
		public DistanceUnits units;
		
		public CSAFE_SETHORIZONTAL_CMD(int distance, DistanceUnits units) {
			super((byte) 0x21, 0);
			this.distance = distance;
			this.units = units;
		}
		
		public byte[] getParameters() {
			return new byte[] {int2byte(distance, 0), int2byte(distance, 1), units.code};
		}
	}
	public static CSAFE_SETHORIZONTAL_CMD CSAFE_SETHORIZONTAL_CMD(int distance, DistanceUnits units) {
		return longCommand.new CSAFE_SETHORIZONTAL_CMD(distance, units);
	}
	
	public class CSAFE_SETCALORIES_CMD extends CSAFE_LONG_COMMAND {
		public int totalCalories;
		
		public CSAFE_SETCALORIES_CMD(int totalCalories) {
			super((byte) 0x23, 0);
			this.totalCalories = totalCalories;
		}
		
		public byte[] getParameters() {
			return new byte[] {int2byte(totalCalories, 0), int2byte(totalCalories, 1)};
		}
	}
	public static CSAFE_SETCALORIES_CMD CSAFE_SETCALORIES_CMD(int totalCalories) {
		return longCommand.new CSAFE_SETCALORIES_CMD(totalCalories);
	}
	
	public class CSAFE_SETPROGRAM_CMD extends CSAFE_LONG_COMMAND {
		public int workoutId;
		
		public CSAFE_SETPROGRAM_CMD(int workoutId) {
			super((byte) 0x24, 0);
			this.workoutId = workoutId;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) workoutId, (byte) 0x00};
		}
	}
	public static CSAFE_SETPROGRAM_CMD CSAFE_SETPROGRAM_CMD(int workoutId) {
		return longCommand.new CSAFE_SETPROGRAM_CMD(workoutId);
	}
	
	public class CSAFE_SETPOWER_CMD extends CSAFE_LONG_COMMAND {
		public int strokeWatts;
		
		public CSAFE_SETPOWER_CMD(int strokeWatts) {
			super((byte) 0x34, 0);
			this.strokeWatts = strokeWatts;
		}
		
		public byte[] getParameters() {
			return new byte[] {int2byte(strokeWatts, 0), int2byte(strokeWatts, 1), (byte) 0x58};
		}
	}
	public static CSAFE_SETPOWER_CMD CSAFE_SETPOWER_CMD(int strokeWatts) {
		return longCommand.new CSAFE_SETPOWER_CMD(strokeWatts);
	}
	
	public class CSAFE_GETCAPS_CMD extends CSAFE_LONG_COMMAND {
		public int capabilityCode;
		
		public CSAFE_GETCAPS_CMD(int capabilityCode) {
			super((byte) 0x70, 11);
			this.capabilityCode = capabilityCode;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) capabilityCode};
		}
	}
	public static CSAFE_GETCAPS_CMD CSAFE_GETCAPS_CMD(int capabilityCode) {
		return longCommand.new CSAFE_GETCAPS_CMD(capabilityCode);
	}
	
	// Specific PM long commands
	public abstract class PM_LONG_COMMAND {
		public byte code;
		public int responseLength;
		
		public PM_LONG_COMMAND(byte code, int responseLength) {
			this.code = code;
			this.responseLength = responseLength;
		}
		
		public abstract byte[] getParameters();
	}
	
	public enum SplitDuration {
		TIME(0), DISTANCE(128);
		
		public final int value;

	    SplitDuration(int value) {
	        this.value = value;
	    }
	}
	
	public class CSAFE_PM_SET_SPLITDURATION extends PM_LONG_COMMAND {
		public SplitDuration timeOrDistance;
		public int duration;
		
		public CSAFE_PM_SET_SPLITDURATION(SplitDuration timeOrDistance, int duration) {
			super((byte) 0x05, 0);
			this.timeOrDistance = timeOrDistance;
			this.duration = duration;
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) timeOrDistance.value,
					int2byte(duration, 0),
					int2byte(duration, 1),
					int2byte(duration, 2), 
					int2byte(duration, 3)};
		}
	}
	public static CSAFE_PM_SET_SPLITDURATION CSAFE_PM_SET_SPLITDURATION(SplitDuration timeOrDistance, int duration) {
		return longCommand.new CSAFE_PM_SET_SPLITDURATION(timeOrDistance, duration);
	}
	
	public class CSAFE_PM_GET_FORCEPLOTDATA extends PM_LONG_COMMAND {
		public int blockLength;
		
		public CSAFE_PM_GET_FORCEPLOTDATA(int blockLength) {
			super((byte) 0x6B, 33);
			this.blockLength = blockLength;
			if (blockLength > 16) {
				this.blockLength = 16;
			} else if (blockLength < 0) {
				this.blockLength = 0;
			}
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) blockLength};
		}
	}
	public static CSAFE_PM_GET_FORCEPLOTDATA CSAFE_PM_GET_FORCEPLOTDATA(int blockLength) {
		return longCommand.new CSAFE_PM_GET_FORCEPLOTDATA(blockLength);
	}
	
	public class CSAFE_PM_SET_SCREENERRORMODE extends PM_LONG_COMMAND {
		public int enable;
		
		public CSAFE_PM_SET_SCREENERRORMODE(boolean enable) {
			super((byte) 0x27, 0);
			if (enable) {
				this.enable = 1;
			} else {
				this.enable = 0;
			}
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) enable};
		}
	}
	public static CSAFE_PM_SET_SCREENERRORMODE CSAFE_PM_SET_SCREENERRORMODE(boolean enable) {
		return longCommand.new CSAFE_PM_SET_SCREENERRORMODE(enable);
	}
	
	public class CSAFE_PM_GET_HEARTBEATDATA extends PM_LONG_COMMAND {
		public int blockLength;
		
		public CSAFE_PM_GET_HEARTBEATDATA(int blockLength) {
			super((byte) 0x6C, 33);
			this.blockLength = blockLength;
			if (blockLength > 16) {
				this.blockLength = 16;
			} else if (blockLength < 0) {
				this.blockLength = 0;
			}
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) blockLength};
		}
	}
	public static CSAFE_PM_GET_HEARTBEATDATA CSAFE_PM_GET_HEARTBEATDATA(int blockLength) {
		return longCommand.new CSAFE_PM_GET_HEARTBEATDATA(blockLength);
	}
	
	public class CSAFE_PM_GET_STROKESTATS extends PM_LONG_COMMAND {
		
		public CSAFE_PM_GET_STROKESTATS() {
			super((byte) 0x6E, 33);
		}
		
		public byte[] getParameters() {
			return new byte[] {(byte) 0x00};
		}
	}
	public static CSAFE_PM_GET_STROKESTATS CSAFE_PM_GET_STROKESTATS() {
		return longCommand.new CSAFE_PM_GET_STROKESTATS();
	}
	
	private byte int2byte(int value, int index) {
		return (byte) ((value >> (8 * index)) & 0xff);
	}
}
