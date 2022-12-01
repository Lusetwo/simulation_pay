package com.example.simulation_pay.common.baseEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentFriendVo extends BaseEntity{
    /**
     * 申请人uuid
     */
    @ApiModelProperty(value = "学生uuid")
    private String userUuid;

    /**
     * 申请人名称
     */
    @ApiModelProperty(value = "学生名称")
    private String studentName;

    /**
     * 申请人头像
     */
    @ApiModelProperty(value = "学生头像")
    private String studentImg;

    /**
     * 被申请人ID
     */
    @ApiModelProperty(value = "申请人ID")
    private String applyForId;

    /**
     * 被申请人学生名称
     */
    @ApiModelProperty(value = "学生名称")
    private String applyForName;

    /**
     * 被申请人头像
     */
    @ApiModelProperty(value = "学生头像")
    private String applyForPic;

    /**
     * 我的身份:申请人:被申请人
     * */
    @TableField(exist = false)
    private String MyStatus;

    /**
     * 申请记录唯一标识
     * */
    private String uuid;

    /**
     * 申请状态
     */
    @ApiModelProperty(value = "申请状态")
    private String applyForStruts;

}
