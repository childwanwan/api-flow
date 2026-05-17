package com.apiflow.domain.config.event;

import com.apiflow.domain.shared.event.DomainEvent;
import lombok.Getter;

@Getter
public class ApiConfigDomainEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private static final String AGGREGATE_TYPE = "API_CONFIG";

    private final String apiCode;
    private final String apiName;
    private final String operator;

    private ApiConfigDomainEvent(String eventType, String apiCode, String apiName, String operator) {
        super(eventType, AGGREGATE_TYPE, apiCode);
        this.apiCode = apiCode;
        this.apiName = apiName;
        this.operator = operator;
    }

    public static class Created extends ApiConfigDomainEvent {

        private static final long serialVersionUID = 1L;

        public Created(String apiCode, String apiName, String operator) {
            super("API_CONFIG_CREATED", apiCode, apiName, operator);
        }
    }

    public static class Updated extends ApiConfigDomainEvent {

        private static final long serialVersionUID = 1L;

        public Updated(String apiCode, String apiName, String operator) {
            super("API_CONFIG_UPDATED", apiCode, apiName, operator);
        }
    }

    public static class Deleted extends ApiConfigDomainEvent {

        private static final long serialVersionUID = 1L;

        public Deleted(String apiCode, String operator) {
            super("API_CONFIG_DELETED", apiCode, null, operator);
        }
    }

    public static class Enabled extends ApiConfigDomainEvent {

        private static final long serialVersionUID = 1L;

        public Enabled(String apiCode, String operator) {
            super("API_CONFIG_ENABLED", apiCode, null, operator);
        }
    }

    public static class Disabled extends ApiConfigDomainEvent {

        private static final long serialVersionUID = 1L;

        public Disabled(String apiCode, String operator) {
            super("API_CONFIG_DISABLED", apiCode, null, operator);
        }
    }
}
