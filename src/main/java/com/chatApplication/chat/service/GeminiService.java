package com.chatApplication.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String askGemini(String message) {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        Map<String, Object> part = Map.of(
                "text", message
        );

        Map<String, Object> content = Map.of(
                "parts", List.of(part)
        );

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(content)
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        // Maximum 3 attempts
        for (int attempt = 1; attempt <= 3; attempt++) {

            try {

                ResponseEntity<Map> response =
                        restTemplate.postForEntity(
                                url,
                                request,
                                Map.class
                        );

                Map body = response.getBody();

                if (body == null) {
                    return "Gemini se response nahi mila.";
                }

                List candidates =
                        (List) body.get("candidates");

                Map candidate =
                        (Map) candidates.get(0);

                Map contentResponse =
                        (Map) candidate.get("content");

                List parts =
                        (List) contentResponse.get("parts");

                Map firstPart =
                        (Map) parts.get(0);

                return firstPart.get("text").toString();

            } catch (HttpServerErrorException.ServiceUnavailable e) {

                System.out.println(
                        "Gemini 503 - Attempt " + attempt
                );

                if (attempt < 3) {

                    try {
                        Thread.sleep(attempt * 1500L);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                } else {

                    return "Gemini abhi busy hai. Thodi der baad try karo.";
                }

            } catch (Exception e) {

                e.printStackTrace();

                return "Gemini se response nahi aa raha.";
            }
        }

        return "Gemini se response nahi mila.";
    }
}