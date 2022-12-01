package com.example.simulation_pay.teacherDuen.teacherModule.entity;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.io.Serializable;
    import java.util.Date;
    import java.util.List;

    import cn.hutool.core.date.DateTime;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.example.simulation_pay.common.baseEntity.BaseEntity;
    import com.example.simulation_pay.manageDuen.adminModule.entity.Classes;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 教师表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Teacher extends BaseEntity {

    private static final long serialVersionUID = 1L;

            /**
            * 用户外键uuid
            */
    private String userUuid;

            /**
            * 教师名称
            */
    private String teacherName;

            /**
            * 教师手机号
            */
    private String teacherPhone;

            /**
            * 教师登录密码
            */
    private String teacherPassword;

            /**
            * 教师金额
            */
    private BigDecimal looseChange;

            /**
            * 教师昵称
            */
    private String teacherNickName;

            /**
            * 创建时间
            */
    private Date createTime;

            /**
            * 备注
            */
    private String remark;

            /**
            * 删除标记
            */
    private Integer delFlag;

            /**
            * 教师角色绑定
            */
    private Integer role;

            /**
            * 最新修改信息时间
            */
    private Date updateTime;

    /**
     * 新密码
     * 登录后修改密码使用
     * */
    @TableField(exist = false)
    private String newPassword;

    /**
     * 教师班级
     * */
    @TableField(exist =false)
    private List<Classes> classes;//此字段数据库中不存在

    /**
     * 老师班级2
     * */
    @TableField(exist =false)
    public List<String> namesOrIds;

    /**
     * 确认密码（用于忘记密码的使用）
     */
    @TableField(exist = false) // 此字段数据库中不存在
    private String verifyPassword;

    /**
     * 收款金额
     */
    @TableField(exist = false)
    private BigDecimal gatheringAmount;
}
