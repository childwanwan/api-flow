package com.apiflow.interfaces.annotation;

import com.apiflow.application.operationlog.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpLogInterceptor implements HandlerInterceptor {

    private final OperationLogService operationLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return;
        }

        OpLog opLog = handlerMethod.getMethodAnnotation(OpLog.class);
        if (opLog == null) {
            return;
        }

        String username = getUsername(request);
        String ip = getIp(request);
        String module = opLog.module();
        String operation = opLog.operation();
        String detail = opLog.detail();

        if (ex != null) {
            operation = operation + "_FAILED";
            detail = detail + ", error: " + ex.getMessage();
        }

        operationLogService.saveLog(username, operation, module, detail, ip);
    }

    private String getUsername(HttpServletRequest request) {
        Object username = request.getAttribute("username");
        return username != null ? username.toString() : "anonymous";
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
