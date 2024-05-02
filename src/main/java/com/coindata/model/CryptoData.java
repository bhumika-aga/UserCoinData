package com.coindata.model;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "crypto")
public class CryptoData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String username;

	@NotNull
	private String symbols;

	@Column(nullable = false)
	private String timestamp;

	@Lob
	private Blob data;

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

	public String getSymbols() {
		return symbols;
	}

	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Blob getData() {
		return data;
	}

	public void setData(Blob data) {
		this.data = data;
	}

	public CryptoData() {
	}

	public CryptoData(@NotNull String username, @NotNull String symbols, String timestamp, Blob data) {
		this.username = username;
		this.symbols = symbols;
		this.timestamp = timestamp;
		this.data = data;
	}
}