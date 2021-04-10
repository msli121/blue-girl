package com.blue.girl.server.controller;

import com.blue.girl.server.dto.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * 下载服务器本地的文件
     */
    @GetMapping(value = "/hello")
    public ApiResult downloadLocalServerFile(@PathVariable Integer fileId, HttpServletResponse response) throws IOException {
        return ApiResult.T("hello world");
    }
}
