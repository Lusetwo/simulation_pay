package com.example.simulation_pay.studentDuen.studentModule.controller;


import cn.hutool.core.date.DateUnit;
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
import com.example.simulation_pay.manageDuen.adminModule.mapper.StudentClassMapper;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.mapper.StudentMapper;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Api(tags = "学生模块-控制器")
@RestController
@RequestMapping("api/studentModule/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private StudentClassMapper studentClassMapper;


    @ApiOperation("注册")
    @PostMapping("saveUser")
    public CommonResult saveUser(@RequestBody Student student){
        if (StrUtil.hasEmpty(student.getStudentName())
                || StrUtil.hasEmpty(student.getStudentPhone())
                || StrUtil.hasEmpty(student.getStudentPassword())){
            return CommonResult.failed("文本框的内容不能为空");
        }
//        Student studentName = studentService.getOne(new QueryWrapper<Student>().eq("student_name", student.getStudentName()).eq("del_flag","1"));
//        if (studentName != null){
//            return CommonResult.failed("用户名称已存在");
//        }
        Student one = studentService.getOne(new QueryWrapper<Student>().eq("student_phone", student.getStudentPhone()).eq("del_flag", "1"));
        if (one != null){
            return CommonResult.failed("手机号已存在");
        }
        Student stu = new Student();
        stu.setStudentName(student.getStudentName());//学生姓名
        if (student.getNickName()!=null){
            stu.setNickName(student.getNickName());
        }else {
            stu.setNickName(student.getStudentName());
        }
        stu.setStudentPhone(student.getStudentPhone());//联系电话
        stu.setStudentPassword(student.getStudentPassword());//登录密码
        stu.setUserUuid(MyUUid.MyUUIDCreate());//生成学生uuid
        stu.setCreateDate(DateUtil.date());//注册时间
        boolean save = studentService.save(stu);
        if (!save){
            return CommonResult.failed("注册失败");
        }
        return CommonResult.success(save,"注册成功");
    }

    @ApiOperation("登录")
    @PostMapping("login")
    public CommonResult getUserByNameAndPassword(@RequestBody Student student){
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("student_name",student.getStudentName())
                .eq("student_password",student.getStudentPassword());
        Student one = studentService.getOne(wrapper);
        if (one.getStatus()==0
                ||one.getStatus().equals("0")){
            return CommonResult.failed("您的账户已被停用!");
        }
        JSONObject jsonObject = new JSONObject();
        if(student.getStudentName()==null){
            return CommonResult.failed("用户名不能为空.");
        }
        if (student.getStudentPassword()==null){
            return CommonResult.failed("密码不能为空.");
        }
        one.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        studentService.updateById(one);
        QueryWrapper<Student> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("student_name",student.getStudentName())
                .eq("student_password",student.getStudentPassword())
                .eq("del_flag",1)
                .eq("status",1);
        Student one1 = studentService.getOne(wrapper1);
        if (one1==null){
            return CommonResult.failed("用户不存在或密码错误");
        }
        // 创建token，将用户UUID添加到token中
        String token = jwtConfig.createToken(one1.getUserUuid());
        if (!StringUtils.isEmpty(token)){
            // 将创建的token添加到json
            jsonObject.set("token",token);
        }
        return CommonResult.success(jsonObject,"登录成功");
    }

    @ApiOperation("手机号登录")
    @PostMapping("loginByPhone")
    public CommonResult studentPhoneLogin(@RequestBody Student student){
        JSONObject jsonObject = new JSONObject();
        Student stu = null;
        if (!StrUtil.hasEmpty(student.getStudentPhone())){
            stu = studentService.getOne(new QueryWrapper<Student>().eq("student_phone",student.getStudentPhone()).eq("del_flag","1"));
        }
        if (stu == null){
            return CommonResult.failed("用户不存在");
        }
        // 创建token，将用户UUID添加到token中
        String token = jwtConfig.createToken(stu.getUserUuid());
        if (!StringUtils.isEmpty(token)){
            // 将创建的token添加到json
            jsonObject.set("token",token);
        }
        return CommonResult.success(jsonObject,"登录成功");
    }

    @ApiOperation("忘记密码")
    @PostMapping("updateStudentPassword")
    public CommonResult updateStudentPassword(@RequestBody Student student){
        if (StrUtil.hasEmpty(student.getStudentPhone()) //联系电话
                || StrUtil.hasEmpty(student.getStudentPassword()) //学生新密码
                || StrUtil.hasEmpty(student.getVerifyPassword())//确认密码
                || !student.getStudentPassword().equals(student.getVerifyPassword())){//支付密码和电话做验证
            return CommonResult.failed("文本框为空或两次密码输入不一致.");
        }
        QueryWrapper<Student> wrapper=new QueryWrapper<>();
        wrapper.eq("student_phone", student.getStudentPhone())
                .eq("del_flag", "1");
        Student one = studentService.getOne(wrapper);
        if (one == null){
            return CommonResult.failed("未查询到用户");
        }
        boolean update = studentService.update(student, wrapper);
        return CommonResult.success(update, "密码修改成功");
    }

    @ApiOperation("查询当前登录学生详情")
    @PostMapping("getUserParticular")
    public CommonResult getUserParticular(HttpServletRequest request){
        // 获取存储在token的user_uuid
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        Student one = studentService.getOne(new QueryWrapper<Student>().eq("user_uuid", userUuid));
        if (one == null){
            return CommonResult.failed("用户不存在");
        }
        List<Classes> classesByStudentUuid = studentClassMapper.getClassesByStudentUuid(userUuid);
        if (classesByStudentUuid.size()!=0){
            one.setClasses(classesByStudentUuid);
        }
        return CommonResult.success(one);
    }

    @ApiOperation("零钱充值")
    @PostMapping("addLooseChange")
    public CommonResult addLooseChange(HttpServletRequest request,@RequestBody Student student){
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid",userUuid).eq("del_flag","1");
        Student one = studentService.getOne(wrapper);
        System.out.println("密码1:"+one.getPayPassword()+"\t密码2:"+student.getPayPassword());
        if (!student.getPayPassword().equals(one.getPayPassword())){
            return CommonResult.failed("支付密码错误.");
        }
        BigDecimal looseChange=one.getLooseChange();
        BigDecimal finaLooseChange=looseChange.add(student.getRechargeAmount());
        student.setLooseChange(finaLooseChange);
        boolean update = studentService.update(student, wrapper);
        if (!update){
            return CommonResult.failed("金额充值失败.");
        }
        return CommonResult.success("金额充值成功");
    }

    @ApiOperation("修改密码")
    @PostMapping("UpdatePassword")
    public CommonResult UpdatePassword(HttpServletRequest request,@RequestBody Student student){
        System.out.println("修改密码");
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        if (student.getUserUuid()!=null){
            userUuid=student.getUserUuid();
        }
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid",userUuid);
        Student one = studentService.getOne(wrapper);
        if (!student.getStudentPassword().equals(one.getStudentPassword())){
            return CommonResult.failed("旧密码错误");
        }
        if (!student.getNewPassword().equals(student.getVerifyPassword())){
            return CommonResult.failed("两次密码输入不一致");
        }
        one.setStudentPassword(student.getNewPassword());
        boolean update = studentService.update(one, wrapper);
        if (!update){
            return CommonResult.failed("修改密码失败");
        }
        return CommonResult.success(update,"修改成功");
    }
}
