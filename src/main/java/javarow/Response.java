package javarow;

public class Response {
	private static Response response = new Response();
	
	public static class CODE {
		// Short commands
		public static final byte CSAFE_GETSTATUS_CMD = (byte) 0x80;
		public static final byte CSAFE_RESET_CMD = (byte) 0x81;
		public static final byte CSAFE_GOIDLE_CMD = (byte) 0x82;
		public static final byte CSAFE_GOHAVEID_CMD = (byte) 0x83;
		public static final byte CSAFE_GOINUSE_CMD = (byte) 0x85;
		public static final byte CSAFE_GOFINISHED_CMD = (byte) 0x86;
		public static final byte CSAFE_GOREADY_CMD = (byte) 0x87;
		public static final byte CSAFE_BADID_CMD = (byte) 0x88;
		public static final byte CSAFE_GETVERSION_CMD = (byte) 0x91;
		public static final byte CSAFE_GETID_CMD = (byte) 0x92;
		public static final byte CSAFE_GETUNITS_CMD = (byte) 0x93;
		public static final byte CSAFE_GETSERIAL_CMD = (byte) 0x94;
		public static final byte CSAFE_GETODOMETER_CMD = (byte) 0x9B;
		public static final byte CSAFE_GETERRORCODE_CMD = (byte) 0x9C;
		public static final byte CSAFE_GETWORK_CMD = (byte) 0xA0;
		public static final byte CSAFE_GETHORIZONTAL_CMD = (byte) 0xA1;
		public static final byte CSAFE_GETCALORIES_CMD = (byte) 0xA3;
		public static final byte CSAFE_GETPROGRAM_CMD = (byte) 0xA4;
		public static final byte CSAFE_GETPACE_CMD = (byte) 0xA6;
		public static final byte CSAFE_GETCADENCE_CMD = (byte) 0xA7;
		public static final byte CSAFE_GETUSERINFO_CMD = (byte) 0xAB;
		public static final byte CSAFE_GETHRCUR_CMD = (byte) 0xB0;
		public static final byte CSAFE_GETPOWER_CMD = (byte) 0xB4;
		
		// Long commands
		public static final byte CSAFE_AUTOUPLOAD_CMD = (byte) 0x01;
		public static final byte CSAFE_IDDIGITS_CMD = (byte) 0x10;
		public static final byte CSAFE_SETTIME_CMD = (byte) 0x11;
		public static final byte CSAFE_SETDATE_CMD = (byte) 0x12;
		public static final byte CSAFE_SETTIMEOUT_CMD = (byte) 0x13;
		public static final byte CSAFE_SETUSERCFG1_CMD = (byte) 0x1A;
		public static final byte CSAFE_SETTWORK_CMD = (byte) 0x20;
		public static final byte CSAFE_SETHORIZONTAL_CMD = (byte) 0x21;
		public static final byte CSAFE_SETCALORIES_CMD = (byte) 0x23;
		public static final byte CSAFE_SETPROGRAM_CMD = (byte) 0x24;
		public static final byte CSAFE_SETPOWER_CMD = (byte) 0x34;
		public static final byte CSAFE_GETCAPS_CMD = (byte) 0x70;
	}
	
	public static class SPECIFIC_PM_CODE {
		// Short commands
		public static final byte CSAFE_PM_GET_WORKOUTTYPE = (byte) 0x89;
		public static final byte CSAFE_PM_GET_DRAGFACTOR = (byte) 0xC1;
		public static final byte CSAFE_PM_GET_STROKESTATE = (byte) 0xBF;
		public static final byte CSAFE_PM_GET_WORKTIME = (byte) 0xA0;
		public static final byte CSAFE_PM_GET_WORKDISTANCE = (byte) 0xA3;
		public static final byte CSAFE_PM_GET_ERRORVALUE = (byte) 0xC9;
		public static final byte CSAFE_PM_GET_WORKOUTSTATE = (byte) 0x8D;
		public static final byte CSAFE_PM_GET_WORKOUTINTERVALCOUNT = (byte) 0x9F;
		public static final byte CSAFE_PM_GET_INTERVALTYPE = (byte) 0x8E;
		public static final byte CSAFE_PM_GET_RESTTIME = (byte) 0xCF;
		public static final byte CSAFE_PM_GET_DISPLAYTYPE = (byte) 0x8A;
		public static final byte CSAFE_PM_GET_DISPLAYUNITS = (byte) 0x8B;
		
		// Long commands
		public static final byte CSAFE_PM_SET_SPLITDURATION = (byte) 0x05;
		public static final byte CSAFE_PM_GET_FORCEPLOTDATA = (byte) 0x6B;
		public static final byte CSAFE_PM_SET_SCREENERRORMODE = (byte) 0x27;
		public static final byte CSAFE_PM_GET_HEARTBEATDATA = (byte) 0x6C;
		public static final byte CSAFE_PM_GET_STROKESTATS = (byte) 0x6E;
	}
	
	public abstract class CSAFE_CMD {}
	
	// Short commands
	public class CSAFE_GETSTATUS_CMD extends CSAFE_CMD {
		public byte status;
		
		public CSAFE_GETSTATUS_CMD(byte[] content) {
			this.status = (byte) (content[0] & 0xF);
		}
	}
	
	public class CSAFE_GETVERSION_CMD extends CSAFE_CMD {
		public int manifacturerId;
		public int cid;
		public int model;
		public int hwVersion;
		public int swVersion;
		
		public CSAFE_GETVERSION_CMD(byte[] content) {
			this.manifacturerId = byte2int(content, 0, 1);
			this.cid = byte2int(content, 1, 1);
			this.model = byte2int(content, 2, 1);
			this.hwVersion = byte2int(content, 3, 2);
			this.swVersion = byte2int(content, 5, 2);
		}
	}
	
	public class CSAFE_GETID_CMD extends CSAFE_CMD {
		public String id;
		
		public CSAFE_GETID_CMD(byte[] content) {
			this.id = new String(content);
		}
	}
	
	public class CSAFE_GETUNITS_CMD extends CSAFE_CMD {
		public byte unitsType;
		
		public CSAFE_GETUNITS_CMD(byte[] content) {
			this.unitsType = content[0];
		}
	}
	
	public class CSAFE_GETSERIAL_CMD extends CSAFE_CMD {
		public String serial;
		
		public CSAFE_GETSERIAL_CMD(byte[] content) {
			this.serial = new String(content);
		}
	}
	
	public class CSAFE_GETODOMETER_CMD extends CSAFE_CMD {
		public int distance;
		public byte unitsSpecifier;
		
		public CSAFE_GETODOMETER_CMD(byte[] content) {
			this.distance = byte2int(content, 0, 4);
			this.unitsSpecifier = content[4];
		}
	}
	
	public class CSAFE_GETERRORCODE_CMD extends CSAFE_CMD {
		public int errorCode;
		
		public CSAFE_GETERRORCODE_CMD(byte[] content) {
			this.errorCode = byte2int(content, 0, 3);
		}
	}
	
	public class CSAFE_GETWORK_CMD extends CSAFE_CMD {
		public int hours;
		public int minutes;
		public int seconds;
		
		public CSAFE_GETWORK_CMD(byte[] content) {
			this.hours = byte2int(content, 0, 1);
			this.minutes = byte2int(content, 1, 1);
			this.seconds = byte2int(content, 2, 1);
		}
	}
			
	public class CSAFE_GETHORIZONTAL_CMD extends CSAFE_CMD {
		public int distance;
		public byte unitsSpecifier;
		
		public CSAFE_GETHORIZONTAL_CMD(byte[] content) {
			this.distance = byte2int(content, 0, 2);
			this.unitsSpecifier = content[2];
		}
	}
	
	public class CSAFE_GETCALORIES_CMD extends CSAFE_CMD {
		public int calories;
		
		public CSAFE_GETCALORIES_CMD(byte[] content) {
			this.calories = byte2int(content, 0, 2);
		}
	}
	
	public class CSAFE_GETPROGRAM_CMD extends CSAFE_CMD {
		public int programNumber;
		
		public CSAFE_GETPROGRAM_CMD(byte[] content) {
			this.programNumber = byte2int(content, 0, 1);
		}
	}
			
	public class CSAFE_GETPACE_CMD extends CSAFE_CMD {
		public int strokePace;
		public byte unitsSpecifier;
		
		public CSAFE_GETPACE_CMD(byte[] content) {
			this.strokePace = byte2int(content, 0, 2);
			this.unitsSpecifier = content[2];
		}
	}
	
	public class CSAFE_GETCADENCE_CMD extends CSAFE_CMD {
		public int strokeRate;
		public byte unitsSpecifier;
		
		public CSAFE_GETCADENCE_CMD(byte[] content) {
			this.strokeRate = byte2int(content, 0, 2);
			this.unitsSpecifier = content[2];
		}
	}
	
	public class CSAFE_GETUSERINFO_CMD extends CSAFE_CMD {
		public int weight;
		public byte unitsSpecifier;
		public int age;
		public byte gender;
		
		public CSAFE_GETUSERINFO_CMD(byte[] content) {
			this.weight = byte2int(content, 0, 2);
			this.unitsSpecifier = content[2];
			this.age = byte2int(content, 0, 1);
			this.gender = content[4];
		}
	}
	
	public class CSAFE_GETHRCUR_CMD extends CSAFE_CMD {
		public int beatsPerMinute;
		
		public CSAFE_GETHRCUR_CMD(byte[] content) {
			this.beatsPerMinute = byte2int(content, 0, 1);
		}
	}
	
	public class CSAFE_GETPOWER_CMD extends CSAFE_CMD {
		public int strokeWatts;
		public byte unitsSpecifier;
		
		public CSAFE_GETPOWER_CMD(byte[] content) {
			this.strokeWatts = byte2int(content, 0, 2);
			this.unitsSpecifier = content[2];
		}
	}
	
	// Long commands
	public class CSAFE_GETCAPS_CMD extends CSAFE_CMD {
		public int maxRxFrame;
		public int maxTxFrame;
		public int minInterframe;
		
		public CSAFE_GETCAPS_CMD(byte[] content) {
			this.maxRxFrame = byte2int(content, 0, 1);
			this.maxTxFrame = byte2int(content, 1, 1);
			this.minInterframe = byte2int(content, 2, 1);
		}
	}
	
	// Specific PM: short commands
	public class CSAFE_PM_GET_WORKOUTTYPE extends CSAFE_CMD {
		public byte workoutType;
		
		public CSAFE_PM_GET_WORKOUTTYPE(byte[] content) {
			this.workoutType = content[0];
		}
	}
	
	public class CSAFE_PM_GET_DRAGFACTOR extends CSAFE_CMD {
		public int dragFactor;
		
		public CSAFE_PM_GET_DRAGFACTOR(byte[] content) {
			this.dragFactor = content[0];
		}
	}
	
	public class CSAFE_PM_GET_STROKESTATE extends CSAFE_CMD {
		public byte strokeState;
		
		public CSAFE_PM_GET_STROKESTATE(byte[] content) {
			this.strokeState = content[0];
		}
	}
	
	public class CSAFE_PM_GET_WORKTIME extends CSAFE_CMD {
		public int workTimeCentisecond;
		public int remainingFractionalDurationCentisecond;
		
		public CSAFE_PM_GET_WORKTIME(byte[] content) {
			this.workTimeCentisecond = byte2int(content, 0, 4);
			this.remainingFractionalDurationCentisecond = content[4];
		}
	}
	
	public class CSAFE_PM_GET_WORKDISTANCE extends CSAFE_CMD {
		public int workDistanceDecimeter;
		public int remainingFractionalDistanceDecimeter;
		
		public CSAFE_PM_GET_WORKDISTANCE(byte[] content) {
			this.workDistanceDecimeter = byte2int(content, 0, 4);
			this.remainingFractionalDistanceDecimeter = content[4];
		}
	}
	
	public class CSAFE_PM_GET_ERRORVALUE extends CSAFE_CMD {
		public int errorValue;
		
		public CSAFE_PM_GET_ERRORVALUE(byte[] content) {
			this.errorValue = byte2int(content, 0, 2);
		}
	}
	
	public class CSAFE_PM_GET_WORKOUTSTATE extends CSAFE_CMD {
		public byte workoutState;
		
		public CSAFE_PM_GET_WORKOUTSTATE(byte[] content) {
			this.workoutState = content[0];
		}
	}
	
	public class CSAFE_PM_GET_WORKOUTINTERVALCOUNT extends CSAFE_CMD {
		public int intervalCount;
		
		public CSAFE_PM_GET_WORKOUTINTERVALCOUNT(byte[] content) {
			this.intervalCount = content[0];
		}
	}
	
	public class CSAFE_PM_GET_INTERVALTYPE extends CSAFE_CMD {
		public byte intervalType;
		
		public CSAFE_PM_GET_INTERVALTYPE(byte[] content) {
			this.intervalType = content[0];
		}
	}

	public class CSAFE_PM_GET_RESTTIME extends CSAFE_CMD {
		public int restTimeSecond;
		
		public CSAFE_PM_GET_RESTTIME(byte[] content) {
			this.restTimeSecond = byte2int(content, 0, 2);
		}
	}
	
	public class CSAFE_PM_GET_DISPLAYTYPE extends CSAFE_CMD {
		public byte displayType;
		
		public CSAFE_PM_GET_DISPLAYTYPE(byte[] content) {
			this.displayType = content[0];
		}
	}
	
	public class CSAFE_PM_GET_DISPLAYUNITS extends CSAFE_CMD {
		public byte unitsType;
		
		public CSAFE_PM_GET_DISPLAYUNITS(byte[] content) {
			this.unitsType = content[0];
		}
	}
	
	// Specific PM: long commands
	public class CSAFE_PM_GET_FORCEPLOTDATA extends CSAFE_CMD {
		public int[] forcePlotData;
		
		public CSAFE_PM_GET_FORCEPLOTDATA(byte[] content) {
			this.forcePlotData = new int[content[0]];
			for (int i = 0; i < forcePlotData.length; i++) {
				forcePlotData[i] = content[i + 1];
			}
		}
	}
	
	public class CSAFE_PM_GET_HEARTBEATDATA extends CSAFE_CMD {
		public int[] heartBeats;
		
		public CSAFE_PM_GET_HEARTBEATDATA(byte[] content) {
			heartBeats = new int[content[0]];
			for (int i = 0; i < heartBeats.length; i++) {
				heartBeats[i] = content[i + 1];
			}
		}
	}
	
	public class CSAFE_PM_GET_STROKESTATS extends CSAFE_CMD {
		public int strokeDistanceCentimeter;
		public int strokeDriveTimeCentisecond;
		public int strokeRecoveryTimeCentisecond;
		public int strokeLengthCentimeter;
		public int strokeCount;
		public int strokePeakForceCentinewton;
		public int strokeImpulseForceKilogramPerMillisecond;
		public int strokeAverageForceCentinewton;
		public int workPerStrokeJoule;
		
		public CSAFE_PM_GET_STROKESTATS(byte[] content) {
			this.strokeDistanceCentimeter = byte2int(content, 0, 2);
			this.strokeDriveTimeCentisecond = byte2int(content, 2, 1);
			this.strokeRecoveryTimeCentisecond = byte2int(content, 3, 2);
			this.strokeLengthCentimeter = byte2int(content, 5, 1);
			this.strokeCount = byte2int(content, 6, 2);
			this.strokePeakForceCentinewton = byte2int(content, 8, 2);
			this.strokeImpulseForceKilogramPerMillisecond = byte2int(content, 10, 2);
			this.strokeAverageForceCentinewton = byte2int(content, 12, 2);
			this.workPerStrokeJoule = byte2int(content, 14, 2);
		}
	}
	
	// Process response
	
	public static CSAFE_GETSTATUS_CMD CSAFE_GETSTATUS_CMD(byte[] content) {
		return response.new CSAFE_GETSTATUS_CMD(content);
	}
	
	public static CSAFE_CMD getResponse(byte code, byte[] content) {
		switch (code) {
			// Short commands
			case CODE.CSAFE_GETVERSION_CMD: return response.new CSAFE_GETVERSION_CMD(content);
			case CODE.CSAFE_GETID_CMD: return response.new CSAFE_GETID_CMD(content);
			case CODE.CSAFE_GETUNITS_CMD: return response.new CSAFE_GETUNITS_CMD(content);
			case CODE.CSAFE_GETSERIAL_CMD: return response.new CSAFE_GETSERIAL_CMD(content);
			case CODE.CSAFE_GETODOMETER_CMD: return response.new CSAFE_GETODOMETER_CMD(content);
			case CODE.CSAFE_GETERRORCODE_CMD: return response.new CSAFE_GETERRORCODE_CMD(content);
			case CODE.CSAFE_GETWORK_CMD: return response.new CSAFE_GETWORK_CMD(content);
			case CODE.CSAFE_GETHORIZONTAL_CMD: return response.new CSAFE_GETHORIZONTAL_CMD(content);
			case CODE.CSAFE_GETCALORIES_CMD: return response.new CSAFE_GETCALORIES_CMD(content);
			case CODE.CSAFE_GETPROGRAM_CMD: return response.new CSAFE_GETPROGRAM_CMD(content);
			case CODE.CSAFE_GETPACE_CMD: return response.new CSAFE_GETPACE_CMD(content);
			case CODE.CSAFE_GETCADENCE_CMD: return response.new CSAFE_GETCADENCE_CMD(content);
			case CODE.CSAFE_GETUSERINFO_CMD: return response.new CSAFE_GETUSERINFO_CMD(content);
			case CODE.CSAFE_GETHRCUR_CMD: return response.new CSAFE_GETHRCUR_CMD(content);
			case CODE.CSAFE_GETPOWER_CMD: return response.new CSAFE_GETPOWER_CMD(content);
			// Long commands
			case CODE.CSAFE_GETCAPS_CMD: return response.new CSAFE_GETCAPS_CMD(content);
			
			default: return null;
		}
	}
	
	public static CSAFE_CMD getResponseSpecificPM(byte code, byte[] content) {
		switch (code) {
			// Short commands
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTTYPE: return response.new CSAFE_PM_GET_WORKOUTTYPE(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_DRAGFACTOR: return response.new CSAFE_PM_GET_DRAGFACTOR(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_STROKESTATE: return response.new CSAFE_PM_GET_STROKESTATE(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKTIME: return response.new CSAFE_PM_GET_WORKTIME(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKDISTANCE: return response.new CSAFE_PM_GET_WORKDISTANCE(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_ERRORVALUE: return response.new CSAFE_PM_GET_ERRORVALUE(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTSTATE: return response.new CSAFE_PM_GET_WORKOUTSTATE(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTINTERVALCOUNT: return response.new CSAFE_PM_GET_WORKOUTINTERVALCOUNT(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_INTERVALTYPE: return response.new CSAFE_PM_GET_INTERVALTYPE(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_RESTTIME: return response.new CSAFE_PM_GET_RESTTIME(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_DISPLAYTYPE: return response.new CSAFE_PM_GET_DISPLAYTYPE(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_DISPLAYUNITS: return response.new CSAFE_PM_GET_DISPLAYUNITS(content);

			// Long commands
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_FORCEPLOTDATA: return response.new CSAFE_PM_GET_FORCEPLOTDATA(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_HEARTBEATDATA: return response.new CSAFE_PM_GET_HEARTBEATDATA(content);
			case SPECIFIC_PM_CODE.CSAFE_PM_GET_STROKESTATS: return response.new CSAFE_PM_GET_STROKESTATS(content);
		
			default: return null;
		}
	}
	
	private int byte2int(byte[] value, int startIndex, int length) {
		int result = 0;
		for (int i = 0; i < length; i++) {
			result = (Byte.toUnsignedInt(value[startIndex + i]) << (8 * i)) | result;
		}
		return result;
	}
}
