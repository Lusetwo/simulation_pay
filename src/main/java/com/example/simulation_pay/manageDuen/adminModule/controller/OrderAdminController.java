package com.example.simulation_pay.manageDuen.adminModule.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.simulation_pay.common.baseEntity.UserOrderVo;
import com.example.simulation_pay.common.result.CommonResult;

import com.example.simulation_pay.common.util.AuthorizationFilter;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.StudentClass;
import com.example.simulation_pay.manageDuen.adminModule.entity.TeacherClass;
import com.example.simulation_pay.manageDuen.adminModule.mapper.UserOrderVoMapper;
import com.example.simulation_pay.manageDuen.adminModule.service.IStudentClassService;
import com.example.simulation_pay.manageDuen.adminModule.service.ITeacherClassService;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.userOrderModule.entity.UserOrder;
import com.example.simulation_pay.studentDuen.userOrderModule.service.IUserOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author 卢某
 * @since 2021-05-25
 */
@Api(tags = "订单管理模块-控制器")
@RestController
@RequestMapping("/api/adminModule/orderAdmin")
public class OrderAdminController {
    @Autowired
    private IUserOrderService userOrderService;
    @Autowired
    private UserOrderVoMapper userOrderVoMapper;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private AuthorizationFilter filter;
    @Autowired
    private ITeacherClassService teacherClassService;
    @Autowired
    private IStudentClassService studentClassService;

    @ApiOperation("查询订单列表")
    @PostMapping("getOrderList")
    public CommonResult getOrderList(HttpServletRequest request){
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        System.out.println(userUuid);
        QueryWrapper<UserOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid",userUuid);
        List<UserOrder> orderList =  userOrderService.list(wrapper);
        if (orderList.size()==0){
            return CommonResult.failed("查询失败");
        }
        System.out.println("查询到以下订单:");
        for (UserOrder uo:orderList
             ) {
            System.out.println(uo);
        }
        return CommonResult.success(orderList,"查询到以下列表.");
    }

    @ApiOperation("订单号查询")
    @PostMapping("getOrderNumber")
    public CommonResult getOrderNumber(@RequestBody UserOrderVo userOrderVo){

        List<UserOrderVo> getOrderNumber = userOrderVoMapper.getOrderNumber(userOrderVo.getOrderNumber());
        if (CollUtil.isEmpty(getOrderNumber)){
            return CommonResult.failed("订单号不存在");
        }

        return CommonResult.success(getOrderNumber,"查询订单成功");

    }

    @ApiOperation("用户名称查询")
    @PostMapping("getStudentName")
    public CommonResult getStudentName(@RequestBody UserOrderVo userOrderVo){
        List<UserOrderVo> studentName = userOrderVoMapper.getStudentName(userOrderVo.getStudentName());
        if (CollUtil.isEmpty(studentName)){
            return CommonResult.failed("用户不存在");
        }
        return CommonResult.success(studentName,"查询成功");
    }

    @ApiOperation("订单列表Excel导出")
    @GetMapping("OrderListExcel")
    public void OrderListExcel(HttpServletResponse response){

        List<UserOrderVo> orderList =  userOrderVoMapper.getOrderList();

        ArrayList<UserOrderVo> arrayList = CollUtil.newArrayList(orderList);

        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();

//        //自定义标题别名
        writer.addHeaderAlias("id", "编号");
        writer.addHeaderAlias("userUuid", "用户外键UUID");
        writer.addHeaderAlias("studentName", "用户名称");
        writer.addHeaderAlias("studentPhone", "学生手机号");
        writer.addHeaderAlias("orderPrice", "订单金额");
        writer.addHeaderAlias("orderNumber", "订单号");
        writer.addHeaderAlias("createTime", "订单创建时间");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("currentPage","当前页码");
        writer.addHeaderAlias("pageSize","页数");
        writer.addHeaderAlias("delFlag", "删除标记");

        // 一次性写出内容，使用默认样式，
        writer.write(arrayList);

        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename= OrderTable.xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.flush(out, true);
            // 关闭writer，释放内存
            writer.close();
            //此处记得关闭输出Servlet流
            IoUtil.close(out);
        }
        return;

    }

    @ApiOperation("查询订单详情")
    @PostMapping("getUserOrder")
    public CommonResult getUserOrder(@RequestBody Student student){
        List<UserOrderVo> studentOrder = userOrderVoMapper.getStudentOrder(student.getStudentName());
        if (CollUtil.isEmpty(studentOrder)){
            return CommonResult.failed("学生订单为空");
        }
        return CommonResult.success(studentOrder);
    }

    @ApiOperation("删除订单")
    @PostMapping("deleteOrder")
    public CommonResult deleteOrder(@RequestBody UserOrder userOrder){
        UserOrder one = userOrderService.getOne(new QueryWrapper<UserOrder>().eq("order_number", userOrder.getOrderNumber()).eq("del_flag", 1));
        if (one == null){
            return CommonResult.failed("订单号不存在");
        }
        UpdateWrapper<UserOrder> wrapper = new UpdateWrapper<>();
        if (userOrder.getOrderNumber() != null && !userOrder.getOrderNumber().equals("")){
            wrapper.eq("order_number",userOrder.getOrderNumber());
        }
        UserOrder order = new UserOrder();
        order.setDelFlag("0");
        boolean update = userOrderService.update(order, wrapper);
        if (!update){
            return CommonResult.failed("订单删除失败");
        }
        return CommonResult.success(update,"订单删除成功");
    }

}
