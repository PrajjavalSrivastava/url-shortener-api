package com.prajjaval.urlshortener.service;

import java.time.LocalDateTime;
import com.prajjaval.urlshortener.dto.UrlAnalyticsResponse;

import com.prajjaval.urlshortener.entity.UrlMapping;

public interface UrlService {

	public UrlMapping createShortUrl(
	        String originalUrl,
	        LocalDateTime expiresAt);

	UrlMapping getOriginalUrl(String shortCode);
	
	UrlAnalyticsResponse getAnalytics(String shortCode);
	
	UrlAnalyticsResponse getUrlDetails(String shortCode);
	
	void deleteUrl(String shortCode);
}