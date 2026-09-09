package com.chatApplication.chat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin
public class GeminiController {

    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.url}")
    private String url;

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> request) {

        String userMessage = request.get("message");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Message empty hai.";
        }
        

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        Map<String, Object> part = new HashMap<>();
        part.put("text", userMessage);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(content));

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );

            Map responseBody = response.getBody();

            if (responseBody == null) {
                return "Please try again!.";
            }

            List candidates =
                    (List) responseBody.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return "Server doesn't get response.";
            }

            Map candidate =
                    (Map) candidates.get(0);

            Map contentResponse =
                    (Map) candidate.get("content");

            if (contentResponse == null) {
                return "Please try again!.";
            }

            List parts =
                    (List) contentResponse.get("parts");

            if (parts == null || parts.isEmpty()) {
                return "Please try again!.";
            }

            Map firstPart =
                    (Map) parts.get(0);

            return String.valueOf(
                    firstPart.get("text")
            );

        } catch (Exception e) {

            e.printStackTrace();

            return "Please try again after somtime.";
        }
    }
}