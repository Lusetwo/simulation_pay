package com.example.simulation_pay.manageDuen.adminModule.mapper;

import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.manageDuen.adminModule.entity.TeacherClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Mapper
public interface TeacherClassMapper extends BaseMapper<TeacherClass> {
    /**
     * 根据教师ID查询到该老师管理的所有班级的信息
     * @param
     * @return
     */
    @Select("SELECT*FROM classes where classes.class_uuid in (select  teacher_class.class_uuid from teacher_class where teacher_class.teacher_uuid=#{teacherUuid} AND  teacher_class.del_flag='1')")
    List<Classes> getClassesByTeacherUuid(@Param("teacherUuid") String teacherUuid);
}
