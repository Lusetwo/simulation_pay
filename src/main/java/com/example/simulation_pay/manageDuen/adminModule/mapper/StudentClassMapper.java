package com.example.simulation_pay.manageDuen.adminModule.mapper;

import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.manageDuen.adminModule.entity.StudentClass;
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
public interface StudentClassMapper extends BaseMapper<StudentClass> {

    /**
     * 根据学生ID查询到班级详情
     * @param
     * @return
     */
    @Select("SELECT*FROM classes where classes.class_uuid in (select  student_class.class_uuid from student_class where student_class.student_uuid=#{studentUuid} AND  student_class.del_flag='1')")
    List<Classes> getClassesByStudentUuid(@Param("studentUuid") String teacherUuid);

}
