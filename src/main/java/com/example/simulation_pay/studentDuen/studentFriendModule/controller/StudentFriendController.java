package com.example.simulation_pay.studentDuen.studentFriendModule.controller;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.simulation_pay.common.baseEntity.StudentFriendVo;
import com.example.simulation_pay.common.result.ApplyForResult;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.studentDuen.studentFriendModule.entity.FriendApplyFor;
import com.example.simulation_pay.studentDuen.studentFriendModule.entity.StudentFriend;
import com.example.simulation_pay.studentDuen.studentFriendModule.mapper.FriendApplyForMapper;
import com.example.simulation_pay.studentDuen.studentFriendModule.mapper.StudentFriendMapper;
import com.example.simulation_pay.studentDuen.studentFriendModule.service.IFriendApplyForService;
import com.example.simulation_pay.studentDuen.studentFriendModule.service.IStudentFriendService;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentService;
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
 * 学生好友表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@RestController
@RequestMapping("api/studentFriendModule/student-friend")
public class StudentFriendController {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private IStudentFriendService studentFriendService;
    @Autowired
    private IFriendApplyForService friendApplyForService;
    @Autowired
    private StudentFriendMapper studentFriendMapper;
    @Autowired
    private JwtConfig jwtConfig;

    @ApiOperation("扫码查询学生信息")
    @PostMapping("getStudentUser") // 已修改
    public CommonResult getStudentUser(HttpServletRequest request,@RequestBody Student student){
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null){
            return CommonResult.failed("您还没有登录，请先登录");
        }

        // 查询当前登录用户的信息
        Student one1 = studentService.getOne(new QueryWrapper<Student>().eq("user_uuid", token).eq("del_flag", "1"));
        // 查询传递过来的好友是否合法
        Student one = studentService.getOne(new QueryWrapper<Student>().eq("user_uuid", student.getUserUuid()).eq("del_flag", "1"));
        if (one == null){
            return CommonResult.failed("扫描的二维码异常或扫描用户不存在");
        }

        // 查询当前登录用户和传递过来的用户是否是好友
        List<StudentFriendVo> studentFriendList = studentFriendMapper.selectStudentFriend(one1.getUserUuid(),one.getUserUuid());

        return CommonResult.success((CollUtil.isNotEmpty(studentFriendList))?studentFriendList:one);

    }
    
    @ApiOperation("拒绝或添加好友")
    @PostMapping("saveOrNoFriend")
    public CommonResult saveFriend(HttpServletRequest request,@RequestBody ApplyForResult Apply){
        System.out.println("同意添加好友"+Apply);
        // 获取当前登录用户保存在jwt的user_uuid
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null){
            return CommonResult.failed("您还没有登录，请先登录");
        }
        System.out.println("token:"+token);

        if (Apply.getStatus().equals("0")){
            QueryWrapper<FriendApplyFor>apply=new QueryWrapper<>();
            apply.eq("uuid",Apply.getUuid());
            FriendApplyFor one = friendApplyForService.getOne(apply);
            one.setApplyForStruts("3");//拒绝添加好友
            boolean update = friendApplyForService.update(one, apply);
            if (!update){
                return CommonResult.failed("拒绝添加好友失败");
            }
            return CommonResult.success("拒绝该好友的申请成功");
        }
        QueryWrapper<FriendApplyFor> apply = new QueryWrapper<>();
        apply.eq("uuid",Apply.getUuid());
        FriendApplyFor one = friendApplyForService.getOne(apply);
        one.setApplyForStruts("2");
        boolean update = friendApplyForService.update(one, apply);
        if (!update){
            return CommonResult.failed("添加好友失败");
        }

        String userUuid = one.getUserUuid();//申请人uuid
        String applyForId = one.getApplyForId();//被申请人的uuid

        StudentFriend friend = new StudentFriend();
        friend.setUserUuid(userUuid);
        friend.setFriendId(applyForId);
        friend.setIsFriend("1");
        boolean save1 = studentFriendService.save(friend);

        StudentFriend friend2 = new StudentFriend();
        friend2.setUserUuid(applyForId);
        friend2.setFriendId(userUuid);
        friend2.setIsFriend("1");
        boolean save = studentFriendService.save(friend2);
        boolean str=false;
        if (save==true&&save1==true){
            str=true;
        }
        return CommonResult.success(str,true?"添加好友成功":"添加好友失败");

    }

    @ApiOperation("查询学生好友列表")
    @PostMapping("getStudentFriendList")
    public CommonResult getStudentFriendList(HttpServletRequest request){
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null){
            return CommonResult.failed("您还没有登录，请先登录");
        }

        Student user = studentService.getOne(new QueryWrapper<Student>().eq("user_uuid", token));
        List<StudentFriendVo> studentFriendList = studentFriendMapper.getStudentFriendList(user.getUserUuid());
        return CommonResult.success(studentFriendList,"好友列表查询成功");

    }
}
