package com.example.simulation_pay.studentDuen.studentFriendModule.mapper;

import com.example.simulation_pay.common.baseEntity.StudentFriendVo;
import com.example.simulation_pay.studentDuen.studentFriendModule.entity.StudentFriend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 学生好友表 Mapper 接口
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Mapper
public interface StudentFriendMapper extends BaseMapper<StudentFriend> {

    // 联表查询好友信息，查询出当前用户的所有好友
    @Select("SELECT s.student_name AS NAME,sb.* FROM student s INNER JOIN student_friend f ON s.user_uuid=f.user_uuid INNER JOIN student sb ON sb.user_uuid=f.friend_id AND s.user_uuid=#{userUuid} AND f.del_flag ='1'")
    List<StudentFriendVo> getStudentFriendList(@Param("userUuid") String userUuid);

    // 联表查询具体好友
    @Select("SELECT s.student_name AS NAME,sb.* FROM student s INNER JOIN student_friend f ON s.user_uuid=f.user_uuid INNER JOIN student sb ON sb.user_uuid=f.friend_id AND s.user_uuid=#{userUuid} AND sb.user_uuid=#{friendId} AND f.del_flag ='1'")
    List<StudentFriendVo> selectStudentFriend(@Param("userUuid") String userUuid,@Param("friendId") String friendId);

}
