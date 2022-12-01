package com.example.simulation_pay.teacherDuen.orderModule;

import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.studentDuen.userOrderModule.entity.UserOrder;
import com.example.simulation_pay.studentDuen.userOrderModule.mapper.UserOrderMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "订单管理模块-控制器")
@RestController
@RequestMapping("/api/OrderModule/order")
public class OrderController {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserOrderMapper userOrderMapper;

    @ApiOperation("查询班级下的学生订单")
    @RequestMapping("getStudentOrderByClassID")
    public CommonResult getStudentOrderByClassID(HttpServletRequest request, @RequestBody Classes classes){
        String uuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null) {
            return CommonResult.failed("您还没有登录，请先登录");
        }
        List<UserOrder> orderByClass = userOrderMapper.getOrderByClass(classes.getClassUuid());
        if (orderByClass.isEmpty()){
            return CommonResult.failed("未查询到订单");
        }
        return CommonResult.success(orderByClass,"查询到以下订单信息");
    }
}
