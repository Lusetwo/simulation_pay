package com.example.simulation_pay.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.simulation_pay.teacherDuen.teacherModule.entity.Teacher;
import com.example.simulation_pay.teacherDuen.teacherModule.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizationFilter {

    @Autowired
    private ITeacherService teacherService;


    public boolean ReleaseIntercept(String uuid){
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid",uuid);
        List<Teacher> list = teacherService.list(wrapper);
        if (!list.isEmpty()){
            if (list.get(0).getRole()==2||list.get(0).getRole()==3){
                return false;
            }
        }
        return true;
    }
}
