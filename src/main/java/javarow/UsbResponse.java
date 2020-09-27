package javarow;

import java.util.HashMap;
import java.util.Map;

public class UsbResponse {
	public Map<Byte, Response.CSAFE_CMD> csafeResponses;
	public Map<Byte, Response.CSAFE_CMD> specificPMResponses;
	
	public UsbResponse() {
		csafeResponses = new HashMap<>();
		specificPMResponses = new HashMap<>();
	}
}
