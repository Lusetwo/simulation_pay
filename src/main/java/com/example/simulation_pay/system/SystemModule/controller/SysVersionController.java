package com.example.simulation_pay.system.SystemModule.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.simulation_pay.common.baseEntity.BaseEntity;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.system.SystemModule.entity.SysVersion;
import com.example.simulation_pay.system.SystemModule.service.ISysVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-08-31
 */
@RestController
@RequestMapping("api/SystemModule/system-version")
public class SysVersionController {
    @Autowired
    private ISysVersionService systemVersionService;

    //添加版本信息
    @RequestMapping("AddSystemMessage")//全
    public CommonResult AddSystemMessage(@RequestBody SysVersion systemVersion){
        System.out.println("添加版本信息");
        QueryWrapper<SysVersion> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag","1");
        List<SysVersion> list = systemVersionService.list(wrapper);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setDelFlag("0");
            systemVersionService.updateById(list.get(i));
        }
        boolean save = systemVersionService.save(systemVersion);
        return CommonResult.success(save);
    }

    @RequestMapping("QuerySystemMessage")
    public CommonResult GetSystemMessage(@RequestBody BaseEntity entity){
        System.out.println("查询最新版本信息");
        QueryWrapper<SysVersion>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("del_flag","1");
        Page<SysVersion> page = new Page<>(entity.getCurrentPage(),entity.getPageSize());
        IPage<SysVersion> studentIPage = systemVersionService.page(page,queryWrapper);
        return CommonResult.success(studentIPage,"查询成功");
    }

    @RequestMapping("UpdateSystemMessage")//全
    public CommonResult UpdateSystemMessage(@RequestBody SysVersion systemVersion){
        System.out.println("修改版本信息");
        boolean b = systemVersionService.updateById(systemVersion);
        return CommonResult.success(b);
    }

//    @RequestMapping("DeleteSystemMessage")//id
//    public CommonResult DeleteSystemMessage(@RequestBody SystemVersion systemVersion){
//        System.out.println("删除版本信息");
//        boolean b = systemVersionService.removeById(systemVersion);
//        return CommonResult.success(b);
//    }



//    @RequestMapping("QuerySystemMessageList")//分页currentPage&pageSize
//    public CommonResult GetSystemMessageList(@RequestBody SystemVersion systemVersion){
//        System.out.println("查询所有系统版本信息");
//        QueryWrapper<SystemVersion> queryWrapper = new QueryWrapper<>();
//        Page<SystemVersion> page = new Page<>(systemVersion.getCurrentPage(),systemVersion.getPageSize());
//        IPage<SystemVersion> studentIPage = systemVersionService.page(page,queryWrapper);
//        return CommonResult.success(studentIPage.getRecords(),"查询成功");
//    }
}
