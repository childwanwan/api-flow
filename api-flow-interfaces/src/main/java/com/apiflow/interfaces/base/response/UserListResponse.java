package com.apiflow.interfaces.base.response;

import com.apiflow.interfaces.base.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    private List<UserVO> list;
    private Long total;
}
