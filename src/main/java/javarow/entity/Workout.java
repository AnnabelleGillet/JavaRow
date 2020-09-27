package javarow.entity;

import java.io.Serializable;

import javarow.Response;

public class Workout implements Serializable {
	private static final long serialVersionUID = 9154114387466856135L;
	private WorkoutState workoutState;
	private WorkoutType workoutType;
	private IntervalType intervalType;
	private int intervalCount;
	
	public Workout() {
		this(null, null, null, null);
	}
	
	public Workout(Response.CSAFE_PM_GET_WORKOUTSTATE workoutState, Response.CSAFE_PM_GET_WORKOUTTYPE workoutType,
				Response.CSAFE_PM_GET_INTERVALTYPE intervalType, 
				Response.CSAFE_PM_GET_WORKOUTINTERVALCOUNT workoutIntervalCount) {
		this.workoutState = workoutState != null ? WorkoutState.getFromCode(workoutState.workoutState) : WorkoutState.UNDEFINED;
		this.workoutType = workoutType != null ? WorkoutType.getFromCode(workoutType.workoutType) : WorkoutType.UNDEFINED;
		this.intervalType = intervalType != null ? IntervalType.getFromCode(intervalType.intervalType) : IntervalType.UNDEFINED;
		this.intervalCount = workoutIntervalCount != null ? workoutIntervalCount.intervalCount : 0;
	}

	public WorkoutState getWorkoutState() {
		return workoutState;
	}

	public WorkoutType getWorkoutType() {
		return workoutType;
	}

	public IntervalType getIntervalType() {
		return intervalType;
	}

	public int getIntervalCount() {
		return intervalCount;
	}
	
	@Override
	public String toString() {
		return "Workout state: " + workoutState.name() +
				"Workout type: " + workoutType.name() +
				"Interval type: " + intervalType.name() +
				"Interval count: " + intervalCount;
	}
}
