package com.apigateway.domain.config.repository;

import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.query.ApiConfigQuery;

import java.util.List;

public interface ApiConfigRepository {

    ApiConfigEntity save(ApiConfigEntity config);

    ApiConfigEntity update(ApiConfigEntity config);

    ApiConfigEntity query(ApiConfigQuery query);

    List<ApiConfigEntity> queryList(ApiConfigQuery query);

    boolean exists(ApiConfigQuery query);

}
