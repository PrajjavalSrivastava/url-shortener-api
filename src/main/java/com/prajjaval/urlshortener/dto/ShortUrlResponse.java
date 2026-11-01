package com.prajjaval.urlshortener.dto;

public class ShortUrlResponse {

    private String originalUrl;
    private String shortCode;

    // getters and setters
    public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
}
