package com.negd.viksit.bharat.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an error message that contains field-specific validation errors.
 *
 * <p>
 * This class provides a detailed structure for error messages that contain validation
 * errors related to specific fields in the request. Each validation error is associated
 * with a specific field and a corresponding error message.
 *
 * <p>
 * For instance, in a scenario where a user submits a form with multiple fields, and some
 * of these fields contain invalid data, this DTO can be used to communicate back to the
 * user exactly which fields have errors and what those errors are.
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class FieldErrorMessage {

	private String correlationId;

	private String errorCode;

	private String message;

	private List<ValidationErrorMessage> fieldErrors;

	private String details;

	public void addValidationError(String field, String message) {
		if (Objects.isNull(fieldErrors)) {
			fieldErrors = new ArrayList<>();
		}

		fieldErrors.add(new ValidationErrorMessage(field, message));
	}

	@Override
	public String toString() {
		return "FieldErrorMessage{" + "correlationId='" + correlationId + '\'' + ", errorCode='" + errorCode + '\''
				+ ", message='" + message + '\'' + '}';
	}

	@Data
	@EqualsAndHashCode
	@RequiredArgsConstructor
	private static class ValidationErrorMessage {

		private final String field;

		private final String message;

		@Override
		public String toString() {
			return "ValidationErrorMessage{" + "field='" + field + '\'' + ", message='" + message + '\'' + '}';
		}

	}

}
