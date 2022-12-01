package com.example.simulation_pay.studentDuen.studentFriendModule.mapper;

import com.example.simulation_pay.common.baseEntity.StudentFriendVo;
import com.example.simulation_pay.studentDuen.studentFriendModule.entity.FriendApplyFor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 好友申请表 Mapper 接口
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Mapper
public interface FriendApplyForMapper extends BaseMapper<FriendApplyFor> {

    //被申请
    @Select("SELECT s.id,s.user_uuid as user_uuid,s.student_name as student_name,s.student_img,sb.student_name as apply_for_name,sb.student_img as apply_for_pic,sb.user_uuid as apply_for_id,f.uuid,f.apply_for_struts FROM student s INNER JOIN friend_apply_for f ON s.user_uuid = f.user_uuid INNER JOIN student sb ON sb.user_uuid = f.apply_for_id AND f.apply_for_id = #{applyForId} AND f.del_flag ='1'")
    List<StudentFriendVo> selectFriendParticularsB(@Param("applyForId") String applyForId);

    //申请者
    @Select("SELECT s.id,s.user_uuid as user_uuid,s.student_name as student_name,s.student_img,sb.student_name as apply_for_name,sb.student_img as apply_for_pic,sb.user_uuid as apply_for_id,f.uuid,f.apply_for_struts FROM student s INNER JOIN friend_apply_for f ON s.user_uuid = f.user_uuid AND f.user_uuid = #{applyForId} INNER JOIN student sb ON sb.user_uuid = f.apply_for_id AND f.del_flag ='1'")
    List<StudentFriendVo> selectFriendParticularsA(@Param("applyForId") String applyForId);

    @Select("SELECT s.* ,f.apply_for_struts,f.apply_for_id FROM student s INNER JOIN friend_apply_for f ON s.user_uuid = f.user_uuid INNER JOIN student sb ON sb.user_uuid = f.apply_for_id AND f.apply_for_id = #{applyForId} AND f.del_flag ='1' or sb.user_uuid=f.apply_for_id and f.user_uuid=#{applyForId} AND f.del_flag ='1'")
    List<StudentFriendVo>selectFriendApplyToMe(@Param("applyForId")String applyForId);

}
