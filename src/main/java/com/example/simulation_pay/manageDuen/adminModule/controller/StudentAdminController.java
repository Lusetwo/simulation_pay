package com.example.simulation_pay.manageDuen.adminModule.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.simulation_pay.common.baseEntity.BaseEntity;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.common.util.AuthorizationFilter;
import com.example.simulation_pay.config.BaseList;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.manageDuen.adminModule.entity.StudentClass;
import com.example.simulation_pay.manageDuen.adminModule.mapper.StudentClassMapper;
import com.example.simulation_pay.manageDuen.adminModule.mapper.TeacherClassMapper;
import com.example.simulation_pay.manageDuen.adminModule.service.IClassesService;
import com.example.simulation_pay.manageDuen.adminModule.service.IStudentClassService;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@RestController
@RequestMapping("api/adminModule/student")
public class StudentAdminController {

    @Autowired
    private IStudentService studentService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private IStudentClassService studentClassService;

    @ApiOperation("分页查询全部学生列表")
    @PostMapping("getStudentList")
    public CommonResult getStudentList(@RequestBody BaseEntity baseEntity) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag","1");
        // 分页页码、分页页数
        Page<Student> page = new Page<>(baseEntity.getCurrentPage(),baseEntity.getPageSize());
        // 分页查询学生列表
        IPage<Student> studentIPage = studentService.page(page,wrapper);
        for (int i = 0; i < studentIPage.getRecords().size(); i++) {
            System.out.println("获取"+studentIPage.getRecords().get(i).getStudentName()+"的班级信息");
            List<Classes> list = studentClassMapper.getClassesByStudentUuid(studentIPage.getRecords().get(i).getUserUuid());
            if (list.size()>0){
                studentIPage.getRecords().get(i).setClasses(list);
            }
            List<String>list1=new ArrayList<>();
            for (int j = 0; j <list.size(); j++) {
                list1.add(list.get(j).getClassUuid());
            }
            if (list1.size()>0){
                studentIPage.getRecords().get(i).setNamesOrIds(list1);
            }
        }
        return CommonResult.success(studentIPage);
    }

    @ApiOperation("分页查询指定班级下的学生列表")
    @PostMapping("getStudentListByPage")
    public CommonResult getStudentListByPage(HttpServletRequest request, @RequestBody Classes classes) {
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<StudentClass> wrapper = new QueryWrapper<>();
        wrapper.eq("class_Uuid",classes.getClassUuid()).eq("del_flag",1);
        if (classes.getCurrentPage()==0||classes.getCurrentPage().equals("")){
            classes.setCurrentPage(1);
        }
        if (classes.getPageSize()==0||classes.getPageSize().equals("")){
            classes.setPageSize(10);
        }
        // 分页页码、分页页数
        Page<StudentClass> page = new Page<>(classes.getCurrentPage(),classes.getPageSize());
        // 分页查询学生列表
        IPage<StudentClass> studentIPage = studentClassService.page(page,wrapper);
        List<StudentClass> records = studentIPage.getRecords();
        List<Student>studentList=new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            QueryWrapper<Student> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("user_uuid",records.get(i).getStudentUuid());
            System.out.println("查询uuid为:"+records.get(i).getStudentUuid());
            Student one = studentService.getOne(wrapper1);
            if (one==null){
                return CommonResult.failed("查询学生详情出错");
            }
            List<Classes> classesByStudentUuid = studentClassMapper.getClassesByStudentUuid(records.get(i).getStudentUuid());
            if (classesByStudentUuid.size()!=0){
                one.setClasses(classesByStudentUuid);
            }
            List<String>list1=new ArrayList<>();
            for (int j = 0; j < classesByStudentUuid.size(); j++) {
                list1.add(classesByStudentUuid.get(i).getClassUuid());
            }
            if (list1.size()>0){
                one.setNamesOrIds(list1);
            }
            one.setNamesOrIds(list1);
            studentList.add(one);
        }
        if (studentList.isEmpty()){
            return CommonResult.failed("未查询到学生信息");
        }
        return CommonResult.success(studentList,"查询到以下学生信息");
    }

    @ApiOperation("统一条件查询学生")
    @PostMapping("FindStudentByCondition")
    public CommonResult FindStudentByCondition(HttpServletRequest request,@RequestBody Student student){
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag","1");
       if (student.getUserUuid()!=null){
           wrapper.like("user_uuid",student.getUserUuid());
       }
       if (student.getStudentName()!=null){
           wrapper.like("student_name",student.getStudentName());
       }
       if (student.getSex()!=null){
           if(student.getSex()==3){

           }else {
               wrapper.eq("sex",student.getSex());
           }
       }
       if (student.getStudentPhone()!=null){
           wrapper.like("student_phone",student.getStudentPhone());
       }
       if (student.getStatus()!=null){
           if(student.getStatus().equals("3")){

           }else {
               wrapper.eq("status",student.getStatus());           }
       }
        List<Student> list = studentService.list(wrapper);
        if (list.isEmpty()){
            return CommonResult.failed("未查询到信息");
        }
        for (int i = 0; i < list.size(); i++) {
            List<Classes> classesList = studentClassMapper.getClassesByStudentUuid(list.get(i).getUserUuid());
            list.get(i).setClasses(classesList);
        }
        return CommonResult.success(list,"查询到以下信息");
    }

    @ApiOperation("批量删除")
    @PostMapping(value = "/batchRemoveStudent",consumes = "application/json")
    public CommonResult batchRemoveStudent(HttpServletRequest request,@RequestBody BaseList userUuid){
        String userUui = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUui == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        if (userUuid.namesOrIds.isEmpty()){
            return CommonResult.failed("请至少选择一位学生进行删除");
        }
        for (int i = 0; i < userUuid.namesOrIds.size(); i++) {
            QueryWrapper<Student> wrapper = new QueryWrapper<>();
            wrapper.eq("user_uuid",userUuid.namesOrIds.get(i));
            Student one = studentService.getOne(wrapper);
            if (one!=null){
                one.setDelFlag(0);
                one.setStatus(0);
                boolean update = studentService.update(one, wrapper);
                if (!update){
                    return CommonResult.failed("【"+one.getStudentName()+"】"+"该学生删除失败");
                }
            }
            List<Classes> list = studentClassMapper.getClassesByStudentUuid(userUuid.namesOrIds.get(i));
            if (list.size()>0){
                for (int j = 0; j < list.size(); j++) {
                    QueryWrapper<StudentClass> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("student_uuid",userUuid.namesOrIds.get(i)).eq("class_uuid",list.get(j).getClassUuid());
                    StudentClass one1 = studentClassService.getOne(wrapper1);
                    if (one1==null){
                        return CommonResult.failed("解绑班级失败");
                    }
                    one1.setDelFlag(0);
                    boolean update = studentClassService.update(one1, wrapper1);
                    if (update==false){
                        return CommonResult.failed("解除班级绑定时出错");
                    }
                }
            }
        }
        return CommonResult.success("批量删除成功");
    }

    @ApiOperation("学生Excel导出")
    @GetMapping("StudentExcel")
    public void StudentExcel(HttpServletResponse response){
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "0");
        List<Student> list = studentService.list(wrapper);
        ArrayList<Student> arrayList = CollUtil.newArrayList(list);
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        //自定义标题别名
        writer.addHeaderAlias("id", "编号");
        writer.addHeaderAlias("userUuid", "用户外键UUID");
        writer.addHeaderAlias("studentName", "学生名称");
        writer.addHeaderAlias("studentPassword", "学生登录密码");
        writer.addHeaderAlias("payPassword", "学生支付密码");
        writer.addHeaderAlias("nickName", "学生昵称");
        writer.addHeaderAlias("looseChange", "学生零钱");
        writer.addHeaderAlias("studentPhone", "学生手机号");
        writer.addHeaderAlias("bankCard", "银行卡卡号");
        writer.addHeaderAlias("isUntie", "银行卡是否解绑");
        writer.addHeaderAlias("verifyPassword","确认密码");
        writer.addHeaderAlias("paymentAmount","支付金额");
        writer.addHeaderAlias("rechargeAmount","充值金额");
        writer.addHeaderAlias("currentPage","当前页码");
        writer.addHeaderAlias("pageSize","页数");
        writer.addHeaderAlias("studentImg", "学生头像");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("delFlag", "删除标记");
        // 一次性写出内容，使用默认样式，
        writer.write(arrayList);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename= StudentTable.xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.flush(out, true);
            // 关闭writer，释放内存
            writer.close();
            //此处记得关闭输出Servlet流
            IoUtil.close(out);
        }
        return;
    }

    @ApiOperation("统一修改学生信息")
    @PostMapping("updateStudent")
    public CommonResult updateStudentNickName(HttpServletRequest request,@RequestBody Student student){
        System.out.println(student);
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        System.out.println(userUuid);
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        //去重过滤
       if (student.getStudentPhone()!=null){
           QueryWrapper<Student> wrapper3 = new QueryWrapper<>();
           wrapper3.eq("del_flag","1").eq("student_phone",student.getStudentPhone());
           Student one1 = studentService.getOne(wrapper3);
           if (one1!=null){
               //电话重复使用
                   if (!student.getUserUuid().equals(one1.getUserUuid())){
                       return CommonResult.failed("当前电话已被使用");
                   }
           }
       }
        System.out.println("student:"+student);
        boolean b = studentService.updateById(student);
        QueryWrapper<StudentClass> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("student_uuid",student.getUserUuid()).eq("del_flag","1");
        List<StudentClass> list = studentClassService.list(wrapper1);
        if (list.size()>0){
            studentClassService.remove(wrapper1);
        }
        if (student.getNamesOrIds()!=null){
            for (int i = 0; i < student.getNamesOrIds().size(); i++) {
                StudentClass aClass = new StudentClass();
                aClass.setStudentUuid(student.getUserUuid());
                aClass.setClassUuid(student.getNamesOrIds().get(i));
                aClass.setDelFlag(1);
                boolean save = studentClassService.save(aClass);
                if (!save){
                    return CommonResult.failed("给学生分配班级出错.");
                }
            }
        }
        return CommonResult.success("修改学生信息成功.");
    }

    @ApiOperation("手机端修改学生信息")
    @PostMapping("updateStudent2")
    public CommonResult updateStudent2(HttpServletRequest request,@RequestBody Student student){
        System.out.println("修改学生手机号->");
        System.out.println(student);
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        //去重过滤
        QueryWrapper<Student> wrapper3 = new QueryWrapper<>();
        if (student.getStudentPhone()!=null){
        wrapper3.eq("del_flag","1").eq("student_phone",student.getStudentPhone());
        Student one1 = studentService.getOne(wrapper3);
        if (one1!=null){
            //电话重复使用
                if (!userUuid.equals(one1.getUserUuid())){
                    return CommonResult.failed("当前电话已被使用");
                }
        }
        }
        Student one = studentService.getOne(new QueryWrapper<Student>().eq("del_flag", "1").eq("user_uuid", userUuid));
        System.out.println("uuid查询用户"+one);
        System.out.println(one.getId());
        student.setId(one.getId());
        System.out.println("student:"+student);
        boolean b = studentService.updateById(student);
        return CommonResult.success(b,"修改学生信息成功.");
    }
}
