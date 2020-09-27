package javarow.entity;

import java.io.Serializable;

public enum WorkoutType implements Serializable {
	JUST_ROW_NO_SPLITS((byte) 0x00),
	JUST_ROW_SPLITS((byte) 0x01),
	FIXED_DISTANCE_NO_SPLITS((byte) 0x02),
	FIXED_DISTANCE_SPLITS((byte) 0x03),
	FIXED_TIME_NO_SPLITS((byte) 0x04),
	FIXED_TIME_SPLITS((byte) 0x05),
	FIXED_TIME_INTERVAL((byte) 0x06),
	FIXED_DISTANCE_INTERVAL((byte) 0x07),
	VARIABLE_INTERVAL((byte) 0x08),
	UNDEFINED(null);
	
	private final Byte code;
	
	WorkoutType(Byte code) {
		this.code = code;
	}
	
	public static WorkoutType getFromCode(byte code) {
		for (WorkoutType workoutType: WorkoutType.values()) {
			if (workoutType.code != null && workoutType.code == code) {
				return workoutType;
			}
		}
		return WorkoutType.UNDEFINED;
	}
}
