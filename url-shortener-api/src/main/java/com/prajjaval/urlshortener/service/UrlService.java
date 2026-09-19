package com.prajjaval.urlshortener.service;

import java.time.LocalDateTime;

import com.prajjaval.urlshortener.entity.UrlMapping;

public interface UrlService {

	public UrlMapping createShortUrl(
	        String originalUrl,
	        LocalDateTime expiresAt);

	UrlMapping getOriginalUrl(String shortCode);
}