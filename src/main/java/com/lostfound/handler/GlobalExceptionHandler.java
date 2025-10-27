package com.lostfound.handler;

import com.lostfound.common.result.Result;
import com.lostfound.common.exception.BaseException;
import com.lostfound.common.exception.LoginFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常（自定义异常）
     * @param ex 业务异常对象
     * @return 统一响应结果
     */
    @ExceptionHandler(BaseException.class)
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理登录失败异常
     * 单独处理登录相关异常，可返回特定状态码
     */
    @ExceptionHandler(LoginFailedException.class)
    public Result handleLoginFailedException(LoginFailedException ex) {
        log.error("登录失败：{}", ex.getMessage());
        return Result.error(ex.getMessage()); // 401表示未授权
    }

    /**
     * 处理SQL完整性约束异常（如唯一键冲突）
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //Duplicate entry 'zs' for key 'employee.idx_username'
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + "已存在";
            return Result.error(msg);
        }else{
            return Result.error("数据库操作异常");
        }
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        log.error("未知异常：", ex); // 打印完整堆栈信息，便于排查
        return Result.error("系统繁忙，请稍后再试");
    }
}