package com.prajjaval.urlshortener.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.prajjaval.urlshortener.dto.UrlAnalyticsResponse;
import com.prajjaval.urlshortener.service.UrlService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import com.prajjaval.urlshortener.exception.ResourceNotFoundException;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    @Test
    void getUrlDetails_shouldReturn200AndUrlDetails() throws Exception {

        UrlAnalyticsResponse response = new UrlAnalyticsResponse();
        response.setShortCode("abc123");
        response.setOriginalUrl("https://spring.io");
        response.setClickCount(2L);

        when(urlService.getUrlDetails("abc123"))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/urls/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.originalUrl").value("https://spring.io"))
                .andExpect(jsonPath("$.clickCount").value(2));
    }
    
    @Test
    void getAnalytics_shouldReturn200AndAnalytics() throws Exception {

        UrlAnalyticsResponse response = new UrlAnalyticsResponse();
        response.setShortCode("abc123");
        response.setOriginalUrl("https://spring.io");
        response.setClickCount(5L);

        when(urlService.getAnalytics("abc123"))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/urls/analytics/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.originalUrl").value("https://spring.io"))
                .andExpect(jsonPath("$.clickCount").value(5));
    }
    
    @Test
    void deleteUrl_shouldReturn204() throws Exception {

        doNothing().when(urlService).deleteUrl("abc123");

        mockMvc.perform(delete("/api/v1/urls/abc123"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void getUrlDetails_shouldReturn404WhenShortCodeDoesNotExist()
            throws Exception {

        when(urlService.getUrlDetails("missing"))
                .thenThrow(new ResourceNotFoundException("Short URL not found"));

        mockMvc.perform(get("/api/v1/urls/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Short URL not found"));
    }
}
