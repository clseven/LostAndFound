package com.lostfound.controller;


import com.lostfound.common.properties.JwtProperties;
import com.lostfound.common.result.Result;
import com.lostfound.common.utils.JwtUtil;
import com.lostfound.domain.dto.UserLoginDTO;
import com.lostfound.domain.dto.UserUpdateDTO;
import com.lostfound.domain.po.User;
import com.lostfound.domain.vo.UserLoginVO;
import com.lostfound.domain.vo.UserUpdateVO;
import com.lostfound.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 失物招领小程序用户表 前端控制器
 * </p>
 *
 * @author clseven
 * @since 2025-10-13
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户信息管理接口")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/wxlogin")
    @ApiOperation("用户登录")
    public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO) {

        log.info("微信用户登录：{}",userLoginDTO);
        //微信登录
        User user = userService.wxlogin(userLoginDTO);

        //为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        log.info("用户{}",userLoginVO);
        return Result.success(userLoginVO);
    }

    @PostMapping("/update")
    @ApiOperation("用户信息修改")
    public Result<UserUpdateVO> updateUserInfo(@Validated @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("用户信息修改：{}", userUpdateDTO);
        UserUpdateVO userUpdateVO = userService.updateUserInfo(userUpdateDTO);
        return Result.success(userUpdateVO);
    }

}
