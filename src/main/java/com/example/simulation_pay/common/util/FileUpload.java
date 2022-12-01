package com.example.simulation_pay.common.util;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

public class FileUpload {

    public String UploadFile(MultipartFile file){
        String path="";
        try{
            String property = System.getProperty("user.dir");
            String img = property+"/upload/img/";//图片存放位置
            String attachment = property+"/upload/attachment";//附件存放位置
            File file1 = new File(attachment);
            if (!file1.exists()){
                file1.mkdir();
            }
            File file2 = new File(img);
            if (!file2.exists()){
                file2.mkdir();
            }
            String fileSuffix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            System.out.println(fileSuffix);
            if (fileSuffix.equalsIgnoreCase(".jpg")
                    ||fileSuffix.equalsIgnoreCase(".png")
                    ||fileSuffix.equalsIgnoreCase(".img")
                    ||fileSuffix.equalsIgnoreCase(".jpeg")){
                String uuid=MyUUid.MyUUIDCreate();
                long date=new Date().getTime();
                File fileDir = new File(img+fileSuffix.toUpperCase().substring(1)+"_"+uuid+"_"+date+fileSuffix);
                path="upload/img/"+fileSuffix.toUpperCase().substring(1)+"_"+uuid+"_"+date+fileSuffix;
                file.transferTo(fileDir);
            }else {
                String uuid=MyUUid.MyUUIDCreate();
                long date=new Date().getTime();
                File fileDir = new File(attachment+fileSuffix.toUpperCase().substring(1)+"_"+uuid+"_"+date+fileSuffix);
                path="upload/attachment/"+fileSuffix.toUpperCase().substring(1)+"_"+uuid+"_"+date+fileSuffix;
                file.transferTo(fileDir);
            }
        }catch(Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
        return path;
    }
}