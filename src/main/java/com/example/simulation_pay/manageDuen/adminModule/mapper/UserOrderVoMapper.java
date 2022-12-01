package com.example.simulation_pay.manageDuen.adminModule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.simulation_pay.common.baseEntity.UserOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author 卢某
 * @since 2021-05-25
 */
@Mapper
public interface UserOrderVoMapper extends BaseMapper<UserOrderVo> {

    /**
     * 订单表和学生表联表查询
     * @return
     */
    @Select("SELECT student.student_name,student.student_phone,user_order.* FROM student,user_order WHERE student.user_uuid = user_order.user_uuid AND user_order.del_flag='0'")
    List<UserOrderVo> getOrderList();

    /**
     * 订单号联表查询
     * @param orderNumber
     * @return
     */
    @Select("SELECT student.student_name,student.student_phone,user_order.* FROM student,user_order WHERE student.user_uuid = user_order.user_uuid AND user_order.order_number=#{orderNumber} AND user_order.del_flag='0'")
    List<UserOrderVo> getOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 用户名联表查询
     * @param studentName
     * @return
     */
    @Select("SELECT student.student_name,student.student_phone,user_order.* FROM student,user_order WHERE student.user_uuid = user_order.user_uuid AND student.student_name=#{studentName} AND user_order.del_flag='0'")
    List<UserOrderVo> getStudentName(@Param("studentName") String studentName);

    /**
     * 用户名称联表查询订单
     * @param studentName
     * @return
     */
    @Select("SELECT student.student_name,user_order.* FROM student INNER JOIN user_order WHERE student.user_uuid=user_order.user_uuid and student.student_name=#{studentName}")
    List<UserOrderVo> getStudentOrder(@Param("studentName") String studentName);

    /**
     * 班级和学生和订单连表查询
     * */
    @Select("select*from (SELECT student.student_name,student.student_phone,classes.id as class_id, classes.class_name,user_order.* FROM student,user_order,classes WHERE student.user_uuid = user_order.user_uuid AND user_order.del_flag='0' and student.classid=classes.id GROUP BY order_number) as jj where jj.class_id  in (select teacher.classid from teacher where teacher.user_uuid=#{teacherUUID} AND teacher.del_flag=1)")
    List<UserOrderVo>getStudentOrderByTeacherID(@Param("teacherUUID") String user_uuid);

    /**
     * 老师班级和学生之间链表查询
     * */
    @Select("select student.id,student.user_uuid,student.student_name,classes.class_name from student,classes where student.classid in(select classid from teacher where user_uuid=#{teacherUUID} and del_flag=1)GROUP BY user_uuid")
    List<UserOrderVo>getStudentsByTeacher(@Param("teacherUUID") String user_uuid);
}
