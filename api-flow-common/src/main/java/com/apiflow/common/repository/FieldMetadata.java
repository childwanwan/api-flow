package com.apiflow.common.repository;

public interface FieldMetadata {

    String getFieldName();

    String getColumnName();

    String getJsonPath();

    boolean isJsonField();
}
