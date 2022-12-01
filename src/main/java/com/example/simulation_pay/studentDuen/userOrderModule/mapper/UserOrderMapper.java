package com.example.simulation_pay.studentDuen.userOrderModule.mapper;

import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.studentDuen.userOrderModule.entity.UserOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Mapper
public interface UserOrderMapper extends BaseMapper<UserOrder> {

    /**
     * 根据班级ID查询到该班级学生订单详情
     * @param
     * @return
     */
    @Select("select user_order.id,user_order.order_price,user_order.create_time,user_order.order_number,user_order.goods_json,user_order.remark,user_order.del_flag,student.student_name from user_order,student where user_order.user_uuid in (select student_class.student_uuid from student_class where student_class.class_uuid=#{classUuid})GROUP BY order_number\n")
    List<UserOrder> getOrderByClass(@Param("classUuid") String classUuid);
}
