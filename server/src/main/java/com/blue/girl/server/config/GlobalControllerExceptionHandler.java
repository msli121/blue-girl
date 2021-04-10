package com.blue.girl.server.config;

import com.blue.girl.server.dto.ApiResult;
import com.blue.girl.server.exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
@Log4j2
@ResponseBody
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void uploadExceptionHandler(MaxUploadSizeExceededException e, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("上传文件大小超出限制");
        out.flush();
        out.close();
    }


    @ExceptionHandler(value = BusinessException.class)
    public ApiResult sysBaseExceptionHandler(BusinessException e) {
        log.info("全局异常捕获 BusinessException");
        log.info(e);
        return ApiResult.F(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult globalDefaultExceptionHandler(Exception e) {
        String message = "系统异常";

        log.info("全局异常捕获 Exception");
        log.info(e);
        if (e instanceof IllegalArgumentException) {
            message = "参数错误";
        }

        if (e instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage();
        }

        return ApiResult.F(message);
    }
}
