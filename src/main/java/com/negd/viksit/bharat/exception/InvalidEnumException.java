package com.negd.viksit.bharat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class InvalidEnumException extends BaseException {

	private final String field;

	private final String value;

	private final List<String> allowedValues;

	public InvalidEnumException(String field, String value, List<String> allowedValues) {
		super(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Invalid value for field '%s', allowed values are: %s",
				field, String.join(", ", allowedValues)));
		this.field = field;
		this.value = value;
		this.allowedValues = allowedValues;
	}

}