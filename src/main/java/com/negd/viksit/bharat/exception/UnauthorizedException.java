package com.negd.viksit.bharat.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

	public UnauthorizedException(String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}

}