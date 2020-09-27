package javarow;

public enum ShortSpecificPMCommand {
	CSAFE_PM_GET_WORKOUTTYPE((byte) 0x89, 1),
	CSAFE_PM_GET_DRAGFACTOR((byte) 0xC1, 1),
	CSAFE_PM_GET_STROKESTATE((byte) 0xBF, 1),
	CSAFE_PM_GET_WORKTIME((byte) 0xA0, 5),
	CSAFE_PM_GET_WORKDISTANCE((byte) 0xA3, 5),
	CSAFE_PM_GET_ERRORVALUE((byte) 0xC9, 2),
	CSAFE_PM_GET_WORKOUTSTATE((byte) 0x8D, 1),
	CSAFE_PM_GET_WORKOUTINTERVALCOUNT((byte) 0x9F, 1),
	CSAFE_PM_GET_INTERVALTYPE((byte) 0x8E, 1),
	CSAFE_PM_GET_RESTTIME((byte) 0xCF, 2),
	CSAFE_PM_GET_DISPLAYTYPE((byte) 0x8A, 1),
	CSAFE_PM_GET_DISPLAYUNITS((byte) 0x8B, 1);
	
	public final byte code;
	public final int responseLength;

    ShortSpecificPMCommand(byte code, int responseLength) {
        this.code = code;
        this.responseLength = responseLength;
    }
}
