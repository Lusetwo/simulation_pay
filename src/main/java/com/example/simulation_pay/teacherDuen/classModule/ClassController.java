package com.example.simulation_pay.teacherDuen.classModule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.manageDuen.adminModule.entity.TeacherClass;
import com.example.simulation_pay.manageDuen.adminModule.mapper.TeacherClassMapper;
import com.example.simulation_pay.manageDuen.adminModule.service.IClassesService;
import com.example.simulation_pay.manageDuen.adminModule.service.ITeacherClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "班级管理模块-控制器")
@RestController
@RequestMapping("api/ClassModule/classes")
public class ClassController {

    @Autowired
    private IClassesService classesService;

    @Autowired
    private ITeacherClassService teacherClassService;

    @Autowired
    private TeacherClassMapper teacherClassMapper;

    @Autowired
    private JwtConfig jwtConfig;

    @ApiOperation("查询我管理的班级")
    @PostMapping("getMyClassList")
    public CommonResult GetMyClass(HttpServletRequest request)   {
        // token中有当前登录用户的user_uuid
        String uuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null) {
            return CommonResult.failed("您还没有登录，请先登录");
        }
        List<Classes> classesList = teacherClassMapper.getClassesByTeacherUuid(uuid);
        if (classesList.isEmpty()){
            return CommonResult.failed("未查询到班级");
        }
        return CommonResult.success(classesList,"查询到以下班级");
    }
}
