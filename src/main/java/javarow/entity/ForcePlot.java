package javarow.entity;

import java.io.Serializable;

import javarow.Response;

public class ForcePlot implements Serializable {
	private static final long serialVersionUID = 552216649152687095L;
	private int[] dataWatts;
	
	public ForcePlot() {
		this.dataWatts = new int[0];
	}
	
	public ForcePlot(Response.CSAFE_PM_GET_FORCEPLOTDATA forcePlot) {
		this.dataWatts = forcePlot.forcePlotData;
	}
	
	public int[] getDataWatts() {
		return dataWatts;
	}
}
