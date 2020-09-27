package javarow.entity;

import java.io.Serializable;

public enum DisplayType implements Serializable {
	STANDARD((byte) 0x00),
	FORCE_PER_VELOCITY((byte) 0x01),
	PACEBOAT((byte) 0x02),
	PER_STROKE((byte) 0x03),
	SIMPLE((byte) 0x04),
	TARGET((byte) 0x05),
	UNDEFINED(null);
	
	private final Byte code;
	
	DisplayType(Byte code) {
		this.code = code;
	}
	
	public static DisplayType getFromCode(byte code) {
		for (DisplayType displayType: DisplayType.values()) {
			if (displayType.code != null && displayType.code == code) {
				return displayType;
			}
		}
		return DisplayType.UNDEFINED;
	}
}
