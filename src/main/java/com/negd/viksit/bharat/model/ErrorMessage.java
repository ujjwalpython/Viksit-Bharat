package com.negd.viksit.bharat.model;

import lombok.*;

/**
 * Represents the structure of error messages returned by the API.
 *
 * <p>
 * This class provides a standardized structure for error messages that the API may return
 * in various scenarios, ensuring consistent error reporting to the clients. It includes
 * standard error codes like API_ERROR, SERVER_ERROR, etc., for categorization, along with
 * a detailed message for each error.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorMessage {

	private String correlationId;

	private String errorCode;

	private String message;

	private String details;

	/**
	 * Provides a string representation of the ErrorMessage instance. Includes
	 * correlationId, errorCode and message in a readable format.
	 * @return String A string representation of the ErrorMessage instance.
	 */
	@Override
	public String toString() {
		return "ErrorMessage{" + "correlationId='" + correlationId + '\'' + ", errorCode='" + errorCode + '\''
				+ ", message='" + message + '\'' + '}';
	}

}
