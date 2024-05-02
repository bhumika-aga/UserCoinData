package com.coindata.controller;

import static java.time.LocalDateTime.now;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.coindata.exception.InvalidUsernameException;
import com.coindata.exception.UserNotFoundException;
import com.coindata.model.CryptoData;
import com.coindata.model.User;
import com.coindata.repository.CryptoDataRepository;
import com.coindata.repository.UserRepository;
import com.coindata.service.impl.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CryptoController {

	private static final String API_URL = "https://pro-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest";
	private static final String API_KEY = "27ab17d1-215f-49e5-9ca4-afd48810c149";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CryptoDataRepository cryptoRepository;

	@GetMapping("/crypto/quotes")
	public ResponseEntity<?> getCryptoQuotes(@RequestParam String symbol) {
		// Get the authenticated user's details
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Check if user is logged in
		if (userDetails != null) {
			// Get user ID
			String username = userDetails.getUsername();

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
			List<String> symbols = Arrays.asList(symbol.split(","));
			Map<String, String> data = new HashMap<>();

			byte[] byteData = null;
			Blob blob = null;
			CryptoData cryptoData = null;
			try {
				for (String symbolString : symbols) {
					byteData = new ObjectMapper().writeValueAsBytes(response.getBody().get("data").get(symbolString));
					blob = new SerialBlob(byteData);
					cryptoData = new CryptoData(username, symbolString,
							response.getBody().get("status").get("timestamp").toString(), blob);
					data.put("username", username);
					data.put(symbolString, response.getBody().get("data").get(symbolString).toString());
					saveResponse(username, cryptoData);
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (SerialException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Check if the request was successful (HTTP status code 200)
			if (response.getStatusCode() == HttpStatus.OK) {
				// Extract and return the response body
				return ResponseHandler.generateResponse("Data found!", HttpStatus.OK,
						response.getBody().get("status").get("timestamp").toString(),
						data);
			} else {
				// Handle error responses
				return ResponseHandler.generateResponse(
						"Error: " + response.getBody().get("status").get("error_message"),
						(HttpStatus) response.getStatusCode(),
						response.getBody().get("status").get("timestamp").toString(),
						response.getBody());
			}
		} else {
			return ResponseHandler.generateResponse("User not logged in.", HttpStatus.UNAUTHORIZED, now().toString(),
					null);
		}
	}

	private void saveResponse(String username, CryptoData data) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		if (!user.getUsername().equals(username)) {
			throw new InvalidUsernameException("Unauthorized access");
		}

		cryptoRepository.saveAndFlush(data);
	}
}