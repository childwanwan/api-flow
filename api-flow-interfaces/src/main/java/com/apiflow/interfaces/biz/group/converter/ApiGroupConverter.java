package com.apiflow.interfaces.biz.group.converter;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.application.group.dto.ApiGroupDTO;
import com.apiflow.application.group.param.CreateApiGroupParam;
import com.apiflow.application.group.param.ListApiGroupParam;
import com.apiflow.application.group.param.PageApiGroupParam;
import com.apiflow.application.group.param.UpdateApiGroupParam;
import com.apiflow.common.result.PageResult;
import com.apiflow.interfaces.biz.group.request.ApiGroupCreateRequest;
import com.apiflow.interfaces.biz.group.request.ApiGroupListRequest;
import com.apiflow.interfaces.biz.group.request.ApiGroupPageRequest;
import com.apiflow.interfaces.biz.group.request.ApiGroupUpdateRequest;
import com.apiflow.interfaces.biz.group.vo.ApiGroupListVO;
import com.apiflow.interfaces.biz.group.vo.ApiGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ApiGroupConverter {
    
    ApiGroupConverter INSTANCE = Mappers.getMapper(ApiGroupConverter.class);

    ApiGroupVO toVO(ApiGroupDTO dto);

    PageApiGroupParam apiGroupPageRequest2ApiGroupPageParam(ApiGroupPageRequest request);

    default PageResult<ApiGroupVO> apiGroupDTOPage2VO(PageResult<ApiGroupDTO> pageResult) {
        if (pageResult == null) {
            return null;
        }
        List<ApiGroupVO> voList = pageResult.getRecords().stream()
                .map(this::toVO)
                .toList();
        return PageResult.of(voList, pageResult.getTotal(), pageResult.getCurrent(), pageResult.getSize());
    }

    ListApiGroupParam apiGroupListRequest2ApiGroupListParam(ApiGroupListRequest request);

    ApiGroupListVO toListVO(ApiGroupDTO dto);

    List<ApiGroupListVO> apiGroupDTOList2VO(List<ApiGroupDTO> list);

    @Mapping(source = "request.groupCode", target = "groupCode")
    @Mapping(source = "request.groupName", target = "groupName")
    @Mapping(source = "request.groupDescription", target = "groupDescription")
    @Mapping(source = "operator", target = "operator")
    CreateApiGroupParam toCreateParam(ApiGroupCreateRequest request, String operator);

    @Mapping(source = "request.groupNo", target = "groupNo")
    @Mapping(source = "request.groupCode", target = "groupCode")
    @Mapping(source = "request.groupName", target = "groupName")
    @Mapping(source = "request.groupDescription", target = "groupDescription")
    @Mapping(source = "operator", target = "operator")
    UpdateApiGroupParam toUpdateParam(ApiGroupUpdateRequest request, String operator);
}
