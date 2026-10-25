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
import com.prajjaval.urlshortener.dto.UrlAnalyticsResponse;
import com.prajjaval.urlshortener.entity.UrlMapping;
import com.prajjaval.urlshortener.service.UrlService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(summary = "Create a short URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Short URL created successfully"),
        @ApiResponse(responseCode = "400",
                description = "Invalid URL input")
    })
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

    @Operation(summary = "Get click analytics for a short URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Analytics retrieved successfully"),
        @ApiResponse(responseCode = "404",
                description = "Short URL not found")
    })
    @GetMapping("/analytics/{shortCode}")
    public UrlAnalyticsResponse getAnalytics(
            @PathVariable String shortCode) {

        return urlService.getAnalytics(shortCode);
    }

    @Operation(summary = "Get short URL details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "URL details retrieved successfully"),
        @ApiResponse(responseCode = "404",
                description = "Short URL not found")
    })
    @GetMapping("/{shortCode}")
    public UrlAnalyticsResponse getUrlDetails(
            @PathVariable String shortCode) {

        return urlService.getUrlDetails(shortCode);
    }

    @Operation(summary = "Delete a short URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204",
                description = "URL deleted successfully"),
        @ApiResponse(responseCode = "404",
                description = "Short URL not found")
    })
    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable String shortCode) {

        urlService.deleteUrl(shortCode);

        return ResponseEntity.noContent().build();
    }
}