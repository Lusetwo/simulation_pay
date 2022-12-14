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
 *  ???????????????
 * </p>
 *
 * @author ?????????
 * @since 2021-07-02
 */
@Api(tags = "??????????????????-?????????")
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

    @ApiOperation("???????????????")
    @PostMapping("addClass")//????????????
    public CommonResult NewClass(HttpServletRequest request, @RequestBody Classes classes){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("?????????????????????????????????");
        }
        boolean b = filter.ReleaseIntercept(uuid);
        if (b==false){
            return CommonResult.failed("?????????,??????????????????????????????");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("class_name",classes.getClassName()).eq("del_flag","1");
        Classes one = classesService.getOne(wrapper);
        if (one!=null){
            return CommonResult.failed("??????????????????");
        }
        classes.setClassUuid(MyUUid.MyUUIDCreate());
        boolean save = classesService.save(classes);
        if (save==false){
            return CommonResult.failed("?????????????????????");
        }
        return CommonResult.success("?????????????????????");
    }

    @ApiOperation("??????????????????")
    @PostMapping("updateClass")//????????????
    public CommonResult UpdateClass(HttpServletRequest request,@RequestBody Classes classes){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("?????????????????????????????????");
        }
        boolean b = filter.ReleaseIntercept(uuid);
        if (b==false){
            return CommonResult.failed("?????????,??????????????????????????????");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("class_name",classes.getClassUuid());
        Classes one = classesService.getOne(wrapper);
        if (one!=null){
            return CommonResult.failed("?????????????????????");
        }
        QueryWrapper<Classes> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("class_uuid",classes.getClassUuid());
        boolean update = classesService.update(classes, wrapper1);
        if (update==false){
            return CommonResult.failed("????????????????????????");
        }
        return CommonResult.success("????????????");
    }

    @ApiOperation("????????????")
    @PostMapping("updateClassDirector")
    public CommonResult UpdateClassDirector(@RequestBody Classes classes){
        System.out.println("???????????????");
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_director",classes.getClassDirector()).eq("del_flag",1);
        Classes one = classesService.getOne(queryWrapper);
        if (one!=null){
            return CommonResult.failed("??????????????????"+one.getClassName()+"??????????????????");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",1).eq("class_uuid",classes.getClassUuid());
        Classes one1 = classesService.getOne(wrapper);
        one1.setClassDirector(classes.getClassDirector());
        boolean b = classesService.updateById(one1);
        return CommonResult.success(b,b?"????????????":"????????????");
    }

    @ApiOperation("??????????????????")
    @PostMapping("getClassList")
    public CommonResult GetClassList(HttpServletRequest request){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("?????????????????????????????????");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",1);
        List<Classes> list = classesService.list(wrapper);
//        if (list.isEmpty()){
//            return CommonResult.failed("????????????????????????");
//        }
        return CommonResult.success(list,"?????????????????????");
    }

    @ApiOperation("????????????????????????")
    @PostMapping("getClassListByPage")
    public CommonResult GetClassListByPage(HttpServletRequest request,@RequestBody BaseEntity baseEntity){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("?????????????????????????????????");
        }
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",1);
        Page<Classes> page = new Page<>(baseEntity.getCurrentPage(),baseEntity.getPageSize());
        // ????????????????????????
        IPage<Classes> studentIPage = classesService.page(page,wrapper);
        return CommonResult.success(studentIPage,"?????????????????????");
    }

    @ApiOperation("??????????????????????????????")
    @PostMapping("getMyClassList")
    public CommonResult getMyClassList(HttpServletRequest request){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("?????????????????????????????????");
        }
        List<Classes> list = teacherClassMapper.getClassesByTeacherUuid(uuid);
        if (list.isEmpty()){
            return CommonResult.failed("??????????????????,?????????????????????????????????");
        }
        return CommonResult.success(list,"?????????????????????");
    }

    @ApiOperation("????????????????????????")
    @PostMapping("GetClassByName")
    public CommonResult getClassByName(HttpServletRequest request,@RequestBody Classes classes){
        System.out.println("??????????????????????????????");
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("?????????????????????????????????");
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
        return CommonResult.success(list,"?????????????????????");
    }

    @ApiOperation("????????????")//????????????
    @PostMapping("deleteClassAndStudentAndTeacher")
    public CommonResult DeleteClass(HttpServletRequest request, @RequestBody BaseList baseList){
        System.out.println(baseList);
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        boolean b = filter.ReleaseIntercept(uuid);
        if (!b){
            return CommonResult.failed("?????????,??????????????????????????????");
        }
        if (baseList==null){
            return CommonResult.failed("???????????????????????????????????????");
        }
        for (int i = 0; i < baseList.namesOrIds.size(); i++) {
            //??????
            QueryWrapper<Classes> wrapper = new QueryWrapper<>();
            wrapper.eq("class_uuid",baseList.namesOrIds.get(i))
            .eq("del_flag",1);
            Classes one = classesService.getOne(wrapper);
            if (one==null){
                return CommonResult.failed("?????????????????????");
            }
            one.setDelFlag(0);
            boolean update = classesService.update(one, wrapper);
            if (!update){
                return CommonResult.failed("??????????????????");
            }

            //????????????
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
                        return CommonResult.failed("????????????????????????????????????.");
                    }
                }
            }
            //????????????
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
                        return CommonResult.failed("????????????????????????????????????.");
                    }
                }
            }
            //??????
            QueryWrapper<StudentClass> wrapper3 = new QueryWrapper<>();
            wrapper3.eq("class_uuid",baseList.namesOrIds.get(i));
            List<StudentClass> list = studentClassService.list(wrapper3);
            if (list.size()>0){
                for (int j = 0; j < list.size(); j++) {
                    QueryWrapper<Student> wrapper4 = new QueryWrapper<>();
                    wrapper4.eq("user_uuid",list.get(i).getStudentUuid());
                    Student one3 = studentService.getOne(wrapper4);
                    if (one3==null){
                        return CommonResult.failed("?????????????????????");
                    }
                    one3.setDelFlag(0);
                    boolean update3 = studentService.update(one3, wrapper4);
                    if (!update3){
                        return CommonResult.failed("?????????????????????.");
                    }
                }
            }
        }
        return CommonResult.success("???????????????????????????");
    }
}
