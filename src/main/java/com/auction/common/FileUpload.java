package com.auction.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.UUID;

@RestController
public class FileUpload implements WebMvcConfigurer {

    @PostMapping("/sys/upload")
    @ResponseBody
    public JSONObject upload(@RequestParam("file") MultipartFile file) {
        String path = System.getProperty("user.dir") + "/src/main/resources/static";//user.dir项目的根目录
        String fileName = file.getOriginalFilename();//获取文件名称
        String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
        fileName = UUID.randomUUID().toString().replace("-", "")+ suffixName;//重新生成文件名
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            // 判断文件夹是否未空，空则创建
            targetFile.mkdirs();
        }
        File saveFile = new File(targetFile, fileName);
        JSONObject jo = new JSONObject();
        try {
            //指定本地存入路径
            file.transferTo(saveFile);
            String path1 = path + fileName;
            jo.put("code", "200");
            jo.put("msg", "添加成功");
            jo.put("data", fileName);
            System.out.println(path1);
        } catch (Exception e) {
            e.printStackTrace();
            jo.put("code", "500");
            jo.put("msg", e.getMessage());
        }
        return jo;
    }
}

