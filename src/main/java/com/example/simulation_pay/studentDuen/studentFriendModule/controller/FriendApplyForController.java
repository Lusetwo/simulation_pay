package com.example.simulation_pay.studentDuen.studentFriendModule.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.simulation_pay.common.baseEntity.StudentFriendVo;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.common.util.MyUUid;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.studentDuen.studentFriendModule.entity.FriendApplyFor;
import com.example.simulation_pay.studentDuen.studentFriendModule.entity.StudentFriend;
import com.example.simulation_pay.studentDuen.studentFriendModule.mapper.FriendApplyForMapper;
import com.example.simulation_pay.studentDuen.studentFriendModule.mapper.StudentFriendMapper;
import com.example.simulation_pay.studentDuen.studentFriendModule.service.IFriendApplyForService;
import com.example.simulation_pay.studentDuen.studentFriendModule.service.IStudentFriendService;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.entity.StudentBankCard;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentBankCardService;
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
 * 好友申请表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@RestController
@RequestMapping("api/studentFriendModule/friend-apply-for")
public class FriendApplyForController {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private FriendApplyForMapper friendApplyForMapper;

    @Autowired
    private IStudentFriendService studentFriendService;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private IFriendApplyForService friendApplyForService;

    @ApiOperation("申请添加好友")
    @PostMapping("applyForSaveFriend")
    public CommonResult applyForSaveFriendA(HttpServletRequest request, @RequestBody Student student){
        // token中有当前登录用户的user_uuid
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null){
            return CommonResult.failed("您还没有登录，请先登录");
        }
        // 查询申请人是否存在
        Student one = null;
        if (student.getUserUuid() != null && !student.getUserUuid().equals("")){
            one = studentService.getOne(new QueryWrapper<Student>().eq("user_uuid", student.getUserUuid()).eq("del_flag", "1"));
        }
        if (one == null){
            return CommonResult.failed("申请用户不存在");
        }
        // 是否已经是好友
        // 查询好友表是否已经添加好友了，如果已经添加了，就不能再加了
        StudentFriend one1 = studentFriendService.getOne(new QueryWrapper<StudentFriend>().eq("user_uuid", token)
                .eq("friend_id", one.getUserUuid()).eq("del_flag", "1"));
        if (one1 != null){
            return CommonResult.failed("好友已存在，请别重复添加");
        }
        // 是否已经发起申请请求
        FriendApplyFor one2 = friendApplyForService.getOne(new QueryWrapper<FriendApplyFor>()
                .eq("user_uuid",token).eq("apply_for_id",one.getUserUuid())
                .eq("apply_for_struts", "1").eq("del_flag", "1"));
        if (one2 != null){
            return CommonResult.failed("您已申请添加好友，请别重复申请");
        }
        FriendApplyFor applyFor = new FriendApplyFor();
        applyFor.setUserUuid(token);
        applyFor.setUuid(MyUUid.MyUUIDCreate());
        applyFor.setApplyForId(one.getUserUuid());
        applyFor.setApplyForStruts("1");
        boolean save = friendApplyForService.save(applyFor);
        return CommonResult.success(save,save?"已发出好友申请，请等待好友同意":"好友申请发送失败");
    }

    @ApiOperation("申请我的")
    @PostMapping("getProposerParticularsB")
    public CommonResult getProposerParticularsB(HttpServletRequest request){
        // token中有当前登录用户的user_uuid
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null){
            return CommonResult.failed("您还没有登录，请先登录");
        }
        List<StudentFriendVo> studentFriendVos = friendApplyForMapper.selectFriendParticularsB(token);
        for (int i = 0; i < studentFriendVos.size(); i++) {
            studentFriendVos.get(i).setMyStatus("0");
        }
        return CommonResult.success(studentFriendVos,"申请我的");
    }

    @ApiOperation("我的申请")
    @PostMapping("getProposerParticularsA")
    public CommonResult getProposerParticularsA(HttpServletRequest request){
        // token中有当前登录用户的user_uuid
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null){
            return CommonResult.failed("您还没有登录，请先登录");
        }
        List<StudentFriendVo> studentFriendVos = friendApplyForMapper.selectFriendParticularsA(token);
        for (int i = 0; i < studentFriendVos.size(); i++) {
            studentFriendVos.get(i).setMyStatus("1");
        }
        return CommonResult.success(studentFriendVos,"我的申请");
    }

    @ApiOperation("和我有关的申请")
    @PostMapping("getProposerParticulars")
    public CommonResult getProposerParticular(HttpServletRequest request){
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null){
            return CommonResult.failed("您还没有登录，请先登录");
        }
        List<StudentFriendVo> studentFriendVos = friendApplyForMapper.selectFriendApplyToMe(token);
        return CommonResult.success(studentFriendVos);
    }
}
