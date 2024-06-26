package com.coindata.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, String timestamp,
			Object responseObj) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("timestamp", timestamp);
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}
}