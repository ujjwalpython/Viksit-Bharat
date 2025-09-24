package com.negd.viksit.bharat.model;

import lombok.*;

/**
 * Represents a generic success message structure used in responses. This class provides a
 * standardized way to return success messages along with any associated data.
 *
 * @param <T> the type of the data being returned in the success message.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class SuccessMessage<T> {

	private String message;

	private String correlationId;

	private T data;

	/**
	 * Provides a string representation of the SuccessMessage instance. Includes the
	 * message, correlationId, and data in a readable format.
	 * @return String A string representation of the SuccessMessage instance.
	 */
	@Override
	public String toString() {
		return "SuccessMessage{" + "data=" + data + ", message='" + message + '\'' + ", correlationId='" + correlationId
				+ '\'' + '}';
	}

}
