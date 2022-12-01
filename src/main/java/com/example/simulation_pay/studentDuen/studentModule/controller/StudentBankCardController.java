package com.example.simulation_pay.studentDuen.studentModule.controller;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.studentDuen.studentModule.entity.StudentBankCard;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentBankCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 银行卡表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Api(tags = "学生银行卡模块-控制器")
@RestController
@RequestMapping("api/studentModule/student-bank-card")
public class StudentBankCardController {

    @Autowired
    private IStudentBankCardService studentBankCardService;
    @Autowired
    private JwtConfig jwtConfig;

    @ApiOperation("添加银行卡")
    @PostMapping("saveBankCard")
    public CommonResult saveBankCard(HttpServletRequest request, @RequestBody StudentBankCard studentBankCard){
        // 获取存储在token的user_uuid
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        StudentBankCard one = studentBankCardService.getOne(new QueryWrapper<StudentBankCard>().eq("user_uuid", userUuid)
                .eq("bank_card",studentBankCard.getBankCard()).eq("is_untie", "1"));
        if (one != null){
            return CommonResult.failed("银行卡已添加，请不要重复添加");
        }
        boolean saveBankCard = false;
        if (studentBankCard.getBankCard() != null && !studentBankCard.getBankCard().equals("")){
            StudentBankCard stu = new StudentBankCard();
            stu.setUserUuid(userUuid);
            stu.setBankCard(studentBankCard.getBankCard());
            saveBankCard = studentBankCardService.save(stu);
        }
        if (!saveBankCard){
            return CommonResult.failed("银行卡添加失败");
        }
        return CommonResult.success(saveBankCard,"银行卡添加成功");
    }

    @ApiOperation("查询学生银行卡列表")
    @PostMapping("getBankCardList")
    public CommonResult getBankCardList(HttpServletRequest request){
        // 获取存储在token的user_uuid
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<StudentBankCard> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid",userUuid);
        wrapper.eq("is_untie","1");
        List<StudentBankCard> list = studentBankCardService.list(wrapper);
        if (CollUtil.isEmpty(list)){
            return CommonResult.failed("银行卡不存在");
        }
        return CommonResult.success(list,"查询成功");
    }

    @ApiOperation("解绑银行卡")
    @PostMapping("deleteBankCard")
    public CommonResult deleteBankCard(HttpServletRequest request,@RequestBody StudentBankCard studentBankCard){
        System.out.println("解绑银行卡:"+studentBankCard);
        // 获取存储在token的user_uuid
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        // 查询银行卡是否已存在
        StudentBankCard one = studentBankCardService.getOne(new QueryWrapper<StudentBankCard>()
                .eq("user_uuid",userUuid)
                .eq("bank_card", studentBankCard.getBankCard())
                .eq("is_untie", "1"));
        if (one == null){
            return CommonResult.failed("银行卡不存在");
        }
        // 修改条件
        UpdateWrapper<StudentBankCard> updateWrapper = new UpdateWrapper<>();
        if (userUuid != null && !userUuid.equals("")){
            updateWrapper.eq("user_uuid",userUuid);
        }
        if (studentBankCard.getBankCard() != null && !studentBankCard.getBankCard().equals("")){
            updateWrapper.eq("bank_card",studentBankCard.getBankCard());
        }
        // 修改内容
        StudentBankCard stu = new StudentBankCard();
        stu.setIsUntie("0");
        boolean update = studentBankCardService.update(stu, updateWrapper);
        if (!update){
            return CommonResult.failed("银行卡解绑失败");
        }
        return CommonResult.success(update,"银行卡解绑成功");
    }
}
