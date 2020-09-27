package javarow.entity;

import java.io.Serializable;

import javarow.Response;

public class Monitor implements Serializable {
	private static final long serialVersionUID = -1180445496464368794L;
	private int timeCentisecond;
	private int distanceDecimeter;
	private int spm;
	private int powerWatts;
	private int paceSecondPerKilometer;
	private int caloriesPerHour;
	private int calories;
	private int heartrateBpm;
	
	public Monitor() {
		this(null, null, null, null, null, null, null);
	}
	
	public Monitor(Response.CSAFE_PM_GET_WORKTIME workTime, Response.CSAFE_PM_GET_WORKDISTANCE workDistance,
			Response.CSAFE_GETCADENCE_CMD cadence, Response.CSAFE_GETPOWER_CMD power, Response.CSAFE_GETPACE_CMD pace,
			Response.CSAFE_GETCALORIES_CMD calories, Response.CSAFE_GETHRCUR_CMD heartRate) {
		this.timeCentisecond = workTime != null ? workTime.workTimeCentisecond : 0;
		this.distanceDecimeter = workDistance != null ? workDistance.workDistanceDecimeter : 0;
		this.spm = cadence != null ? cadence.strokeRate : 0;
		this.powerWatts = power != null ? power.strokeWatts : 0;
		this.paceSecondPerKilometer = pace != null ? pace.strokePace : 0;
		this.caloriesPerHour = power != null ? (int) (powerWatts * (4 * 0.8604) + 300) : 0;
		this.calories = calories != null ? calories.calories : 0;
		this.heartrateBpm = heartRate != null ? heartRate.beatsPerMinute : 0;
	}

	public int getTimeCentisecond() {
		return timeCentisecond;
	}

	public int getDistanceDecimeter() {
		return distanceDecimeter;
	}

	public int getSpm() {
		return spm;
	}

	public int getPower() {
		return powerWatts;
	}

	public int getPaceSecondPerKilometer() {
		return paceSecondPerKilometer;
	}

	public int getCaloriesPerHour() {
		return caloriesPerHour;
	}

	public int getCalories() {
		return calories;
	}

	public int getHeartrateBpm() {
		return heartrateBpm;
	}

	@Override
	public String toString() {
		return "Time: " + timeCentisecond + " cs\n" +
				"Distance: " + distanceDecimeter + " cm\n" +
				"Stroke per minute: " + spm + " spm\n" +
				"Power: " + powerWatts + " watts\n" +
				"Pace: " + paceSecondPerKilometer + " s/m\n" +
				"Calories/hour: " + caloriesPerHour + "\n" +
				"Calories: " + calories + "\n" +
				"Heart rate: " + heartrateBpm + " bpm";
	}
}
