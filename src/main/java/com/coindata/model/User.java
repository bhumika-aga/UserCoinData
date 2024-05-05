package com.coindata.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number")
	private String mobile;

	@NotBlank
	@Size(min = 4, max = 15)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only characters and digits")
	private String username;

	@Size(min = 8, max = 15, message = "Password length must be between 8 and 15 characters")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character")
	private String password;

	@Pattern(regexp = "^[a-zA-Z0-9]{10}$", message = "The PAN No. must be of the form ABCDE1234F")
	private String userPan;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public String getUserPan() {
		return userPan;
	}

	public void setUserPan(String userPan) {
		this.userPan = userPan;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User() {
	}

	public User(@NotBlank @Email String email,
			@NotBlank @Size(min = 4, max = 15) @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only characters and digits") String username,
			@Size(min = 8, max = 15, message = "Password length must be between 8 and 15 characters") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character") String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public User(@NotBlank String firstName, @NotBlank String lastName,
			@NotBlank @Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number") String mobile,
			@Size(min = 8, max = 15, message = "Password length must be between 8 and 15 characters") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character") String password,
			@NotBlank @Pattern(regexp = "^[a-zA-Z0-9]{10}$", message = "The PAN No. must be of the form ABCDE1234F") String userPan) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.password = password;
		this.userPan = userPan;
	}

	public User(@NotBlank String firstName, @NotBlank String lastName, @NotBlank @Email String email,
			@NotBlank @Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number") String mobile,
			@NotBlank @Size(min = 4, max = 15) @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only characters and digits") String username,
			@Size(min = 8, max = 15, message = "Password length must be between 8 and 15 characters") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character") String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobile = mobile;
		this.username = username;
		this.password = password;
	}

}