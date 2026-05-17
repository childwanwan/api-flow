package com.apiflow.domain.receipt;

import java.util.Map;

public interface HttpReceiptGateway {

    int sendHttpRequest(String url, String method, Map<String, String> headers, Map<String, Object> body);
}
