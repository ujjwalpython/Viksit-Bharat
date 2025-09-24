package com.negd.viksit.bharat.enums;

import lombok.Getter;

/**
 * Enumeration defining a set of error codes and messages associated with user service
 * operations. These error codes represent various failure scenarios ranging from
 * validation issues to system errors. They are used to standardize the error reporting
 * throughout the user service and provide clear, consistent feedback to the clients and
 * developers.
 */
@Getter
public enum UserServiceErrorCodeEnum {

	// Application-Specific Business Logic Errors
	VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed for the provided data."),
	INVALID_JSON("INVALID_JSON", "Invalid JSON input, Please check the JSON syntax and structure"),
	DUPLICATE_ENTRY("DUPLICATE_ENTRY", "A duplicate entry with the same values already exists in the system"),
	USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "User already exists."),
	PASSWORD_STRENGTH_FAILED("PASSWORD_STRENGTH_FAILED", "Password does not meet the strength requirements."),
	MAX_ATTEMPTS_EXCEEDED("MAX_ATTEMPTS_EXCEEDED", "Maximum number of attempts exceeded."),

	// Data Access/Integration Errors
	DATABASE_ERROR("DATABASE_ERROR", "A database error occurred."),
	DATA_NOT_FOUND("DATA_NOT_FOUND", "Requested data not found."),
	DATA_CONFLICT("DATA_CONFLICT", "Data conflict detected."),
	EXTERNAL_SERVICE_FAILURE("EXTERNAL_SERVICE_FAILURE", "External service failed or returned an error."),

	// Security and Compliance Errors
	INVALID_TOKEN("INVALID_TOKEN", "The provided token is invalid or expired."),
	ACCESS_DENIED("ACCESS_DENIED", "Access is denied."),
	SECURITY_POLICY_VIOLATION("SECURITY_POLICY_VIOLATION", "Security policy violation detected."),

	// API and Web Service Errors
	DEPRECATED_API("DEPRECATED_API", "The API used is deprecated and no longer supported."),

	// Other Custom Errors
	UNEXPECTED_ERROR("UNEXPECTED_ERROR",
			"An unexpected error occurred in the authentication service. Please provide the correlation ID when contacting support if the problem persists."),
	DOWNSTREAM_SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE",
			"We're sorry, but the service you requested is currently unavailable. Please try again later."),
	SERVICE_TIMEOUT("SERVICE_TIMEOUT", "The service has timed out."),
	SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "The service is unavailable."),
	OPERATION_NOT_PERMITTED("OPERATION_NOT_PERMITTED", "Operation is not permitted."),
	FEATURE_NOT_SUPPORTED("FEATURE_NOT_SUPPORTED", "The requested feature is not supported.");

	private final String code;

	private final String message;

	/**
	 * Constructs a new instance of UserServiceErrorCodeEnum with the provided code and
	 * message. This allows for each error scenario to have a unique code and a
	 * descriptive message that explains the nature of the error.
	 * @param code The unique code representing the error scenario.
	 * @param message The descriptive message associated with the error scenario.
	 */
	UserServiceErrorCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

}
