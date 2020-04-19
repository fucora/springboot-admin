package com.mm.modules.oss.controller;

import com.mm.common.exception.GException;
import com.mm.common.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 *
 * @author lwl
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {


    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new GException("上传文件不能为空");
        }

        return R.ok();
    }


}
