package com.hjj.xiantao.controller;

import com.hjj.xiantao.common.BaseResponse;
import com.hjj.xiantao.common.ResultUtils;
import com.hjj.xiantao.manager.AliOSSManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private AliOSSManager aliOSSManager;

    @PostMapping("/image")
    public BaseResponse<String> uploadImage(MultipartFile multipartFile) throws IOException {
        String imageUrl = aliOSSManager.upload(multipartFile);
        System.out.println(imageUrl);
        return ResultUtils.success(imageUrl);
    }

}
