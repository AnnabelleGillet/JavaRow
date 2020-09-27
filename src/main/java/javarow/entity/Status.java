package javarow.entity;

import java.io.Serializable;

public enum Status implements Serializable {
	ERROR((byte) 0x00),
	READY((byte) 0x01),
	IDLE((byte) 0x02),
	HAVE_ID((byte) 0x03),
	NA((byte) 0x04),
	IN_USE((byte) 0x05),
	PAUSE((byte) 0x06),
	FINISHED((byte) 0x07),
	MANUAL((byte) 0x08),
	OFFLINE((byte) 0x09),
	UNDEFINED(null);
	
	private final Byte code;
	
	Status(Byte code) {
		this.code = code;
	}
	
	public static Status getFromCode(byte code) {
		for (Status status: Status.values()) {
			if (status.code != null && status.code == code) {
				return status;
			}
		}
		return Status.UNDEFINED;
	}
}
