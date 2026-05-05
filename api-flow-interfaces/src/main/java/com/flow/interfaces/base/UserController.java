package com.flow.interfaces.base;

import com.flow.api.repository.user.UserRepository;
import com.flow.api.repository.user.dto.UserIDTO;
import com.flow.api.repository.user.param.InsertUserParam;
import com.flow.api.repository.user.param.SelectUserParam;
import com.flow.api.repository.user.param.UpdateUserParam;
import com.flow.application.auth.AuthService;
import com.flow.common.result.Result;
import com.flow.interfaces.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @GetMapping
    public String userPage(Model model, HttpServletRequest request) {
        String token = TokenUtil.getTokenFromCookie(request);
        if (token == null || !authService.isValidToken(token)) {
            return "redirect:/login";
        }
        UserIDTO user = authService.getUserByToken(token);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        
        List<UserIDTO> userList = userRepository.selectList(null);
        model.addAttribute("userList", userList);
        return "user";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public Result<Map<String, Object>> getUserList(@RequestParam(required = false) String username,
                                                   @RequestParam(required = false) String role,
                                                   @RequestParam(required = false) String status) {
        SelectUserParam param = SelectUserParam.builder()
                .username(StringUtils.isBlank(username) ? null : username)
                .role(StringUtils.isBlank(role) ? null : role)
                .status(StringUtils.isBlank(status) ? null : status)
                .build();
        List<UserIDTO> userList = userRepository.selectList(param);
        long total = userRepository.count(param);
        
        Map<String, Object> data = new HashMap<>();
        data.put("list", userList);
        data.put("total", total);
        return Result.success(data);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Result<UserIDTO> getUser(@PathVariable Long id) {
        SelectUserParam param = SelectUserParam.builder().build();
        List<UserIDTO> list = userRepository.selectList(param);
        UserIDTO user = list.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        if (user == null) {
            return Result.fail("USER001", "用户不存在", null);
        }
        return Result.success(user);
    }

    @PostMapping("/api/create")
    @ResponseBody
    public Result<String> createUser(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");
        String role = body.get("role");
        
        if (StringUtils.isBlank(username)) {
            return Result.fail("USER002", "用户名不能为空", null);
        }
        if (StringUtils.isBlank(password)) {
            return Result.fail("USER003", "密码不能为空", null);
        }
        
        SelectUserParam checkParam = SelectUserParam.builder().username(username).build();
        if (userRepository.count(checkParam) > 0) {
            return Result.fail("USER004", "用户名已存在", null);
        }
        
        String operator = authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request));
        InsertUserParam insertParam = InsertUserParam.builder()
                .username(username)
                .password(password)
                .role(StringUtils.defaultIfBlank(role, "USER"))
                .status("ENABLED")
                .createOperator(operator)
                .build();
        
        int result = userRepository.insert(insertParam);
        if (result > 0) {
            return Result.success("创建成功");
        }
        return Result.fail("USER005", "创建失败", null);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Result<String> updateUser(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String password = body.get("password");
        String role = body.get("role");
        String status = body.get("status");
        
        UpdateUserParam updateParam = UpdateUserParam.builder()
                .id(id)
                .password(StringUtils.isBlank(password) ? null : password)
                .role(StringUtils.isBlank(role) ? null : role)
                .status(StringUtils.isBlank(status) ? null : status)
                .build();
        
        int result = userRepository.update(updateParam);
        if (result > 0) {
            return Result.success("更新成功");
        }
        return Result.fail("USER006", "更新失败", null);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public Result<String> deleteUser(@PathVariable Long id) {
        int result = userRepository.delete(id);
        if (result > 0) {
            return Result.success("删除成功");
        }
        return Result.fail("USER007", "删除失败", null);
    }

}