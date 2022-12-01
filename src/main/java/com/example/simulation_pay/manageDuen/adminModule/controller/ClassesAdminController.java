package com.example.simulation_pay.manageDuen.adminModule.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.simulation_pay.common.baseEntity.BaseEntity;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.common.util.AuthorizationFilter;
import com.example.simulation_pay.common.util.MyUUid;
import com.example.simulation_pay.config.BaseList;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.manageDuen.adminModule.entity.GoodsSort;
import com.example.simulation_pay.manageDuen.adminModule.entity.StudentClass;
import com.example.simulation_pay.manageDuen.adminModule.entity.TeacherClass;
import com.example.simulation_pay.manageDuen.adminModule.mapper.StudentClassMapper;
import com.example.simulation_pay.manageDuen.adminModule.mapper.TeacherClassMapper;
import com.example.simulation_pay.manageDuen.adminModule.service.IClassesService;
import com.example.simulation_pay.manageDuen.adminModule.service.IStudentClassService;
import com.example.simulation_pay.manageDuen.adminModule.service.ITeacherClassService;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentService;
import com.example.simulation_pay.teacherDuen.teacherModule.entity.Teacher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Api(tags = "班级管理模块-控制器")
@RestController
@RequestMapping("api/adminModule/classes")
public class ClassesAdminController {

    @Autowired
    private IClassesService classesService;

    @Autowired
    private AuthorizationFilter filter;

    @Autowired
    private IStudentClassService studentClassService;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private TeacherClassMapper teacherClassMapper;

    @Autowired
    private ITeacherClassService teacherClassService;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private JwtConfig jwtConfig;

    @ApiOperation("添加新班级")
    @PostMapping("addClass")//权限控制
    public CommonResult NewClass(HttpServletRequest request, @RequestBody Classes classes){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        boolean b = filter.ReleaseIntercept(uuid);
        if (b==false){
            return CommonResult.failed("对不起,您没有权限执行此操作");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("class_name",classes.getClassName()).eq("del_flag","1");
        Classes one = classesService.getOne(wrapper);
        if (one!=null){
            return CommonResult.failed("班级重复创建");
        }
        classes.setClassUuid(MyUUid.MyUUIDCreate());
        boolean save = classesService.save(classes);
        if (save==false){
            return CommonResult.failed("创建新班级失败");
        }
        return CommonResult.success("新班级创建成功");
    }

    @ApiOperation("修改班级名称")
    @PostMapping("updateClass")//权限控制
    public CommonResult UpdateClass(HttpServletRequest request,@RequestBody Classes classes){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        boolean b = filter.ReleaseIntercept(uuid);
        if (b==false){
            return CommonResult.failed("对不起,您没有权限执行此操作");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("class_name",classes.getClassUuid());
        Classes one = classesService.getOne(wrapper);
        if (one!=null){
            return CommonResult.failed("该班级已经存在");
        }
        QueryWrapper<Classes> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("class_uuid",classes.getClassUuid());
        boolean update = classesService.update(classes, wrapper1);
        if (update==false){
            return CommonResult.failed("修改班级名称失败");
        }
        return CommonResult.success("修改成功");
    }

    @ApiOperation("换班主任")
    @PostMapping("updateClassDirector")
    public CommonResult UpdateClassDirector(@RequestBody Classes classes){
        System.out.println("更换班主任");
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_director",classes.getClassDirector()).eq("del_flag",1);
        Classes one = classesService.getOne(queryWrapper);
        if (one!=null){
            return CommonResult.failed("该教师已经在"+one.getClassName()+"班担任班主任");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",1).eq("class_uuid",classes.getClassUuid());
        Classes one1 = classesService.getOne(wrapper);
        one1.setClassDirector(classes.getClassDirector());
        boolean b = classesService.updateById(one1);
        return CommonResult.success(b,b?"修改成功":"修改失败");
    }

    @ApiOperation("查询全部班级")
    @PostMapping("getClassList")
    public CommonResult GetClassList(HttpServletRequest request){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",1);
        List<Classes> list = classesService.list(wrapper);
//        if (list.isEmpty()){
//            return CommonResult.failed("未查询到班级信息");
//        }
        return CommonResult.success(list,"查询到以下班级");
    }

    @ApiOperation("分页查询全部班级")
    @PostMapping("getClassListByPage")
    public CommonResult GetClassListByPage(HttpServletRequest request,@RequestBody BaseEntity baseEntity){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",1);
        Page<Classes> page = new Page<>(baseEntity.getCurrentPage(),baseEntity.getPageSize());
        // 分页查询学生列表
        IPage<Classes> studentIPage = classesService.page(page,wrapper);
        return CommonResult.success(studentIPage,"查询到以下班级");
    }

    @ApiOperation("查询我管理的全部班级")
    @PostMapping("getMyClassList")
    public CommonResult getMyClassList(HttpServletRequest request){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        List<Classes> list = teacherClassMapper.getClassesByTeacherUuid(uuid);
        if (list.isEmpty()){
            return CommonResult.failed("未查询到班级,请先联系管理员分配班级");
        }
        return CommonResult.success(list,"查询到以下班级");
    }

    @ApiOperation("模糊查询班级信息")
    @PostMapping("GetClassByName")
    public CommonResult getClassByName(HttpServletRequest request,@RequestBody Classes classes){
        System.out.println("通过班级名称查询班级");
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        System.out.println(classes);
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",1);
        if (classes.getClassUuid()!=null){
            wrapper.eq("class_uuid",classes.getClassUuid());
        }
        if (classes.getClassName()!=null){
            wrapper.like("class_name",classes.getClassName());
        }
        if (classes.getClassDirector()!=null){
            wrapper.like("class_director", classes.getClassDirector());
        }
        List<Classes> list = classesService.list(wrapper);
        return CommonResult.success(list,"查询到以下班级");
    }

    @ApiOperation("班级毕业")//权限控制
    @PostMapping("deleteClassAndStudentAndTeacher")
    public CommonResult DeleteClass(HttpServletRequest request, @RequestBody BaseList baseList){
        System.out.println(baseList);
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        boolean b = filter.ReleaseIntercept(uuid);
        if (!b){
            return CommonResult.failed("对不起,您没有权限执行此操作");
        }
        if (baseList==null){
            return CommonResult.failed("请至少选择一个班级进行删除");
        }
        for (int i = 0; i < baseList.namesOrIds.size(); i++) {
            //班级
            QueryWrapper<Classes> wrapper = new QueryWrapper<>();
            wrapper.eq("class_uuid",baseList.namesOrIds.get(i))
            .eq("del_flag",1);
            Classes one = classesService.getOne(wrapper);
            if (one==null){
                return CommonResult.failed("未查询到该班级");
            }
            one.setDelFlag(0);
            boolean update = classesService.update(one, wrapper);
            if (!update){
                return CommonResult.failed("班级删除失败");
            }

            //老师班级
            QueryWrapper<TeacherClass> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("class_uuid",baseList.namesOrIds.get(i)).eq("del_flag",1);
            List<TeacherClass> one1 = teacherClassService.list(wrapper1);
            if (!one1.isEmpty()){
                for (int j = 0; j < one1.size(); j++) {
                    QueryWrapper<TeacherClass> wrapper2 = new QueryWrapper<>();
                    wrapper2.eq("teacher_uuid",one1.get(i).getTeacherUuid()).eq("class_uuid",baseList.namesOrIds.get(i));
                    teacherClassService.getOne(wrapper2);
                    one1.get(i).setDelFlag(0);
                    boolean update1 = teacherClassService.update(one1.get(i), wrapper2);
                    if (!update1){
                        return CommonResult.failed("取消班级和老师关系时出错.");
                    }
                }
            }
            //学生班级
            QueryWrapper<StudentClass> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("class_uuid",baseList.namesOrIds.get(i)).eq("del_flag",1);
            List<StudentClass> one2 = studentClassService.list(wrapper2);
            if (one2==null){
                for (int j = 0; j < one2.size(); j++) {
                    QueryWrapper<StudentClass> wrapper3 = new QueryWrapper<>();
                    wrapper3.eq("student_uuid",one2.get(j).getStudentUuid()).eq("class_uuid",baseList.namesOrIds.get(i));
                    studentClassService.getOne(wrapper3);
                    one2.get(j).setDelFlag(0);
                    boolean update1 = studentClassService.update(one2.get(j), wrapper3);
                    if (!update1){
                        return CommonResult.failed("取消班级和老师关系时出错.");
                    }
                }
            }
            //学生
            QueryWrapper<StudentClass> wrapper3 = new QueryWrapper<>();
            wrapper3.eq("class_uuid",baseList.namesOrIds.get(i));
            List<StudentClass> list = studentClassService.list(wrapper3);
            if (list.size()>0){
                for (int j = 0; j < list.size(); j++) {
                    QueryWrapper<Student> wrapper4 = new QueryWrapper<>();
                    wrapper4.eq("user_uuid",list.get(i).getStudentUuid());
                    Student one3 = studentService.getOne(wrapper4);
                    if (one3==null){
                        return CommonResult.failed("删除学生时出错");
                    }
                    one3.setDelFlag(0);
                    boolean update3 = studentService.update(one3, wrapper4);
                    if (!update3){
                        return CommonResult.failed("删除学生时出错.");
                    }
                }
            }
        }
        return CommonResult.success("设置该班级结课成功");
    }
}
