package com.apiflow.infrastructure.gateway;

import com.apiflow.domain.receipt.HttpReceiptGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateHttpReceiptGateway implements HttpReceiptGateway {

    private final RestTemplate restTemplate;

    @Override
    public int sendHttpRequest(String url, String method, Map<String, String> headers, Map<String, Object> body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::add);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.valueOf(method),
                entity,
                String.class
        );

        return response.getStatusCode().value();
    }
}
