package com.example.simulation_pay.studentDuen.studentModule.service.impl;

import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.mapper.StudentMapper;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
