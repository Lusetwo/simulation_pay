package com.example.simulation_pay.teacherDuen.qrcodeMoudle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Qrcode implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 老师Uuid
     */
    private String teacherUuid;

    /**
     * 图片名字
     */
    private Integer picCode;

    /**
     * 图片名字
     */
    private String picName;
    /**
     * 图片路径
     */
    private String picUrl;

    /**
     * 上传时间
     */
    private LocalDate createDate;
}
