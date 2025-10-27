package com.lostfound.service;

import com.lostfound.domain.dto.UserLoginDTO;
import com.lostfound.domain.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 失物招领小程序用户表 服务类
 * </p>
 *
 * @author clseven
 * @since 2025-10-13
 */
public interface IUserService extends IService<User> {

    User wxlogin(UserLoginDTO userLoginDTO);
}
