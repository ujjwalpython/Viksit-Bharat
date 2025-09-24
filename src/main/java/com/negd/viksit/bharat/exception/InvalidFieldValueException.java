package com.negd.viksit.bharat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidFieldValueException extends BaseException {

	private final String field;

	private final String message;

	public InvalidFieldValueException(String field, String message) {
		super(HttpStatus.UNPROCESSABLE_ENTITY, String.format("%s : %s", field, message));
		this.field = field;
		this.message = message;
	}

}
