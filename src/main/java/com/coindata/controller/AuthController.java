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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coindata.config.JWTUtils;
import com.coindata.exception.InvalidEmailException;
import com.coindata.exception.InvalidUsernameException;
import com.coindata.model.entity.User;
import com.coindata.model.request.LoginRequest;
import com.coindata.model.request.SignUpRequest;
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
			return new ResponseEntity<>(new InvalidUsernameException("Username Already Exists!"),
					HttpStatus.BAD_REQUEST);
		}

		if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
			return new ResponseEntity<>(new InvalidEmailException("Email Already Exists!"), HttpStatus.BAD_REQUEST);
		}

		// Create new user's account
		User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getMobile(),
				request.getUsername(), encoder.encode(request.getPassword()));

		userRepository.save(user);

		return new ResponseEntity<>("User registered successfully!" + user, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
		Authentication auth = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(auth);
		String token = utils.generateJwtToken(auth);
		logger.info("Generating token : " + token);

		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		return new ResponseEntity<>(
				new JWTResponse(userDetails.getUserId(), userDetails.getUsername(), userDetails.getEmail(), token),
				HttpStatus.OK);
	}

//	@PutMapping("/update")
//	public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateRequest request) {
//		
//	}
}