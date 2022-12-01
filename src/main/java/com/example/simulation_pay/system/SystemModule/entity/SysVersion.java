package com.example.simulation_pay.system.SystemModule.entity;

    import java.time.LocalDateTime;
    import java.io.Serializable;

    import com.example.simulation_pay.common.baseEntity.BaseEntity;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author 龚某鑫
* @since 2021-08-31
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class SysVersion extends BaseEntity {

    private static final long serialVersionUID = 1L;

            /**
            * 版本名称
            */
    private String versionName;

            /**
            * 版本描述
            */
    private String versionDescribe;

            /**
            * 版本号
            */
    private String versionValue;

            /**
            * 创建时间
            */
    private LocalDateTime createTime;

            /**
            * 学生端下载地址
            */
    private String studentAppDownloadUrl;

            /**
            * 教师端下载地址
            */
    private String teacherAppDownloadUrl;

            /**
            * 是否推送
            */
    private String push;

            /**
            * 删除标记
            */
    private String delFlag;

            /**
            * 备注
            */
    private String remark;


}
