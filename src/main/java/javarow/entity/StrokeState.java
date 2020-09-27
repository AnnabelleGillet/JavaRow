package javarow.entity;

import java.io.Serializable;

public enum StrokeState implements Serializable {
	WAITING_FOR_WHEEL_TO_REACH_MIN_SPEED((byte) 0x00),
	WAITING_FOR_WHEEL_TO_ACCELERATE((byte) 0x01),
	DRIVING((byte) 0x02),
	DWELLING_AFTER_DRIVE((byte) 0x03),
	RECOVERY((byte) 0x04),
	UNDEFINED(null);
	// Note: Catch would be the	transition from recovery to	driving. 
	// End-of-stroke would be the transition from driving to dwelling after drive
	
	private final Byte code;
	
	StrokeState(Byte code) {
		this.code = code;
	}
	
	public static StrokeState getFromCode(byte code) {
		for (StrokeState strokeState: StrokeState.values()) {
			if (strokeState.code != null && strokeState.code == code) {
				return strokeState;
			}
		}
		return StrokeState.UNDEFINED;
	}
}
