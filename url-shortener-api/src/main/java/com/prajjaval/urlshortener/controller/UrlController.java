package com.prajjaval.urlshortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prajjaval.urlshortener.dto.CreateShortUrlRequest;
import com.prajjaval.urlshortener.dto.ShortUrlResponse;
import com.prajjaval.urlshortener.entity.UrlMapping;
import com.prajjaval.urlshortener.service.UrlService;

import jakarta.validation.Valid;
import com.prajjaval.urlshortener.dto.UrlAnalyticsResponse;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ShortUrlResponse createShortUrl(
            @Valid @RequestBody CreateShortUrlRequest request) {

    	UrlMapping saved = urlService.createShortUrl(
    	        request.getOriginalUrl(),
    	        request.getExpiresAt());

        ShortUrlResponse response = new ShortUrlResponse();

        response.setOriginalUrl(saved.getOriginalUrl());
        response.setShortCode(saved.getShortCode());

        return response;
    }
    
    @GetMapping("/analytics/{shortCode}")
    public UrlAnalyticsResponse getAnalytics(
            @PathVariable String shortCode) {

        return urlService.getAnalytics(shortCode);
    }
    
    @GetMapping("/{shortCode}")
    public UrlAnalyticsResponse getUrlDetails(
            @PathVariable String shortCode) {

        return urlService.getUrlDetails(shortCode);
    }
    
    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable String shortCode) {

        urlService.deleteUrl(shortCode);

        return ResponseEntity.noContent().build();
    }
}
