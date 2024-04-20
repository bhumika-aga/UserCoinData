package com.coindata.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coindata.config.JWTUtils;
import com.coindata.exception.InvalidUsernameException;
import com.coindata.exception.UserNotFoundException;
import com.coindata.model.User;
import com.coindata.model.request.LoginRequest;
import com.coindata.model.request.SignUpRequest;
import com.coindata.model.request.UpdateRequest;
import com.coindata.model.response.JWTResponse;
import com.coindata.repository.UserRepository;
import com.coindata.service.impl.UserDetailsImpl;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JWTUtils utils;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
		if (Boolean.TRUE.equals(userRepository.existsByUsername(request.getUsername()))) {
			return ResponseHandler.generateResponse("Username Already Exists", HttpStatus.BAD_REQUEST, null);
		}

		if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
			return ResponseHandler.generateResponse("Email Already Exists", HttpStatus.BAD_REQUEST, null);
		}

		// Create new user's account
		User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getMobile(),
				request.getUsername(), encoder.encode(request.getPassword()));

		userRepository.save(user);

		return ResponseHandler.generateResponse("User registered successfully!", HttpStatus.OK, user);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
		Authentication auth = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(auth);
		String token = utils.generateJwtToken(auth);
		logger.info("Generating token : " + token);

		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		return ResponseHandler.generateResponse("Login Successful!", HttpStatus.OK,
				new JWTResponse(userDetails.getUserId(), userDetails.getUsername(), userDetails.getEmail(), token));
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateRequest request) {

		// Get the authenticated user's details
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Check if user is logged in
		if (userDetails != null) {
			// Get user ID
			String username = userDetails.getUsername();

			// Update user details
			try {
				User updatedUser = updateUser(username, request);
				return ResponseHandler.generateResponse("User updated successfully!", HttpStatus.OK, updatedUser);
			} catch (UserNotFoundException e) {
				return ResponseHandler.generateResponse("User does not exist!", HttpStatus.NOT_FOUND, null);
			} catch (InvalidUsernameException e) {
				return ResponseHandler.generateResponse("Unauthorized access!", HttpStatus.UNAUTHORIZED, null);
			} catch (Exception e) {
				return ResponseHandler.generateResponse("User could not be updated!", HttpStatus.INTERNAL_SERVER_ERROR,
						null);
			}
		} else {
			return ResponseHandler.generateResponse("User not logged in!", HttpStatus.UNAUTHORIZED, null);
		}
	}

	private User updateUser(String username, UpdateRequest request) throws UserNotFoundException, Exception {
		// Retrieve the user from the database
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		// Check if the current user has permission to update the details
		// This could be based on roles or any other criteria
		// For simplicity, let's assume any logged-in user can update their own details
		// You might want to implement more fine-grained authorization logic
		// using Spring Security annotations or custom authorization logic
		if (!user.getUsername().equals(username)) {
			throw new InvalidUsernameException("Unauthorized access");
		}

		// Update the user details
		if (request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (request.getMobile() != null) {
			user.setMobile(request.getMobile());
		}
		if (request.getPassword() != null) {
			user.setPassword(encoder.encode(request.getPassword()));
		}

		// Save the updated user object
		return userRepository.save(user);
	}
}