package javarow.entity;

import java.io.Serializable;

public enum IntervalType implements Serializable {
	TIME((byte) 0x00),
	DISTANCE((byte) 0x01),
	REST((byte) 0x02),
	UNDEFINED(null);
	
	private final Byte code;
	
	IntervalType(Byte code) {
		this.code = code;
	}
	
	public static IntervalType getFromCode(byte code) {
		for (IntervalType intervalType: IntervalType.values()) {
			if (intervalType.code != null && intervalType.code == code) {
				return intervalType;
			}
		}
		return IntervalType.UNDEFINED;
	}
}
