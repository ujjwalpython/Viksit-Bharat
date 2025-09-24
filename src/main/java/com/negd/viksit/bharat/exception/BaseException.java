package com.negd.viksit.bharat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class BaseException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	private final HttpStatus status;

	public BaseException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

}