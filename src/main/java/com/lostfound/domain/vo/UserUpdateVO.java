package com.lostfound.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "用户信息修改响应")
public class UserUpdateVO {

    @ApiModelProperty("用户唯一标识")
    private Long id;

    @ApiModelProperty("用户姓名")
    private String name; // 对应实体类name

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("性别：0-女，1-男")
    private String sex; // 保持String类型

    @ApiModelProperty("用户头像URL")
    private String avatar;

}
