package com.apiflow.domain.plugin.builtin;

import com.apiflow.domain.config.model.ApiConfig;
import com.apiflow.domain.config.model.ExtraConfig;
import com.apiflow.domain.plugin.Plugin;
import com.apiflow.domain.plugin.PluginContext;
import com.apiflow.domain.plugin.PluginResult;
import com.apiflow.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BusinessExecutorPlugin implements Plugin {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    @Override
    public String getCode() {
        return "BUSINESS_EXECUTOR";
    }

    @Override
    public String getName() {
        return "业务执行插件";
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public PluginResult execute(PluginContext context) {
        context.putCompensateData("apiCode", context.getApiCode());
        context.putCompensateData("taskNo", context.getTaskNo());

        ExtraConfig extraConfig = context.getExtraConfig();
        if (extraConfig == null || extraConfig.getTargetUrl() == null || extraConfig.getTargetUrl().isEmpty()) {
            log.info("[BusinessExecutor] No target URL configured for apiCode={}, using mock execution", context.getApiCode());
            return PluginResult.success("Business logic executed (mock)", Map.of("result", "success", "mode", "mock"));
        }

        try {
            String url = resolveTemplate(extraConfig.getTargetUrl(), context);
            String method = extraConfig.getTargetMethod() != null ? extraConfig.getTargetMethod().toUpperCase() : "POST";
            String bodyTemplate = extraConfig.getTargetBodyTemplate();
            int timeoutMs = extraConfig.getTargetTimeoutMs() != null ? extraConfig.getTargetTimeoutMs() : 30000;

            log.info("[BusinessExecutor] Executing {} {} for taskNo={}, apiCode={}", method, url, context.getTaskNo(), context.getApiCode());

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofMillis(timeoutMs));

            if (extraConfig.getTargetHeaders() != null) {
                for (Map.Entry<String, String> entry : extraConfig.getTargetHeaders().entrySet()) {
                    String headerValue = resolveTemplate(entry.getValue(), context);
                    requestBuilder.header(entry.getKey(), headerValue);
                }
            }

            if (!requestHasContentType(extraConfig)) {
                requestBuilder.header("Content-Type", "application/json");
            }

            switch (method) {
                case "GET":
                    requestBuilder.GET();
                    break;
                case "DELETE":
                    requestBuilder.DELETE();
                    break;
                case "PUT":
                    String putBody = buildRequestBody(bodyTemplate, context);
                    requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(putBody));
                    break;
                case "PATCH":
                    String patchBody = buildRequestBody(bodyTemplate, context);
                    requestBuilder.method("PATCH", HttpRequest.BodyPublishers.ofString(patchBody));
                    break;
                case "POST":
                default:
                    String postBody = buildRequestBody(bodyTemplate, context);
                    requestBuilder.POST(HttpRequest.BodyPublishers.ofString(postBody));
                    break;
            }

            HttpRequest httpRequest = requestBuilder.build();
            long startTime = System.currentTimeMillis();
            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            long costTime = System.currentTimeMillis() - startTime;

            log.info("[BusinessExecutor] Response: status={}, costTime={}ms, taskNo={}", response.statusCode(), costTime, context.getTaskNo());

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("statusCode", response.statusCode());
            resultData.put("responseBody", response.body());
            resultData.put("costTimeMs", costTime);
            resultData.put("mode", "curl");

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return PluginResult.success("HTTP " + method + " executed successfully", resultData);
            } else {
                return PluginResult.fail("HTTP request failed with status: " + response.statusCode(), "HTTP_ERROR");
            }
        } catch (Exception e) {
            log.error("[BusinessExecutor] Failed to execute HTTP request for taskNo={}, apiCode={}", context.getTaskNo(), context.getApiCode(), e);
            return PluginResult.fail("HTTP request execution failed: " + e.getMessage(), "EXECUTION_ERROR");
        }
    }

    @Override
    public PluginResult compensate(PluginContext context) {
        log.info("[BusinessExecutor] Compensating for taskNo={}, apiCode={}", context.getTaskNo(), context.getApiCode());
        return PluginResult.success();
    }

    private boolean requestHasContentType(ExtraConfig extraConfig) {
        if (extraConfig.getTargetHeaders() == null) return false;
        for (String key : extraConfig.getTargetHeaders().keySet()) {
            if ("Content-Type".equalsIgnoreCase(key)) return true;
        }
        return false;
    }

    private String buildRequestBody(String bodyTemplate, PluginContext context) {
        if (bodyTemplate == null || bodyTemplate.isEmpty()) {
            if (context.getParams() != null && !context.getParams().isEmpty()) {
                return JsonUtil.toJson(context.getParams());
            }
            return "{}";
        }
        return resolveTemplate(bodyTemplate, context);
    }

    @SuppressWarnings("unchecked")
    private String resolveTemplate(String template, PluginContext context) {
        if (template == null) return null;
        String result = template;
        if (context.getParams() != null) {
            Map<String, Object> params = context.getParams();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String placeholder = "${params." + entry.getKey() + "}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                result = result.replace(placeholder, value);
            }
        }
        if (context.getCustomData() != null) {
            Map<String, Object> customData = context.getCustomData();
            for (Map.Entry<String, Object> entry : customData.entrySet()) {
                String placeholder = "${customData." + entry.getKey() + "}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                result = result.replace(placeholder, value);
            }
        }
        result = result.replace("${taskNo}", context.getTaskNo());
        result = result.replace("${apiCode}", context.getApiCode());
        result = result.replace("${apiName}", context.getApiName() != null ? context.getApiName() : "");
        return result;
    }
}
