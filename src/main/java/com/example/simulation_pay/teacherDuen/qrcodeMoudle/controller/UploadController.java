package com.example.simulation_pay.teacherDuen.qrcodeMoudle.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.common.util.OssUploadUtil;
import com.example.simulation_pay.common.util.QRcodeIdUtil;
import com.example.simulation_pay.teacherDuen.qrcodeMoudle.entity.Qrcode;
import com.example.simulation_pay.teacherDuen.qrcodeMoudle.mapper.QrcodeMapper;
import com.example.simulation_pay.teacherDuen.qrcodeMoudle.service.IQrcodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "扫描二维码")
@RestController
@RequestMapping("/api/QRcodeMoudle/upload")
public class UploadController {
    @Autowired
    private IQrcodeService qrcodeService;
    private QrcodeMapper qrcodeMapper;

    @ApiOperation("图片上传")
    @PostMapping("/uploadImg")
    public CommonResult uploadImg(@RequestParam MultipartFile file,String fileName) throws IOException{
        System.out.println(fileName);
        if (file.isEmpty()){
            return CommonResult.failed("上传失败");
        }
        String path = OssUploadUtil.uploadImage(file);
        System.out.println(path);
        Qrcode qrCode = new Qrcode();
        qrCode.setPicUrl(path);
        qrCode.setPicName(fileName);
        qrCode.setPicCode(QRcodeIdUtil.QrcodeRandom());
        qrcodeService.save(qrCode);
        return CommonResult.success(path,"上传成功");
    }

    @ApiOperation("查询图片")
    @PostMapping("/getQrcodeList")
    public CommonResult getQrcodeList(){
        List<Qrcode> qrcodes = qrcodeService.list();
        System.out.println(qrcodes);
        if (qrcodes==null){
            return CommonResult.failed("数据为空");
        }
        return CommonResult.success(qrcodes,"查询成功");
    }

    @ApiOperation("条件查询")
    @PostMapping("/getOneQrcode")
    public CommonResult getOneQrcode(HttpServletRequest request, @Param("picCode") Integer picCode){
        QueryWrapper wrapper = new QueryWrapper();
        if (picCode!=null){
            wrapper.like("pic_code",picCode);
        }
        List<Qrcode> list = qrcodeService.list(wrapper);
        return CommonResult.success(list,"查询到以下记录");
    }

    @ApiOperation("修改数据")
    @PostMapping("/updateQrcode")
    public CommonResult updateQrcode(@RequestBody Qrcode qrcode){
        System.out.println(qrcode);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("pic_code",qrcode.getPicCode());
        Qrcode qrcode1 = qrcodeService.getOne(wrapper);
        if (qrcode1==null){
            return CommonResult.failed("找不到此条记录");
        }
        qrcode1.setPicName(qrcode.getPicName());
        qrcode1.setPicUrl(qrcode.getPicUrl());
        qrcodeService.update(qrcode1,wrapper);
        return CommonResult.success("更新成功");
    }

    @ApiOperation("条件删除")
    @PostMapping("/deleteQrcodeById")
    public CommonResult deleteQrcodeById(@Param("picCode")  Integer picCode){
        System.out.println(picCode);
        if (picCode==null){
            return CommonResult.failed();
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("pic_code",picCode);
        qrcodeService.remove(wrapper);
        return CommonResult.success("删除成功");
    }

    @ApiOperation("批量删除")
    @PostMapping("/deleteQrcodeByIds")
    public CommonResult deleteQrcodeByIds(@RequestParam(value = "picIds", required = true) List<Integer> picIds){
        List<Integer> ids = new ArrayList<>();
        for (int i=0;i<picIds.size();i++){
            ids.add(picIds.get(i));
        }
        qrcodeService.removeByIds(ids);
        return CommonResult.success("删除成功");
    }
}
