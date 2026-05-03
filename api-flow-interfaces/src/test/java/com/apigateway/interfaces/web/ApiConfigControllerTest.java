package com.apigateway.interfaces.web;

import com.apigateway.application.service.ApiConfigApplicationService;
import com.apigateway.common.result.Result;
import com.apigateway.domain.api.config.enums.ApiConfigStatus;
import com.apigateway.domain.api.config.model.ApiConfigEntity;
import com.apigateway.interfaces.web.dto.ApiConfigCreateRequest;
import com.apigateway.interfaces.web.dto.ApiConfigUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiConfigController.class)
class ApiConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApiConfigApplicationService apiConfigApplicationService;

    @Test
    void shouldCreateApiConfig() throws Exception {
        ApiConfigCreateRequest request = new ApiConfigCreateRequest();
        request.setApiCode("TEST_API_001");
        request.setApiName("Test API");
        request.setRequestTimeoutMs(30000);
        request.setAutoRetryCount(3);

        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .build();

        when(apiConfigApplicationService.createApiConfig(any(ApiConfigEntity.class))).thenReturn(entity);

        mockMvc.perform(post("/api/v1/api-configs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldGetApiConfigByApiCode() throws Exception {
        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .status(ApiConfigStatus.ENABLED)
                .build();

        when(apiConfigApplicationService.getApiConfigByApiCode("TEST_API_001")).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/api/v1/api-configs/api-code/TEST_API_001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldUpdateApiConfig() throws Exception {
        ApiConfigUpdateRequest request = new ApiConfigUpdateRequest();
        request.setApiName("Updated API");
        request.setRequestTimeoutMs(60000);

        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Updated API")
                .build();

        when(apiConfigApplicationService.updateApiConfig(any(Long.class), any(ApiConfigEntity.class))).thenReturn(entity);

        mockMvc.perform(put("/api/v1/api-configs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldEnableApiConfig() throws Exception {
        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .status(ApiConfigStatus.ENABLED)
                .build();

        when(apiConfigApplicationService.enableApiConfig(1L)).thenReturn(entity);

        mockMvc.perform(put("/api/v1/api-configs/1/enable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldDisableApiConfig() throws Exception {
        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .status(ApiConfigStatus.DISABLED)
                .build();

        when(apiConfigApplicationService.disableApiConfig(1L)).thenReturn(entity);

        mockMvc.perform(put("/api/v1/api-configs/1/disable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldDeleteApiConfig() throws Exception {
        mockMvc.perform(delete("/api/v1/api-configs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

}
