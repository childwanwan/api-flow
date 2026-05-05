package com.apiflow.interfaces.biz.group;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.application.group.ApiGroupApplicationService;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
public class ApiGroupController {

    private final ApiGroupApplicationService apiGroupApplicationService;
    private final com.apiflow.application.user.AuthService authService;

    @PostMapping("/create")
    public Result<ApiGroupIDTO> createGroup(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        String groupNo = (String) request.get("groupNo");
        String groupName = (String) request.get("groupName");
        String groupDescription = (String) request.get("groupDescription");
        String operator = authService.getUsernameByToken(TokenUtil.getTokenFromCookie(httpRequest));
        ApiGroupIDTO group = apiGroupApplicationService.createGroup(groupNo, groupName, groupDescription, operator);
        return Result.success(group);
    }

    @PutMapping("/update")
    public Result<ApiGroupIDTO> updateGroup(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        String groupNo = (String) request.get("groupNo");
        String groupName = (String) request.get("groupName");
        String groupDescription = (String) request.get("groupDescription");
        String operator = authService.getUsernameByToken(TokenUtil.getTokenFromCookie(httpRequest));
        ApiGroupIDTO group = apiGroupApplicationService.updateGroup(groupNo, groupName, groupDescription, operator);
        return Result.success(group);
    }

    @GetMapping("/{groupNo}")
    public Result<ApiGroupIDTO> getGroup(@PathVariable String groupNo) {
        ApiGroupIDTO group = apiGroupApplicationService.getGroup(groupNo);
        return Result.success(group);
    }

    @GetMapping("/list")
    public Result<List<ApiGroupIDTO>> listGroups(@RequestParam(required = false) String groupNo,
                                                  @RequestParam(required = false) String groupName) {
        List<ApiGroupIDTO> groups = apiGroupApplicationService.listGroups(groupNo, groupName);
        return Result.success(groups);
    }

    @DeleteMapping("/{groupNo}")
    public Result<String> deleteGroup(@PathVariable String groupNo, HttpServletRequest httpRequest) {
        String operator = authService.getUsernameByToken(TokenUtil.getTokenFromCookie(httpRequest));
        apiGroupApplicationService.deleteGroup(groupNo, operator);
        return Result.success("删除成功");
    }
}
