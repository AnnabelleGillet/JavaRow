package javarow.entity;

import java.io.Serializable;

public enum WorkoutState implements Serializable {
	WAITING_TO_BEGIN((byte) 0x00),
	WORKOUT_ROW((byte) 0x01),
	COUNTDOWN_PAUSE((byte) 0x02),
	INTERVAL_REST((byte) 0x03),
	WORK_TIME_INTERVAL((byte) 0x04),
	REST_INTERVAL_END_TO_WORK_TIME_INTERVAL_BEGIN((byte) 0x06),
	REST_INTERVAL_END_TO_WORK_DISTANCE_INTERVAL_BEGIN((byte) 0x07),
	WORK_TIME_INTERVAL_END_TO_REST_INTERVAL_BEGIN((byte) 0x08),
	WORK_DISTANCE_INTERVAL_END_TO_REST_INTERVAL_BEGIN((byte) 0x09),
	WORKOUT_END((byte) 0x0A),
	WORKOUT_TERMINATE((byte) 0x0B),
	WORKOUT_LOGGED((byte) 0x0C),
	WORKOUT_REARM((byte) 0x0D),
	UNDEFINED(null);
	
	private final Byte code;
	
	WorkoutState(Byte code) {
		this.code = code;
	}
	
	public static WorkoutState getFromCode(byte code) {
		for (WorkoutState workoutState: WorkoutState.values()) {
			if (workoutState.code != null && workoutState.code == code) {
				return workoutState;
			}
		}
		return WorkoutState.UNDEFINED;
	}
}
