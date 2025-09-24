package com.negd.viksit.bharat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HelperUtil {


	public static String toJson(Object response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(response);
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

}
