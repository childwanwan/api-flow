package com.apiflow.interfaces.biz.group;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.application.group.ApiGroupApplicationService;
import com.apiflow.application.group.dto.ApiGroupDTO;
import com.apiflow.application.group.param.*;
import com.apiflow.application.user.AuthService;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.result.PageResult;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.biz.group.converter.ApiGroupConverter;
import com.apiflow.interfaces.biz.group.request.ApiGroupCreateRequest;
import com.apiflow.interfaces.biz.group.request.ApiGroupListRequest;
import com.apiflow.interfaces.biz.group.request.ApiGroupPageRequest;
import com.apiflow.interfaces.biz.group.request.ApiGroupUpdateRequest;
import com.apiflow.interfaces.biz.group.vo.ApiGroupListVO;
import com.apiflow.interfaces.biz.group.vo.ApiGroupVO;
import com.apiflow.interfaces.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
public class ApiGroupController {

    private final AuthService authService;
    private final ApiGroupApplicationService apiGroupApplicationService;

    @PostMapping("/create")
    public Result<ApiGroupVO> createGroup(@RequestBody ApiGroupCreateRequest request, HttpServletRequest httpRequest) {
        validateApiGroupCreateRequest(request);
        String operator = authService.getUsernameByToken(TokenUtil.getTokenFromCookie(httpRequest));
        ApiGroupCreateParam param = ApiGroupConverter.INSTANCE.toCreateParam(request, operator);
        ApiGroupDTO group = apiGroupApplicationService.createGroup(param);
        return Result.success(ApiGroupConverter.INSTANCE.toVO(group));
    }


    @PutMapping("/update")
    public Result<ApiGroupVO> updateGroup(@RequestBody ApiGroupUpdateRequest request, HttpServletRequest httpRequest) {
        validateApiGroupUpdateRequest(request);
        String operator = authService.getUsernameByToken(TokenUtil.getTokenFromCookie(httpRequest));
        ApiGroupUpdateParam param = ApiGroupConverter.INSTANCE.toUpdateParam(request, operator);
        ApiGroupDTO group = apiGroupApplicationService.updateGroup(param);
        return Result.success(ApiGroupConverter.INSTANCE.toVO(group));
    }

    @GetMapping("/{groupNo}")
    public Result<ApiGroupVO> getGroup(@PathVariable String groupNo) {
        if (StrUtil.isBlank(groupNo)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        ApiGroupGetParam param = ApiGroupGetParam.builder().groupNo(groupNo).build();
        ApiGroupDTO group = apiGroupApplicationService.getGroup(param);
        return Result.success(ApiGroupConverter.INSTANCE.toVO(group));
    }

    @PostMapping("/page")
    public Result<PageResult<ApiGroupVO>> page(@RequestBody ApiGroupPageRequest request) {
        validateApiGroupPageRequest(request);
        ApiGroupPageParam pageParam = ApiGroupConverter.INSTANCE.apiGroupPageRequest2ApiGroupPageParam(request);
        PageResult<ApiGroupDTO> pageResult = apiGroupApplicationService.pageGroups(pageParam);
        return Result.success(ApiGroupConverter.INSTANCE.apiGroupDTOPage2VO(pageResult));
    }

    @GetMapping("/list")
    public Result<List<ApiGroupListVO>> list(ApiGroupListRequest request) {
        validateApiGroupListRequest(request);
        ApiGroupListParam listParam = ApiGroupConverter.INSTANCE.apiGroupListRequest2ApiGroupListParam(request);
        List<ApiGroupDTO> list = apiGroupApplicationService.listGroups(listParam);
        return Result.success(ApiGroupConverter.INSTANCE.apiGroupDTOList2VO(list));
    }


    @DeleteMapping("/{groupNo}")
    public Result<String> deleteGroup(@PathVariable String groupNo, HttpServletRequest httpRequest) {
        if (StrUtil.isBlank(groupNo)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        String operator = authService.getUsernameByToken(TokenUtil.getTokenFromCookie(httpRequest));
        ApiGroupDeleteParam param = ApiGroupDeleteParam.builder()
                .groupNo(groupNo)
                .operator(operator)
                .build();
        apiGroupApplicationService.deleteGroup(param);
        return Result.success();
    }

    private void validateApiGroupPageRequest(ApiGroupPageRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validateBasePageParam();
    }

    private void validateApiGroupListRequest(ApiGroupListRequest request) {
//        if (ObjectUtil.isEmpty(request)) {
//            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
//        }
    }

    private void validateApiGroupCreateRequest(ApiGroupCreateRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validate();
    }

    private void validateApiGroupUpdateRequest(ApiGroupUpdateRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validate();
    }

}