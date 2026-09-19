package com.prajjaval.urlshortener.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prajjaval.urlshortener.dto.CreateShortUrlRequest;
import com.prajjaval.urlshortener.dto.ShortUrlResponse;
import com.prajjaval.urlshortener.entity.UrlMapping;
import com.prajjaval.urlshortener.service.UrlService;

import jakarta.validation.Valid;

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
}
