package javarow.entity;

import java.io.Serializable;

import javarow.Response;

public class HeartBeatPlot implements Serializable {
	private static final long serialVersionUID = 5946729641907082021L;
	private int[] dataBpm;
	
	public HeartBeatPlot() {
		this.dataBpm = new int[0];
	}
	
	public HeartBeatPlot(Response.CSAFE_PM_GET_HEARTBEATDATA heartBeatPlot) {
		this.dataBpm = heartBeatPlot.heartBeats;
	}
	
	public int[] getDataBpm() {
		return dataBpm;
	}
}
