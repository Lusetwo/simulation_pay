package com.example.simulation_pay.teacherDuen.teacherModule.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.common.util.MyUUid;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.manageDuen.adminModule.entity.TeacherClass;
import com.example.simulation_pay.manageDuen.adminModule.mapper.TeacherClassMapper;
import com.example.simulation_pay.manageDuen.adminModule.service.IClassesService;
import com.example.simulation_pay.manageDuen.adminModule.service.ITeacherClassService;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.teacherDuen.teacherModule.entity.Teacher;
import com.example.simulation_pay.teacherDuen.teacherModule.service.ITeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 教师表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@RestController
@RequestMapping("api/teacherModule/teacher")
public class TeacherController {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private ITeacherClassService teacherClassService;

    @Autowired
    private IClassesService classesService;

    @Autowired
    private TeacherClassMapper teacherClassMapper;

    @ApiOperation("注册")
    @PostMapping("saveTeacherUser")
    public CommonResult saveTeacherUser(@RequestBody Teacher teacher){
        Teacher teacher1= teacherService.getOne(new QueryWrapper<Teacher>()
                .eq("teacher_phone",teacher.getTeacherPhone()).eq("del_flag","1"));
        if (teacher1!=null&&!teacher.equals("")){
            return CommonResult.failed("用户已存在");
        }
        Teacher tea = new Teacher();
        tea.setTeacherName(teacher.getTeacherName());
        tea.setTeacherPassword(teacher.getTeacherPassword());
        tea.setTeacherPhone(teacher.getTeacherPhone());
        tea.setCreateTime(DateUtil.date());
        tea.setUserUuid(MyUUid.MyUUIDCreate());
        boolean save = teacherService.save(tea);
        if (!save){
            return CommonResult.failed("注册失败");
        }
        return CommonResult.success(save,"注册成功");
    }

    @ApiOperation("登录")
    @PostMapping("teacherLogin")
    public CommonResult teacherLogin(@RequestBody Teacher teacher){
        JSONObject jsonObject = new JSONObject();
        Teacher stu = null;
        if (!StrUtil.hasEmpty(teacher.getTeacherName()) && !StrUtil.hasEmpty(teacher.getTeacherName())){
            stu = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_name",teacher.getTeacherName())
                    .eq("teacher_password",teacher.getTeacherPassword()).eq("del_flag","1"));
        }
        if (stu == null){
            return CommonResult.failed("用户不存在或密码错误");
        }
        // 创建token，将用户UUID添加到token中
        String token = jwtConfig.createToken(stu.getUserUuid());
        if (!StringUtils.isEmpty(token)){
            // 将创建的token添加到json
            jsonObject.set("token",token);
        }
        return CommonResult.success(jsonObject,"登录成功");
    }

    @ApiOperation("手机号登录")
    @PostMapping("TeacherPhoneLogin")
    public CommonResult TeacherPhoneLogin(@RequestBody Teacher teacher){
        JSONObject jsonObject = new JSONObject();
        Teacher teacher1 = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_phone",teacher.getTeacherPhone()).eq("del_flag","1"));
        if (teacher1==null){
            return CommonResult.failed("用户不存在");
        }
        // 创建token，将用户UUID添加到token中
        String token = jwtConfig.createToken(teacher1.getUserUuid());
        if (!StringUtils.isEmpty(token)){
            // 将创建的token添加到json
            jsonObject.set("token",token);
        }
        return CommonResult.success(jsonObject,"登录成功");
    }

    @ApiOperation("忘记密码")
    @PostMapping("updateTeacherPassword")
    public CommonResult updateTeacherPassword(@RequestBody Teacher teacher){

        if (StrUtil.hasEmpty(teacher.getTeacherPhone()) || StrUtil.hasEmpty(teacher.getTeacherPassword()) || StrUtil.hasEmpty(teacher.getVerifyPassword())
                || !teacher.getTeacherPassword().equals(teacher.getVerifyPassword())){
            return CommonResult.failed("文本框为空或两次输入的密码不一致");
        }

        Teacher one = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_phone", teacher.getTeacherPhone()).eq("del_flag", "1"));
        if (one==null&&one.equals("")){
            return CommonResult.failed("手机号码不存在");
        }
        // 修改的条件
        UpdateWrapper<Teacher> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_uuid",one.getUserUuid());

        // 修改的内容
        Teacher tea = new Teacher();
        tea.setTeacherPassword(teacher.getTeacherPassword());
        boolean update = teacherService.update(tea, updateWrapper);
        if (!update){
            return CommonResult.failed("密码修改失败");
        }
        return CommonResult.success(update, "密码修改成功");

    }

    @ApiOperation("教师修改密码")
    @PostMapping("UpdatePassword")
    public CommonResult UpdatePassword(HttpServletRequest request,@RequestBody Teacher teacher){
        System.out.println("教师修改密码");
        System.out.println(teacher);
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        if (teacher.getUserUuid()!=null){
            userUuid=teacher.getUserUuid();
        }
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid",userUuid);
        Teacher one = teacherService.getOne(wrapper);
        if (!teacher.getTeacherPassword().equals(one.getTeacherPassword())){
            return CommonResult.failed("旧密码错误");
        }
        if (!teacher.getNewPassword().equals(teacher.getVerifyPassword())){
            return CommonResult.failed("两次密码输入不一致");
        }
        one.setTeacherPassword(teacher.getNewPassword());
        boolean update = teacherService.update(one, wrapper);
        if (!update){
            return CommonResult.failed("修改密码失败");
        }
        return CommonResult.success(update,"修改成功");
    }

    @ApiOperation("查询教师详情")
    @PostMapping("getTeacherParticular")
    public CommonResult getTeacherParticular(HttpServletRequest request){
        // 获取存储在token的user_uuid
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        Teacher teacher1= teacherService.getOne(new QueryWrapper<Teacher>().eq("user_uuid", userUuid).eq("del_flag", "1"));
        if (teacher1==null){
            return CommonResult.failed("用户不存在");
        }
        List<Classes> classesByTeacherUuid = teacherClassMapper.getClassesByTeacherUuid(userUuid);
        teacher1.setClasses(classesByTeacherUuid);
        return CommonResult.success(teacher1);
    }

    @ApiOperation("查询我管理的班级")
    @PostMapping("getMyClassList")
    public CommonResult GetMyClass(HttpServletRequest request)   {
        // token中有当前登录用户的user_uuid
        String uuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null) {
            return CommonResult.failed("您还没有登录，请先登录");
        }
        //通过教师ID查询到该老师管理的班级
        QueryWrapper<TeacherClass> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_uuid", uuid);
        //获取到所有uuid和登录的教师的uuid符合的教师信息
        List<TeacherClass> list = teacherClassService.list(wrapper);
        if (list.size()==0){
            return CommonResult.failed("未查询到班级");
        }
        //查询到老师管理的所有班级的id
        List<Classes>classlist=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            QueryWrapper<Classes> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("class_uuid",list.get(i).getClassUuid());
            Classes classes = classesService.getOne(wrapper1);
            classlist.add(classes);
        }
        if (classlist.isEmpty()){
            return CommonResult.failed("未查询到班级");
        }
        return CommonResult.success(classlist,"查询到以下班级");
    }
}
