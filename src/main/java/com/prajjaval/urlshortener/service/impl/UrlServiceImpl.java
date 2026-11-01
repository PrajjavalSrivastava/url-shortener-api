package com.prajjaval.urlshortener.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.prajjaval.urlshortener.entity.UrlMapping;
import com.prajjaval.urlshortener.repository.UrlRepository;
import com.prajjaval.urlshortener.service.UrlService;
import com.prajjaval.urlshortener.util.ShortCodeGenerator;
import com.prajjaval.urlshortener.exception.ResourceNotFoundException;

import com.prajjaval.urlshortener.exception.ExpiredUrlException;

import com.prajjaval.urlshortener.dto.UrlAnalyticsResponse;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlServiceImpl implements UrlService {

	private final UrlRepository urlRepository;

	public UrlServiceImpl(UrlRepository urlRepository) {
		this.urlRepository = urlRepository;
	}

	@Override
	@Transactional
	public UrlMapping createShortUrl(String originalUrl, LocalDateTime expiresAt) {

		String shortCode;

		do {
			shortCode = ShortCodeGenerator.generateShortCode();
		} while (urlRepository.existsByShortCode(shortCode));

		UrlMapping url = new UrlMapping();

		url.setOriginalUrl(originalUrl);
		url.setShortCode(shortCode);
		url.setCreatedAt(LocalDateTime.now());
		url.setClickCount(0L);
		url.setExpiresAt(expiresAt);

		return urlRepository.save(url);
	}

	@Override
	@Transactional
	public UrlMapping getOriginalUrl(String shortCode) {

		UrlMapping url = urlRepository.findByShortCode(shortCode)
				.orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

		if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {

			throw new ExpiredUrlException("Short URL has expired");
		}

		url.setClickCount(url.getClickCount() + 1);

		urlRepository.save(url);

		return url;
	}

	@Override
	@Transactional(readOnly = true)
	public UrlAnalyticsResponse getAnalytics(String shortCode) {

		UrlMapping url = urlRepository.findByShortCode(shortCode)
				.orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

		return mapToResponse(url);
	}

	@Override
	public UrlAnalyticsResponse getUrlDetails(String shortCode) {

		UrlMapping url = urlRepository.findByShortCode(shortCode)
				.orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

		return mapToResponse(url);
	}

	@Override
	@Transactional
	public void deleteUrl(String shortCode) {

		UrlMapping url = urlRepository.findByShortCode(shortCode)
				.orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

		urlRepository.delete(url);
	}

	private UrlAnalyticsResponse mapToResponse(UrlMapping url) {

		UrlAnalyticsResponse response = new UrlAnalyticsResponse();

		response.setShortCode(url.getShortCode());
		response.setOriginalUrl(url.getOriginalUrl());
		response.setClickCount(url.getClickCount());
		response.setCreatedAt(url.getCreatedAt());
		response.setExpiresAt(url.getExpiresAt());

		return response;
	}
}