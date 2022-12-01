package com.example.simulation_pay.manageDuen.adminModule.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.simulation_pay.common.baseEntity.BaseEntity;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.common.util.AuthorizationFilter;
import com.example.simulation_pay.common.util.MyUUid;
import com.example.simulation_pay.config.BaseList;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.manageDuen.adminModule.entity.StudentClass;
import com.example.simulation_pay.manageDuen.adminModule.entity.TeacherClass;
import com.example.simulation_pay.manageDuen.adminModule.mapper.TeacherClassMapper;
import com.example.simulation_pay.manageDuen.adminModule.service.ITeacherClassService;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.teacherDuen.teacherModule.entity.Teacher;
import com.example.simulation_pay.teacherDuen.teacherModule.service.ITeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
@RequestMapping("api/adminModule/teacher-class")
public class TeacherAdminController {

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AuthorizationFilter filter;

    @Autowired
    private ITeacherClassService teacherClassService;

    @Autowired
    private TeacherClassMapper teacherClassMapper;

    @ApiOperation("查询教师列表")
    @PostMapping("getTeacherList")
    public CommonResult getTeacherList(HttpServletRequest request,@RequestBody BaseEntity baseEntity) throws NullPointerException{
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        // 分页页码、分页页数
        Page<Teacher> page = new Page<>(baseEntity.getCurrentPage(),baseEntity.getPageSize());
        // 查询老师列表
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag","1");
        // 分页查询老师列表
        IPage<Teacher> studentIPage = teacherService.page(page, wrapper);
        for (int i = 0; i < studentIPage.getRecords().size(); i++) {
            List<Classes> list = teacherClassMapper.getClassesByTeacherUuid(studentIPage.getRecords().get(i).getUserUuid());
            List<String>list1=new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                list1.add(list.get(j).getClassUuid());
                System.out.println(j+"班级:"+list.get(j).getClassUuid());
            }
            if (list1.size()>0){
                studentIPage.getRecords().get(i).setNamesOrIds(list1);;
            }
            if (list.size()>0){
                studentIPage.getRecords().get(i).setClasses(list);
            }
        }
        return CommonResult.success(studentIPage,"查询成功");
    }

    @ApiOperation("教师名称或手机号查询")
    @PostMapping("getTeacherNameOrTeacherPhone")
    public CommonResult getTeacherNameOrTeacherPhone(@RequestBody Teacher teacher){
        Teacher one = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_name", teacher.getTeacherName()).or()
                .eq("teacher_phone", teacher.getTeacherPhone()).eq("del_flag", "0"));
        if (one == null){
            return CommonResult.failed("查询失败");
        }
        return CommonResult.success(one);
    }

    @ApiOperation("批量删除教师")
    @PostMapping(value = "/batchRemoveTeacher",consumes = "application/json")
    public CommonResult batchRemoveStudent(HttpServletRequest request,@RequestBody BaseList userUuid){
        String userUui = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUui == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        if (userUuid.namesOrIds.size()==0){
            return CommonResult.failed("请至少选择一位同学进行删除");
        }
        for (int i = 0; i < userUuid.namesOrIds.size(); i++) {
            QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
            wrapper.eq("user_uuid",userUuid.namesOrIds.get(i));
            Teacher one = teacherService.getOne(wrapper);
            if (one!=null){
                one.setDelFlag(0);
                boolean update = teacherService.update(one, wrapper);
                if (!update){
                    return CommonResult.failed("【"+one.getTeacherName()+"】"+"该老师删除失败");
                }
            }
            List<Classes> list = teacherClassMapper.getClassesByTeacherUuid(userUuid.namesOrIds.get(i));
            if (list.size()>0){
                for (int j = 0; j < list.size(); j++) {
                    QueryWrapper<TeacherClass> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("teacher_uuid",userUuid.namesOrIds.get(i)).eq("class_uuid",list.get(j).getClassUuid());
                    TeacherClass one1 = teacherClassService.getOne(wrapper1);
                    if (one1==null){
                        return CommonResult.failed("解绑班级失败");
                    }
                    one1.setDelFlag(0);
                    boolean update = teacherClassService.update(one1, wrapper1);
                    if (update==false){
                        return CommonResult.failed("解除班级绑定时出错");
                    }
                }
            }
        }
        return CommonResult.success("批量删除成功");
    }

    @ApiOperation("添加教师")
    @PostMapping("saveTeacher")//权限控制
    public CommonResult saveTeacher(HttpServletRequest request,@RequestBody Teacher teacher) {
        String uuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        boolean b = filter.ReleaseIntercept(uuid);
        if (b == false) {
            return CommonResult.failed("对不起,您没有权限执行此操作");
        }
        //去除重复
        QueryWrapper<Teacher> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("teacher_phone",teacher.getTeacherPhone())
                .eq("del_flag","1");
        Teacher one1 = teacherService.getOne(wrapper2);
        if (one1!=null){
            return CommonResult.failed("该手机号已经被注册");
        }

        if (teacher.getNamesOrIds()==null){
            return CommonResult.failed("请给老师分配至少一个班级");
        }
        if (teacher.getTeacherName()==null){
            return CommonResult.failed("用户名不能为空");
        }
        teacher.setCreateTime(DateUtil.date());
        teacher.setUserUuid(MyUUid.MyUUIDCreate());
        teacher.setRemark(teacher.getRemark());
        if (teacher.getTeacherPassword()==null){
            teacher.setTeacherPassword("123456");
        }
        teacher.setTeacherPassword(teacher.getTeacherPassword());
        teacher.setRole(1);
        boolean save = teacherService.save(teacher);
        if (!save) {
            return CommonResult.failed("教师添加失败");
        }
        if (teacher.getNamesOrIds().size() > 0) {
            for (int i = 0; i < teacher.getNamesOrIds().size(); i++) {
                Teacher tea = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_name", teacher.getTeacherName())
                        .or().eq("teacher_phone", teacher.getTeacherPhone()).eq("del_flag", "1"));
                TeacherClass aClass = new TeacherClass();
                aClass.setTeacherUuid(tea.getUserUuid());
                aClass.setClassUuid(teacher.getNamesOrIds().get(i));
                aClass.setCreateDate(DateUtil.date());
                boolean save2 = teacherClassService.save(aClass);
                if (!save2) {
                    return CommonResult.failed("给教师分配班级出错.");
                }
            }
        }
        return CommonResult.success(save, "教师添加成功");
    }

    @ApiOperation("教师Excel导出")
    @GetMapping("teacherExcel")//权限控制
    public CommonResult teacherExcel(HttpServletResponse response, HttpServletRequest request){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        boolean b = filter.ReleaseIntercept(uuid);
        if (b==false){
            return CommonResult.failed("对不起,您没有权限执行此操作");
        }
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "1");
        List<Teacher> list = teacherService.list(wrapper);

        ArrayList<Teacher> arrayList = CollUtil.newArrayList(list);

        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();

        //自定义标题别名
        writer.addHeaderAlias("id","编号");
        writer.addHeaderAlias("userUuid", "用户外键UUID");
        writer.addHeaderAlias("teacherName", "教师名称");
        writer.addHeaderAlias("teacherPhone", "教师手机号");
        writer.addHeaderAlias("teacherPassword", "教师登录密码");
        writer.addHeaderAlias("looseChange", "教师金额");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("verifyPassword","确认密码");
        writer.addHeaderAlias("gatheringAmount","收款金额");
        writer.addHeaderAlias("currentPage","当前页码");
        writer.addHeaderAlias("pageSize","页数");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("delFlag", "删除标记");

        // 一次性写出内容，使用默认样式，
        writer.write(arrayList);

        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename= TeacherTable.xls");
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
        return CommonResult.success("导出完毕.");
    }

    @ApiOperation("统一编辑教师信息")
    @PostMapping("updateTeacher")
    public CommonResult updateTeacher(HttpServletRequest request, @RequestBody Teacher teacher){
        // 获取存储在token的user_uuid
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<Teacher> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("teacher_phone",teacher.getTeacherPhone()).eq("del_flag","1");
        Teacher one = teacherService.getOne(wrapper2);
        if (one!=null){
            if (one.getId()!=teacher.getId()){
                return CommonResult.failed("改手机号已经被使用");
            }
        }
        boolean b = teacherService.updateById(teacher);
        if (!b){
            return CommonResult.failed("修改教师基本信息时出现异常");
        }
        QueryWrapper<TeacherClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_uuid",teacher.getUserUuid()).eq("del_flag","1");
        List<TeacherClass> list1 = teacherClassService.list(queryWrapper);
        for (int i = 0; i < list1.size(); i++) {
            teacherClassService.removeById(list1.get(i));
        }
        if (teacher.getNamesOrIds()!=null){
            for (int i = 0; i < teacher.getNamesOrIds().size(); i++) {
                TeacherClass aClass = new TeacherClass();
                aClass.setTeacherUuid(teacher.getUserUuid());
                aClass.setClassUuid(teacher.getNamesOrIds().get(i));
                boolean save = teacherClassService.save(aClass);
                if (!save){
                    return CommonResult.failed("给教师分配班级出错.");
                }
            }
        }
        return CommonResult.success("修改教师信息成功.");
    }

    @ApiOperation("统一条件查询教师")
    @PostMapping("FindTeacherByCondition")
    public CommonResult FindteacherByCondition(HttpServletRequest request,@RequestBody Teacher teacher){
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag","1");
        if (teacher.getUserUuid()!=null){
            wrapper.like("user_uuid",teacher.getUserUuid());
        }
        if (teacher.getTeacherName()!=null){
            wrapper.like("teacher_name",teacher.getTeacherName());
        }
        if (teacher.getTeacherPhone()!=null){
            wrapper.like("teacher_phone",teacher.getTeacherPhone());
        }
        List<Teacher> list = teacherService.list(wrapper);
        if (list.isEmpty()){
            return CommonResult.failed("未查询到信息");
        }
        for (int i = 0; i < list.size(); i++) {
            List<Classes> classesList = teacherClassMapper.getClassesByTeacherUuid(list.get(i).getUserUuid());
            list.get(i).setClasses(classesList);
        }
        return CommonResult.success(list,"查询到以下信息");
    }

}
