package com.prajjaval.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class CreateShortUrlRequest {

    @NotBlank(message = "Original URL must not be blank")

    @Pattern(
            regexp = "^(https?|ftp)://.*$",
            message = "Please provide a valid URL starting with http:// or https://"
    )
    private String originalUrl;

    private LocalDateTime expiresAt;
    
    public CreateShortUrlRequest() {
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}