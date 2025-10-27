package com.lostfound.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@ApiModel(description = "用户信息修改参数")
public class UserUpdateDTO {

    @ApiModelProperty(value = "用户唯一标识", required = true)
    @Positive(message = "用户ID必须为正整数") // 确保ID是有效的正整数
    private Long id;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "手机号码", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone; // 与实体类一致

    @ApiModelProperty(value = "性别：0-女，1-男")
    private String sex;

    @ApiModelProperty("用户头像URL")
    private String avatar;
}
