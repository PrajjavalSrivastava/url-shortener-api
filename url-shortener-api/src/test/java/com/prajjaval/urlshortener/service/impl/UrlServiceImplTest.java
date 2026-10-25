package com.prajjaval.urlshortener.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prajjaval.urlshortener.dto.UrlAnalyticsResponse;
import com.prajjaval.urlshortener.entity.UrlMapping;
import com.prajjaval.urlshortener.exception.ExpiredUrlException;
import com.prajjaval.urlshortener.exception.ResourceNotFoundException;
import com.prajjaval.urlshortener.repository.UrlRepository;
import com.prajjaval.urlshortener.util.ShortCodeGenerator;

import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlServiceImpl urlService;

    private UrlMapping url;

    @BeforeEach
    void setUp() {
        url = new UrlMapping();
        url.setOriginalUrl("https://spring.io");
        url.setShortCode("abc123");
        url.setCreatedAt(LocalDateTime.now().minusDays(1));
        url.setExpiresAt(null);
        url.setClickCount(2L);
    }

    @Test
    void getAnalytics_shouldReturnAnalyticsWithoutIncrementingClicks() {
        when(urlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(url));

        UrlAnalyticsResponse response = urlService.getAnalytics("abc123");

        assertNotNull(response);
        assertEquals("abc123", response.getShortCode());
        assertEquals("https://spring.io", response.getOriginalUrl());
        assertEquals(2L, response.getClickCount());

        verify(urlRepository, never()).save(any(UrlMapping.class));
    }

    @Test
    void getAnalytics_shouldThrowWhenShortCodeDoesNotExist() {
        when(urlRepository.findByShortCode("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> urlService.getAnalytics("missing"));
    }

    @Test
    void getOriginalUrl_shouldThrowWhenUrlHasExpired() {
        url.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(urlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(url));

        assertThrows(
                ExpiredUrlException.class,
                () -> urlService.getOriginalUrl("abc123"));

        verify(urlRepository, never()).save(any(UrlMapping.class));
    }
    
    @Test
    void deleteUrl_shouldDeleteExistingUrl() {
        when(urlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(url));

        urlService.deleteUrl("abc123");

        verify(urlRepository).delete(url);
    }

    @Test
    void getOriginalUrl_shouldReturnUrlWhenNotExpired() {
        url.setExpiresAt(LocalDateTime.now().plusHours(1));

        when(urlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(url));

        UrlMapping result = urlService.getOriginalUrl("abc123");

        assertNotNull(result);
        assertEquals("abc123", result.getShortCode());
        assertEquals(3L, result.getClickCount());

        verify(urlRepository).save(url);
    }
    
    @Test
    void createShortUrl_shouldSaveUrlWithInitialClickCount() {
        when(urlRepository.existsByShortCode(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(false);

        when(urlRepository.save(any(UrlMapping.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UrlMapping result = urlService.createShortUrl(
                "https://spring.io", null);

        assertNotNull(result);
        assertEquals("https://spring.io", result.getOriginalUrl());
        assertNotNull(result.getShortCode());
        assertEquals(6, result.getShortCode().length());
        assertEquals(0L, result.getClickCount());
        assertNotNull(result.getCreatedAt());

        verify(urlRepository).save(any(UrlMapping.class));
    }
    
    @Test
    void deleteUrl_shouldThrowWhenShortCodeDoesNotExist() {

        when(urlRepository.findByShortCode("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> urlService.deleteUrl("missing"));

        verify(urlRepository, never()).delete(any(UrlMapping.class));
    }
}