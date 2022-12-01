package com.example.simulation_pay.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class OssUploadUtil {
    //阿里域名
    public static final String ALI_DOMAIN = "https://qrcode-1121.oss-cn-guangzhou.aliyuncs.com/";
    public static String uploadImage(MultipartFile file) throws IOException {
        //生成文件名
        String originalFilename = file.getOriginalFilename();
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + ext;
        //地域节点
        String endpoint = "http://oss-cn-guangzhou.aliyuncs.com";
        String accessKeyId = "LTAI4GEe8HFL2hiv4nC3dAyA";
        String accesskeySecret = "rr8BD42k4HRBZ2oK1EZVd6oGNjAVOf";
        //oss客户端对象
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accesskeySecret);
        ossClient.putObject(
                "qrcode-1121",//仓库名
                fileName,
                file.getInputStream()
        );
        ossClient.shutdown();
        return ALI_DOMAIN + fileName;
    }


}
