package com.coindata.model.response;

public class JWTResponse {

	private Long id;
	private String username;
	private String email;
	private String accessToken;
	private String tokenType = "Bearer";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public JWTResponse(Long id, String username, String email, String accessToken) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.accessToken = accessToken;
	}
}