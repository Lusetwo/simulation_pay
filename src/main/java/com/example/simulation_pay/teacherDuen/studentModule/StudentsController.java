package com.example.simulation_pay.teacherDuen.studentModule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.common.util.FileUpload;
import com.example.simulation_pay.common.util.MyUUid;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import com.example.simulation_pay.manageDuen.adminModule.entity.StudentClass;
import com.example.simulation_pay.manageDuen.adminModule.mapper.StudentClassMapper;
import com.example.simulation_pay.manageDuen.adminModule.service.IStudentClassService;
import com.example.simulation_pay.studentDuen.studentModule.entity.Student;
import com.example.simulation_pay.studentDuen.studentModule.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-05-23
 */
@Api(tags = "学生模块-控制器")
@RestController
@RequestMapping("/api/StudentModule/student")
public class StudentsController {

    @Autowired
    private IStudentService studentService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private IStudentClassService studentClassService;

    @ApiOperation("分页查询指定班级下的学生列表")
    @PostMapping("getStudentListByPage")
    public CommonResult getStudentListByPage(HttpServletRequest request, @RequestBody Classes classes) {
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<StudentClass> wrapper = new QueryWrapper<>();
        wrapper.eq("class_Uuid",classes.getClassUuid()).eq("del_flag",1);
        // 分页页码、分页页数
        Page<StudentClass> page = new Page<>(classes.getCurrentPage(),classes.getPageSize());
        // 分页查询学生列表
        IPage<StudentClass> studentIPage = studentClassService.page(page,wrapper);
        List<StudentClass> records = studentIPage.getRecords();
        List<Student>studentList=new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            QueryWrapper<Student> wrapper1 = new QueryWrapper<>();
            System.out.println("查询uuid为:"+records.get(i).getStudentUuid());
            wrapper1.eq("user_uuid",records.get(i).getStudentUuid());
            Student one = studentService.getOne(wrapper1);
            if (one!=null){
                List<Classes> classesByStudentUuid = studentClassMapper.getClassesByStudentUuid(records.get(i).getStudentUuid());
                if (classesByStudentUuid.size()!=0){
                    one.setClasses(classesByStudentUuid);
                }
                studentList.add(one);
            }
        }
        if (studentList.isEmpty()){
            return CommonResult.failed("未查询到学生信息");
        }
        for (Student s:studentList
             ) {
            System.out.println("student"+s.getStudentName()+"del:"+s.getDelFlag());
        }
        return CommonResult.success(studentList,"查询到以下学生信息");
    }

    @ApiOperation("上传头像图片")
    @PostMapping("uploadStudentPic")
    public CommonResult UploadGoodsPic( MultipartFile file){
        FileUpload upload = new FileUpload();
        String s = upload.UploadFile(file);
        return CommonResult.success(s,"头像上传成功.");
    }

    @ApiOperation("添加学生")
    @PostMapping("saveStudent")
    public CommonResult saveStudent(HttpServletRequest request,@RequestBody Student student){
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        if (student.getStudentPhone()==null){
            return CommonResult.failed("手机号码不允许为空");
        }
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_phone",student.getStudentPhone()).eq("del_flag","1");
        Student one2 = studentService.getOne(queryWrapper);
        if (one2!=null){
            return CommonResult.failed("手机号已经被使用");
        }
        if (student.getStudentName()==null||student.getStudentName().equals("")){
            return CommonResult.failed("学生姓名不允许为空.");
        }
        if (student.getStudentImg()==null){
            student.setStudentImg("img/JPG_21d3f6c7634f4e00802744d6c12d4954_1625243178538.jpg");//默认头像
        }
        if (student.getNickName()==null){
            student.setNickName(student.getStudentName());
        }
        QueryWrapper<Student> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("del_flag","1").eq("nick_name",student.getNickName());
        Student one1 = studentService.getOne(wrapper3);
        if (one1!=null){
           return CommonResult.failed("昵称已被使用");
        }
        student.setUserUuid(MyUUid.MyUUIDCreate());//uuid
        if (student.getStudentPassword()==null
                ||student.getStudentPassword().equals("")){
            student.setStudentPassword("123456");//设置初始密码
        }
        student.setStudentPassword(student.getStudentPassword());//设置初始密码
        if (student.getPayPassword()==null){
            student.setPayPassword("123456");//设置初始支付密码
        }
        student.setPayPassword(student.getPayPassword());
        if (student.getNickName()==null){
            student.setNickName(student.getStudentName());//设置初始学生昵称
        }else {
            student.setNickName(student.getNickName());
        }
        student.setCreateDate(new Date());
        boolean save = studentService.save(student);
        if (!save){
            return CommonResult.failed("添加学生基本信息时出错.");
        }
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("student_name",student.getStudentName()).eq("student_phone",student.getStudentPhone());
        Student one = studentService.getOne(wrapper);//定位到该学生的uuid
        if (student.getNamesOrIds()!=null){
            for (int i = 0; i < student.getNamesOrIds().size(); i++) {
                StudentClass aClass = new StudentClass();
                aClass.setClassUuid(student.namesOrIds.get(i));
                aClass.setStudentUuid(one.getUserUuid());
                aClass.setCreateDate(new Date());
                boolean save1 = studentClassService.save(aClass);
                if (!save1){
                    return CommonResult.failed("给学生分配班级出错");
                }
            }
        }
        return CommonResult.success("添加学生成功.");
    }

    @ApiOperation("学生Excel导出")
    @PostMapping("StudentExcel")
    public void StudentExcel(HttpServletResponse response){

        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "1");

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
        writer.addHeaderAlias("createDate","注册时间");
        writer.addHeaderAlias("updateDate","最近修改时间");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("status","可用状态");
        writer.addHeaderAlias("role","角色定位");
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

    @ApiOperation("查询当前学生详细信息")
    @PostMapping("getStudentEveryThing")
    public CommonResult getStudentEveryThing(HttpServletRequest request,@RequestBody Student student){
        // 获取存储在token的user_uuid
        String userUuid = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (userUuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uuid",student.getUserUuid());
        Student one = studentService.getOne(wrapper);
        if (one==null){
            return CommonResult.failed("未查询到该学生详细信息");
        }
        List<Classes> list = studentClassMapper.getClassesByStudentUuid(student.getUserUuid());
        if (list.size()>0){
            one.setClasses(list);
        }
        return CommonResult.success(one,"查询到以下信息");
    }
}
