package com.example.simulation_pay.studentDuen.userOrderModule.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentService;
import com.example.simulation_pay.studentDuen.userOrderModule.entity.UserOrder;
import com.example.simulation_pay.studentDuen.userOrderModule.service.IUserOrderService;
import com.example.simulation_pay.teacherDuen.goodsModule.entity.Goods;
import com.example.simulation_pay.teacherDuen.goodsModule.service.IGoodsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@RestController
@RequestMapping("api/userOrderModule/user-order")
public class UserOrderController {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private IUserOrderService iUserOrderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private IUserOrderService orderService;

    @ApiOperation("查询我的订单")
    @RequestMapping("GetMyOrder")
    public CommonResult getMyOrder(HttpServletRequest request){
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<UserOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid",userUuid).eq("del_flag",1);
        List<UserOrder> list = iUserOrderService.list(wrapper);
        if (list.isEmpty()){
            return CommonResult.failed("未查询到订单信息");
        }
        return CommonResult.success(list,"查询到以下订单.");
    }

    @ApiOperation("扫码付款")
    @PostMapping("ewnGenerateOrder")
    public CommonResult ewnGenerateOrder(HttpServletRequest request, @RequestBody Student student) {

        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null) {
            return CommonResult.failed("您还没有登录，请先登录");
        }

        if (student.getPayPassword() == null || student.getPayPassword().equals("")){
            return CommonResult.failed("文本框不能为空");
        }

        List<Integer> idList = new ArrayList<>();
        for (int i=0; i<student.getIds().size(); i++){
            Integer id = student.getIds().get(i).getId();
            idList.add(id);
        }

        // 批量根据ID查询
        Collection<Goods> goods = goodsService.listByIds(idList);
        if (CollUtil.isEmpty(goods)){
            return CommonResult.failed("商品信息为空不能产生订单");
        }

        Student one = studentService.getOne(new QueryWrapper<Student>().eq("user_uuid", token).eq("del_flag", "1"));
        // 学生原本的金额
        BigDecimal looseChange = one.getLooseChange();
        // 页面传递过来的金额
        BigDecimal paymentAmount = student.getPaymentAmount();
        if (looseChange.compareTo(paymentAmount) != 1) {
            return CommonResult.failed("您的零钱不足");
        }
        // 计算后的金额，做减法运算
        BigDecimal subtract = looseChange.subtract(paymentAmount);

        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid", token);
        if (student.getPayPassword() != null && !student.getPayPassword().equals("")) {
            wrapper.eq("pay_password", student.getPayPassword());
        }

        Student stu = new Student();
        stu.setLooseChange(subtract);

        boolean update = studentService.update(stu, wrapper);
        if (!update) {
            return CommonResult.failed("付款失败");
        }

        // 生成订单号
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String orderNumber = snowflake.nextIdStr();

        UserOrder userOrder = new UserOrder();
        userOrder.setUserUuid(token);
        userOrder.setOrderNumber(orderNumber);
        userOrder.setOrderPrice(paymentAmount);
        userOrder.setCreateTime(DateUtil.date());
        userOrder.setGoodsJson(goods.toString());

        // 创建订单
        boolean save = orderService.save(userOrder);
        if (!save) {
            return CommonResult.failed("订单生成失败");
        }

        return CommonResult.success(update, "付款成功");

    }

    @ApiOperation("付款码付款")
    @PostMapping("updateMoney")
    public CommonResult updateMoney(HttpServletRequest request, @RequestBody Student student) {
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null) {
            return CommonResult.failed("您还没有登录，请先登录");
        }
        Student one = studentService.getOne(new QueryWrapper<Student>().eq("user_uuid", token).eq("del_flag", "1"));
        // 学生原本的金额
        BigDecimal looseChange = one.getLooseChange();
        // 页面传递过来的金额
        BigDecimal paymentAmount = student.getPaymentAmount();
        // 计算后的金额，做减法运算
        BigDecimal subtract = looseChange.subtract(paymentAmount);
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid", token);
        Student stu = new Student();
        stu.setLooseChange(subtract);
        boolean update = studentService.update(stu, wrapper);
        if (!update) {
            return CommonResult.failed("付款失败");
        }
        // 生成订单号
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String orderNumber = snowflake.nextIdStr();
        UserOrder userOrder = new UserOrder();
        userOrder.setUserUuid(token);
        userOrder.setOrderNumber(orderNumber);
        userOrder.setOrderPrice(paymentAmount);
        userOrder.setCreateTime(DateUtil.date());
        // 创建订单
        boolean save = orderService.save(userOrder);
        if (!save) {
            return CommonResult.failed("订单生成失败");
        }
        return CommonResult.success(update, "付款成功");
    }

    @ApiOperation("支付帐单")
    @PostMapping("payBills")
    public CommonResult payBills(HttpServletRequest request, @RequestBody UserOrder userOrder) {
        String token = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (token == null) {
            return CommonResult.failed("您还没有登录，请先登录");
        }
        // 分页页码和分页条数
        Page<UserOrder> userOrderPage = new Page<>(userOrder.getCurrentPage(), userOrder.getPageSize());
        // 通过用户UUID查询所有订单
        QueryWrapper<UserOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uuid", token);
        // 调用分页查询所有订单
        IPage<UserOrder> page = orderService.page(userOrderPage, queryWrapper);
        return CommonResult.success(page);
    }
}
