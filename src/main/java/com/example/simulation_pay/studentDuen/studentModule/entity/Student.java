package com.example.simulation_pay.studentDuen.studentModule.entity;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    import cn.hutool.core.date.DateTime;
    import cn.hutool.json.JSONArray;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.example.simulation_pay.common.baseEntity.BaseEntity;
    import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 学生表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Student extends BaseEntity {

    private static final long serialVersionUID = 1L;

            /**
            * 用户外键uuid
            */
    private String userUuid;

            /**
            * 学生名称
            */
    private String studentName;

            /**
            * 学生性别
            */
    private Integer sex;

            /**
            * 学生登录密码
            */
    private String studentPassword;

            /**
            * 学生支付密码
            */
    private String payPassword;

            /**
            * 学生头像
            */
    private String studentImg;

            /**
            * 学生昵称
            */
    private String nickName;

            /**
            * 学生零钱
            */
    private BigDecimal looseChange;

            /**
            * 学生手机号
            */
    private String studentPhone;

            /**
            * 备注
            */
    private String remark;

            /**
            * 家庭住址
            */
    private String address;

            /**
            * 注册时间
            */
    private Date createDate;

            /**
            * 删除标记
            */
    private Integer delFlag;

            /**
            * 角色
            */
    private Integer role;

            /**
            * 是否(0/1)停用
            */
    private Integer status;

            /**
            * 最新修改时间
            */
    private String updateDate;

    /**
     * 新密码
     * 登录后修改密码使用
     * */
    @TableField(exist = false)
    private String newPassword;

    /**
     * 学生班级
     * */
    @TableField(exist =false)
    private List<Classes>classes;//此字段数据库中不存在

    /**
     * 学生班级2
     * */
    @TableField(exist =false)
    public List<String> namesOrIds;

    /**
     * 确认密码
     */
    @TableField(exist = false) // 此字段数据库中不存在
    private String verifyPassword;

    /**
     * 充值金额
     */
    @TableField(exist = false) // 此字段数据库中不存在
    private BigDecimal rechargeAmount;

    /**
     * 付款金额
     */
    @TableField(exist = false) // 此字段数据库中不存在
    private BigDecimal paymentAmount;

    /**
     * 付款金额
     */
    @TableField(exist = false) // 此字段数据库中不存在
    private JSONArray goodsJson;

    @TableField(exist = false)
    private ArrayList<BaseEntity> ids;
}
