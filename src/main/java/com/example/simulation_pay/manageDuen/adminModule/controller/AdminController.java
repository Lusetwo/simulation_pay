package com.example.simulation_pay.manageDuen.adminModule.controller;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.Admin;
import com.example.simulation_pay.manageDuen.adminModule.service.IAdminService;
import com.example.simulation_pay.teacherDuen.teacherModule.entity.Teacher;
import com.example.simulation_pay.teacherDuen.teacherModule.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Api(tags = "管理模块-控制器")
@RestController
@RequestMapping("api/adminModule/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private JwtConfig jwtConfig;

    @ApiOperation("登录")
    @PostMapping("adminLogin")
    public CommonResult adminLogin(@RequestBody Admin admin) {
        System.err.println("管理员登录");
        if (admin.getAdminName()==null||admin.getAdminPassword()==null){
            return CommonResult.failed("登录信息缺失");
        }
            String username=admin.getAdminName();
            String password=admin.getAdminPassword();
            JSONObject jsonObject = new JSONObject();
            Admin adminUser = adminService.getOne(new QueryWrapper<Admin>().eq("admin_name", username).eq("admin_password", password));
            if (adminUser != null) {
                String token = jwtConfig.createToken(adminUser.getUserUuid());
                if (!StringUtils.isEmpty(token)) {
                    jsonObject.set("token", token);
                }
                return CommonResult.success(jsonObject, "登录成功");
            }
            Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_name", username).eq("teacher_password", password).eq("del_flag",1));
            if (teacher != null) {
                String token = jwtConfig.createToken(teacher.getUserUuid());
                if (!StringUtils.isEmpty(token)) {
                    jsonObject.set("token", token);
                }
                return CommonResult.success(jsonObject, "登录成功");
            }
            return CommonResult.failed("未查询到账户信息");
    }

}
