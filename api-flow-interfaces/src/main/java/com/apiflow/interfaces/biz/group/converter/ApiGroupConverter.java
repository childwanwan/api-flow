package com.apiflow.interfaces.biz.group.converter;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.application.group.dto.ApiGroupDTO;
import com.apiflow.application.group.param.ApiGroupCreateParam;
import com.apiflow.application.group.param.ApiGroupPageParam;
import com.apiflow.application.group.param.ApiGroupUpdateParam;
import com.apiflow.common.result.PageResult;
import com.apiflow.interfaces.biz.group.request.ApiGroupCreateRequest;
import com.apiflow.interfaces.biz.group.request.ApiGroupPageRequest;
import com.apiflow.interfaces.biz.group.request.ApiGroupUpdateRequest;
import com.apiflow.interfaces.biz.group.vo.ApiGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApiGroupConverter {
    
    ApiGroupConverter INSTANCE = Mappers.getMapper(ApiGroupConverter.class);

    ApiGroupVO toVO(ApiGroupDTO dto);

    ApiGroupPageParam apiGroupPageRequest2ApiGroupPageParam(ApiGroupPageRequest request);

    PageResult<ApiGroupVO> apiGroupDTOPage2VO(PageResult<ApiGroupDTO> pageResult);

    @Mapping(source = "request.groupCode", target = "groupCode")
    @Mapping(source = "request.groupName", target = "groupName")
    @Mapping(source = "request.groupDescription", target = "groupDescription")
    @Mapping(source = "operator", target = "operator")
    ApiGroupCreateParam toCreateParam(ApiGroupCreateRequest request, String operator);

    @Mapping(source = "request.groupNo", target = "groupNo")
    @Mapping(source = "request.groupCode", target = "groupCode")
    @Mapping(source = "request.groupName", target = "groupName")
    @Mapping(source = "request.groupDescription", target = "groupDescription")
    @Mapping(source = "operator", target = "operator")
    ApiGroupUpdateParam toUpdateParam(ApiGroupUpdateRequest request, String operator);
}
