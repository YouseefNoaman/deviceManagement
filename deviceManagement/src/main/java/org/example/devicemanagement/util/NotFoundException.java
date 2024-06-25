package org.example.devicemanagement.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -2728703941480057461L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(final String message) {
		super(message);
	}

}
