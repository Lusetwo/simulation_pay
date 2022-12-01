package com.example.simulation_pay.common.baseEntity;

import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
/**
 * 用于查询学生详情包括班级
 * */
public class StudentAllEntity {
    /**
     * 学生uuid
     * */
    private String userUuid;
    /**
     * 学生头像
     * */
    private MultipartFile studentPic;
    /**
     * 学生头像
     * */
    private String studentImg;
    /**
     * 学生姓名
     * */
    private String studentName;
    /**
     * 学生性别
     * */
    private char studentSex;
    /**
     * 学生登录密码
     * */
    private String studentPassword;
    /**
     * 学生联系电话
     * */
    private String studentPhone;
    /**
     * 学生账户余额
     * */
    private BigDecimal studentLooseChange;
    /**
     * 支付密码
     * */
    private String studentPayWord;
    /**
     * 学生留言
     * */
    private String studentRemark;
    /**
     * 学生班级
     * */
    private List<Classes> studentClasses;
    private List<String>classes;
    /**
     * 学生家庭住址
     * */
    private String studentAddress;
    /**
     * 学生昵称
     * */
    private String studentNickName;
    /**
     * 学生是否删除
     * */
    private String studentDelFlage;
    /**
     * 学生是否启用
     * */
    private String status;
}
