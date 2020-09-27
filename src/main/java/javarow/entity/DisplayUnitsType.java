package javarow.entity;

import java.io.Serializable;

public enum DisplayUnitsType implements Serializable {
	TIME_PER_METER((byte) 0x00),
	PACE((byte) 0x01),
	WATTS((byte) 0x02),
	CALORIES((byte) 0x03),
	UNDEFINED(null);
	
	private final Byte code;
	
	DisplayUnitsType(Byte code) {
		this.code = code;
	}
	
	public static DisplayUnitsType getFromCode(byte code) {
		for (DisplayUnitsType displayUnitsType: DisplayUnitsType.values()) {
			if (displayUnitsType.code != null && displayUnitsType.code == code) {
				return displayUnitsType;
			}
		}
		return DisplayUnitsType.UNDEFINED;
	}
}
