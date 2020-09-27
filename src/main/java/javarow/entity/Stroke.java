package javarow.entity;

import java.io.Serializable;

import javarow.Response;

public class Stroke implements Serializable {
	private static final long serialVersionUID = 2644305927902067778L;
	private StrokeState state;
	private int distanceCentimeter;
	private int driveTimeCentisecond;
	private int recoveryTimeCentisecond;
	private int lengthCentimeter;
	private int count;
	private int peakForceCentinewton;
	private int impulseForceCentigramPerMeterPerSecond;
	private int averageForceCentinewton;
	private int workPerStrokeJoule;
	
	public Stroke() {
		this(null, null);
	}
	
	public Stroke(Response.CSAFE_PM_GET_STROKESTATE strokeState, Response.CSAFE_PM_GET_STROKESTATS strokeStats) {
		this.state = strokeState != null ? StrokeState.getFromCode(strokeState.strokeState) : StrokeState.UNDEFINED;
		this.distanceCentimeter = strokeStats != null ? strokeStats.strokeDistanceCentimeter : 0;
		this.driveTimeCentisecond = strokeStats != null ? strokeStats.strokeDriveTimeCentisecond : 0;
		this.recoveryTimeCentisecond = strokeStats != null ? strokeStats.strokeRecoveryTimeCentisecond : 0;
		this.lengthCentimeter = strokeStats != null ? strokeStats.strokeLengthCentimeter : 0;
		this.count = strokeStats != null ? strokeStats.strokeCount : 0;
		this.peakForceCentinewton = strokeStats != null ? strokeStats.strokePeakForceCentinewton : 0;
		this.impulseForceCentigramPerMeterPerSecond = strokeStats != null ? strokeStats.strokeImpulseForceKilogramPerMillisecond : 0;
		this.averageForceCentinewton = strokeStats != null ? strokeStats.strokeAverageForceCentinewton : 0;
		this.workPerStrokeJoule = strokeStats != null ? strokeStats.workPerStrokeJoule : 0;
	}
	
	public StrokeState getState() {
		return state;
	}

	public int getDistanceCentimeter() {
		return distanceCentimeter;
	}

	public int getDriveTimeCentisecond() {
		return driveTimeCentisecond;
	}

	public int getRecoveryTimeCentisecond() {
		return recoveryTimeCentisecond;
	}

	public int getLengthCentimeter() {
		return lengthCentimeter;
	}

	public int getCount() {
		return count;
	}

	public int getPeakForceCentinewton() {
		return peakForceCentinewton;
	}

	public int getImpulseForceCentigramPerMeterPerSecond() {
		return impulseForceCentigramPerMeterPerSecond;
	}

	public int getAverageForceCentinewton() {
		return averageForceCentinewton;
	}

	public int getWorkPerStrokeJoule() {
		return workPerStrokeJoule;
	}
	
	@Override
	public String toString() {
		return "State: " + state.name() + "\n" +
				"Drive time: " + driveTimeCentisecond + " cs\n" +
				"Recovery time: " + recoveryTimeCentisecond + " cs\n" +
				"Length: " + lengthCentimeter + " cm\n" +
				"Count: " + count + "\n" +
				"Peak force: " + peakForceCentinewton + " cN\n" +
				"Impulse force: " + impulseForceCentigramPerMeterPerSecond + "cg/m/s\n" +
				"Work per stroke: " + workPerStrokeJoule + " J";
	}
}
