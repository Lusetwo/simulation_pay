package com.example.simulation_pay.teacherDuen.goodsModule.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.simulation_pay.common.baseEntity.BaseEntity;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.TemporaryOrder;
import com.example.simulation_pay.studentDuen.userOrderModule.entity.UserOrder;
import com.example.simulation_pay.studentDuen.userOrderModule.service.IUserOrderService;
import com.example.simulation_pay.teacherDuen.goodsModule.entity.Goods;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentService;
import com.example.simulation_pay.teacherDuen.goodsModule.service.IGoodsService;
import com.example.simulation_pay.teacherDuen.teacherModule.entity.Teacher;
import com.example.simulation_pay.teacherDuen.teacherModule.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author 卢某
 * @since 2021-05-23
 */
@Api(tags = "商品模块-控制器")
@RestController
@RequestMapping("api/goodsModule/goods")
public class GoodsController {

    @Autowired
    private IStudentService studentService;

    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private IUserOrderService userOrderService;

    @ApiOperation("扫码查价")
    @PostMapping("getGoods")
    public CommonResult getGoods(@RequestBody Goods goods){
        Goods selectGoods = goodsService.getOne(new QueryWrapper<Goods>()
                .eq("goods_barcode",goods.getGoodsBarcode()).eq("del_flag", "1"));
        if (selectGoods == null){
            return CommonResult.failed("商品不存在");
        }
        selectGoods.setGoodsPicture2(JSON.parseArray(selectGoods.getGoodsPicture(),String.class));
        return CommonResult.success(selectGoods,"商品查询成功");
    }

    @ApiOperation("输入金额收款")
    @PostMapping("updateStudentLooseChange")
    public CommonResult updateStudentLooseChange(@RequestBody Student student){
        // 不足两位小数补0
        DecimalFormat decimalFormat = new DecimalFormat("0.00#");
        Student one = studentService.getOne(new QueryWrapper<Student>().eq("user_uuid", student.getUserUuid()).eq("del_flag", "1"));
        // 页面传递过来的金额
        BigDecimal gatheringAmount = new BigDecimal(decimalFormat.format(student.getPaymentAmount()));
        if (gatheringAmount.compareTo(new BigDecimal("0.00")) == 0 || gatheringAmount.compareTo(new BigDecimal("0.00")) == -1){
            return CommonResult.failed("输入金额不合法，金额必须大于0");
        }
        // 数据库查询出来的金额
        BigDecimal looseChange = one.getLooseChange();
        // 计算后的金额
        BigDecimal paymentAmount = looseChange.subtract(gatheringAmount);
        if (looseChange.compareTo(gatheringAmount) == -1) {
            return CommonResult.failed("您的零钱不足");
        }
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        if (student.getUserUuid() != null && !student.getUserUuid().equals("")){
            updateWrapper.eq("user_uuid",student.getUserUuid());
        }
        Student student1 = new Student();
        student1.setLooseChange(paymentAmount);
        boolean update = studentService.update(student1, updateWrapper);
        if (!update){
            return CommonResult.failed("收款失败");
        }
        return CommonResult.success(update, "收款成功");

    }

    @ApiOperation("收款码收款")
    @PostMapping("ewnPayment")
    public CommonResult ewnPayment(@RequestBody ArrayList<BaseEntity> ids){
        List<Integer> idList = new ArrayList<>();
        for (int i=0; i<ids.size(); i++){
            Integer id = ids.get(i).getId();
            idList.add(id);
        }
        // 批量根据ID查询
        Collection<Goods> goods = goodsService.listByIds(idList);
        if (CollUtil.isEmpty(goods)){
            return CommonResult.failed("商品信息为空不能产生订单");
        }
        return CommonResult.success(goods);
    }

    @ApiOperation("加入购物车")
    @PostMapping("addShoppingCart")
    public CommonResult addShoppingCart(@RequestBody Goods goods){
        if(goods.getGoodsBarcode() == "" || goods.getGoodsBarcode() == null){
            return CommonResult.failed("未检测到商品条形码");
        }
        //查询商品信息
        Goods goods1 = goodsService.getOne(new QueryWrapper<Goods>().eq("goods_barcode", goods.getGoodsBarcode()).eq("del_flag", "1"));
        if (goods1 == null){
            return CommonResult.failed("商品信息为空");
        }
        return CommonResult.success(goods1);
    }

    @ApiOperation("确认购买")
    @PostMapping("sureToBuy")
    public CommonResult SureToBuy(HttpServletRequest request,@RequestBody List<TemporaryOrder>goodsList){
        //获取到购物车中的商品信息列表,用于生成订单
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        //生成订单
        String orderNumber=(int)('A'+Math.random()*('Z'-'A'+1))+(new Date().getTime()+"");
        for (int i = 0; i < goodsList.size(); i++) {
            UserOrder userOrder=new UserOrder();
            QueryWrapper<Goods> wrapper = new QueryWrapper<>();
            wrapper.eq("goods_barcode",goodsList.get(i).getGoodBarCode()).eq("del_flag","1");
            Goods one = goodsService.getOne(wrapper);
            userOrder.setGoodsJson(one.getGoodsName());//设置商品
            userOrder.setCreateTime(new Date());//设置订单生成时间
            userOrder.setGoodsSum(goodsList.get(i).getSum());
            userOrder.setOrderNumber(orderNumber);//设置订单号
            userOrder.setOrderPrice(goodsList.get(i).getPrice().multiply(new BigDecimal(goodsList.get(i).getSum())));//设置总金额
            userOrder.setGoodsPrice(goodsList.get(i).getPrice());
            userOrder.setGoodsBarcode(goodsList.get(i).getGoodBarCode());//商品条码
            boolean save = userOrderService.save(userOrder);
            if (!save){
                return CommonResult.failed("生成待支付订单失败.");
            }
        }
        QueryWrapper<UserOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number",orderNumber);
        List<UserOrder> list = userOrderService.list(wrapper);
        if (list.isEmpty()){
            return CommonResult.failed("查询待支付订单失败.");
        }
        return CommonResult.success(list,"成功生成待支付订单");
    }

    @ApiOperation("完成订单")
    @PostMapping("endOrder")
    public CommonResult endOrder(HttpServletRequest request,@RequestBody UserOrder userOrder){
        System.out.println(userOrder);
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
//        String userUuid=null;
//        //如果当前登录的用户是学生,就直接使用学生的uuid
//        QueryWrapper<Student>queryWrapper1=new QueryWrapper<>();
//        queryWrapper1.eq("user_uuid",uuid).eq("del_flag","1");
//        Student one2 = studentService.getOne(queryWrapper1);
//        if (one2!=null){
//            userUuid=one2.getUserUuid();
//        }else {
//            userUuid=userOrde r.getUserUuid();
//        }

        String number = userOrder.getOrderNumber();
        if (number==null){
            return CommonResult.failed("请输入订单号");
        }
        QueryWrapper<UserOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number",number).eq("status","0");
        List<UserOrder> list = userOrderService.list(wrapper);
        if (list.isEmpty()){
            return CommonResult.success("未查询到待支付的订单");
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println("订单:"+i+":"+list.get(i));
                list.get(i).setStatus("1");
                list.get(i).setUserUuid(userOrder.getUserUuid());
                boolean update = userOrderService.updateById(list.get(i));
                QueryWrapper<Goods> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("goods_barcode",list.get(i).getGoodsBarcode()).eq("del_flag","1");
                Goods one1 = goodsService.getOne(wrapper2);
                one1.setGoodsStore(one1.getGoodsStore()-list.get(i).getGoodsSum());
                goodsService.updateById(one1);
                if (!update){
                    return CommonResult.failed("完成订单失败");
                }
        }
        return CommonResult.success("完成订单.");
    }
}
