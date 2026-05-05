package com.apiflow.api.repository.plugin.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PluginConfigField implements FieldMetadata {
    ID("id", "id", null),
    PLUGIN_CODE("pluginCode", "plugin_code", null),
    PLUGIN_NAME("pluginName", "plugin_name", null),
    PLUGIN_CLASS("pluginClass", "plugin_class", null),
    DESCRIPTION("description", "description", null),
    CONFIG("config", "config", null),
    ENABLED("enabled", "enabled", null),
    ORDER_NUM("orderNum", "order_num", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null),
    UPDATE_TIME_MS("updateTimeMs", "update_time_ms", null),
    DELETED("deleted", "deleted", null),
    VERSION("version", "version", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
