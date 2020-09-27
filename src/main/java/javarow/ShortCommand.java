package javarow;

public enum ShortCommand {
	CSAFE_GETSTATUS_CMD((byte) 0x80, 0),
	CSAFE_RESET_CMD((byte) 0x81, 0),
	CSAFE_GOIDLE_CMD((byte) 0x82, 0),
	CSAFE_GOHAVEID_CMD((byte) 0x83, 0),
	CSAFE_GOINUSE_CMD((byte) 0x85, 0),
	CSAFE_GOFINISHED_CMD((byte) 0x86, 0),
	CSAFE_GOREADY_CMD((byte) 0x87, 0),
	CSAFE_BADID_CMD((byte) 0x88, 0),
	CSAFE_GETVERSION_CMD((byte) 0x91, 5),
	CSAFE_GETID_CMD((byte) 0x92, 5),
	CSAFE_GETUNITS_CMD((byte) 0x93, 1),
	CSAFE_GETSERIAL_CMD((byte) 0x94, 9),
	CSAFE_GETODOMETER_CMD((byte) 0x9B, 5),
	CSAFE_GETERRORCODE_CMD((byte) 0x9C, 3),
	CSAFE_GETTWORK_CMD((byte) 0xA0, 3),
	CSAFE_GETHORIZONTAL_CMD((byte) 0xA1, 3),
	CSAFE_GETCALORIES_CMD((byte) 0xA3, 2),
	CSAFE_GETPROGRAM_CMD((byte) 0xA4, 1),
	CSAFE_GETPACE_CMD((byte) 0xA6, 3),
	CSAFE_GETCADENCE_CMD((byte) 0xA7, 3),
	CSAFE_GETUSERINFO_CMD((byte) 0xAB, 5),
	CSAFE_GETHRCUR_CMD((byte) 0xB0, 5),
	CSAFE_GETPOWER_CMD((byte) 0xB4, 3);
	
	public final byte code;
	public final int responseLength;

    ShortCommand(byte code, int responseLength) {
        this.code = code;
        this.responseLength = responseLength;
    }
}
