package com.example.simulation_pay.common.baseEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserOrderVo extends BaseEntity {

    /**
     * 用户外键uuid
     */
    @ApiModelProperty("用户外键uuid")
    private String userUuid;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private BigDecimal orderPrice;

    /**
     * 订单创建时间
     */
    @ApiModelProperty("订单创建时间")
    private Date createTime;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNumber;

    /**
     * 订单中的商品信息
     */
    @ApiModelProperty("订单中的商品信息")
    private String goodsJson;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


    /**
     * 删除标记
     */
    @ApiModelProperty("删除标记")
    private Integer delFlag;

    /**
     * 学生名称、别名
     */
    @TableField(exist = false) // 此字段数据库中不存在
    private String NAME;

    /**
     * 学生名称
     */
    @ApiModelProperty(value = "学生名称")
    private String studentName;

    /**
     * 学生手机号
     */
    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;

    /**
     * 好友ID
     */
    @ApiModelProperty(value = "好友ID")
    private String friendId;

    /**
     * 是否好友
     */
    @ApiModelProperty(value = "是否好友")
    private String isFriend;

}
