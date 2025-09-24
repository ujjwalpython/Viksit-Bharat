package com.negd.viksit.bharat.enums;

import lombok.Getter;

/**
 * Enumeration representing the possible states of a user's status within the system. This
 * enum provides a standardized set of statuses to clearly define whether a user is
 * currently active or inactive. These statuses can be used throughout the application to
 * manage access, track user state, and enforce rules based on the user's status.
 */
@Getter
public enum UserStatusEnum {

	INACTIVE("InActive", "InActive"), ACTIVE("Active", "Active");

	private final String code;

	private final String value;

	/**
	 * Constructs a new UserStatusEnum instance with the provided code and value. This
	 * constructor sets up the enumeration constants with the specified details, ensuring
	 * each status is uniquely identifiable and understandable.
	 * @param code The code representing the user status.
	 * @param value The human-readable value representing the user status.
	 */
	UserStatusEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}

}
