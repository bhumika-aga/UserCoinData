package com.coindata.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CryptoController {

	private static final String API_URL = "https://pro-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest";
	private static final String API_KEY = "27ab17d1-215f-49e5-9ca4-afd48810c149";

	@GetMapping("/crypto/quotes")
	public JsonNode getCryptoQuotes(@RequestParam String symbol) {
		// Create a RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// Build the request URL with the provided symbol
		String url = API_URL + "?symbol=" + symbol;

		// Set up headers including the API key
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-CMC_PRO_API_KEY", API_KEY);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// Make the HTTP GET request and get the response as a String
		ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

		// Check if the request was successful (HTTP status code 200)
		if (response.getStatusCode() == HttpStatus.OK) {
			// Extract and return the response body
			return response.getBody();
		} else {
			// Handle error responses
			return null;
		}
	}
}