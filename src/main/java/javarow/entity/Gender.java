package javarow.entity;

import java.io.Serializable;

public enum Gender implements Serializable {
	NONE((byte) 0x00),
	MALE((byte) 0x01),
	FEMALE((byte) 0x02),
	UNDEFINED(null);
	
	private final Byte code;
	
	Gender(Byte code) {
		this.code = code;
	}
	
	public static Gender getFromCode(byte code) {
		for (Gender gender: Gender.values()) {
			if (gender.code != null && gender.code == code) {
				return gender;
			}
		}
		return Gender.UNDEFINED;
	}
}
