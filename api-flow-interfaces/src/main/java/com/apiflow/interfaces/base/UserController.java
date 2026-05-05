package com.apiflow.interfaces.base;

import com.apiflow.api.repository.user.idto.UserIDTO;
import com.apiflow.api.repository.user.param.InsertUserParam;
import com.apiflow.api.repository.user.param.SelectUserParam;
import com.apiflow.api.repository.user.param.UpdateUserParam;
import com.apiflow.application.user.AuthService;
import com.apiflow.application.user.UserService;
import com.apiflow.common.enums.EnableStatus;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.result.Result;
import com.apiflow.common.util.MD5Util;
import com.apiflow.interfaces.base.request.UserCreateRequest;
import com.apiflow.interfaces.base.request.UserUpdateRequest;
import com.apiflow.interfaces.base.response.UserListResponse;
import com.apiflow.interfaces.base.vo.UserVO;
import com.apiflow.interfaces.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    private UserVO toUserVO(UserIDTO dto) {
        if (dto == null) return null;
        return UserVO.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .role(dto.getRole())
                .status(dto.getStatus())
                .createTimeMs(dto.getCreateTimeMs())
                .updateTimeMs(dto.getUpdateTimeMs())
                .createOperator(dto.getCreateOperator())
                .lastLoginTimeMs(dto.getLastLoginTimeMs())
                .build();
    }

    @GetMapping
    public String userPage(Model model, HttpServletRequest request) {
        model.addAttribute("username", request.getAttribute("username"));
        model.addAttribute("role", request.getAttribute("role"));

        List<UserIDTO> userList = userService.getUserList(null);
        model.addAttribute("userList", userList);
        return "user";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public Result<UserListResponse> getUserList(@RequestParam(required = false) String username,
                                                @RequestParam(required = false) String role,
                                                @RequestParam(required = false) String status) {
        SelectUserParam param = SelectUserParam.builder()
                .username(StringUtils.isBlank(username) ? null : FieldCondition.of(username))
                .role(StringUtils.isBlank(role) ? null : FieldCondition.of(role))
                .status(StringUtils.isBlank(status) ? null : FieldCondition.of(status))
                .build();
        List<UserIDTO> userList = userService.getUserList(param);
        long total = userService.count(param);

        UserListResponse response = UserListResponse.builder()
                .list(userList.stream().map(this::toUserVO).toList())
                .total(total)
                .build();
        return Result.success(response);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Result<UserVO> getUser(@PathVariable Long id) {
        UserIDTO user = userService.getUserById(id);
        if (user == null) {
            return Result.fail(ErrorCode.USER_NOT_FOUND);
        }
        return Result.success(toUserVO(user));
    }

    @PostMapping("/api/create")
    @ResponseBody
    public Result<String> createUser(@RequestBody UserCreateRequest request, HttpServletRequest httpRequest) {
        String username = request.getUsername();
        String password = request.getPassword();
        String role = request.getRole();

        if (StringUtils.isBlank(username)) {
            return Result.fail(ErrorCode.USER_USERNAME_EMPTY);
        }
        if (StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.USER_PASSWORD_EMPTY);
        }

        if (userService.existsByUsername(username)) {
            return Result.fail(ErrorCode.USER_USERNAME_EXISTS);
        }

        String operator = authService.getUsernameByToken(TokenUtil.getTokenFromCookie(httpRequest));
        InsertUserParam insertParam = InsertUserParam.builder()
                .username(username)
                .password(MD5Util.encode(password))
                .role(StringUtils.defaultIfBlank(role, "USER"))
                .status(EnableStatus.ENABLED.getValue())
                .createOperator(operator)
                .build();

        if (userService.createUser(insertParam)) {
            return Result.success("创建成功");
        }
        return Result.fail(ErrorCode.USER_CREATE_FAILED);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Result<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        String password = request.getPassword();
        String role = request.getRole();
        String status = request.getStatus();

        UpdateUserParam updateParam = UpdateUserParam.builder()
                .id(id)
                .password(StringUtils.isBlank(password) ? null : password)
                .role(StringUtils.isBlank(role) ? null : role)
                .status(StringUtils.isBlank(status) ? null : status)
                .build();

        if (userService.updateUser(updateParam)) {
            return Result.success("更新成功");
        }
        return Result.fail(ErrorCode.USER_UPDATE_FAILED);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public Result<String> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return Result.success("删除成功");
        }
        return Result.fail(ErrorCode.USER_DELETE_FAILED);
    }

}
