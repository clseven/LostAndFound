package com.lostfound.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserInfoVO {

    private Long id;
    private String name;
    private String phone;
    private String sex;
    private String avatar;
    private LocalDateTime createTime;
}
