package com.apigateway.common.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtil() {
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JacksonException e) {
            log.error("对象转JSON失败", e);
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JacksonException e) {
            log.error("JSON转对象失败", e);
            throw new RuntimeException("JSON转对象失败", e);
        }
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JacksonException e) {
            log.error("JSON转对象失败", e);
            throw new RuntimeException("JSON转对象失败", e);
        }
    }

}