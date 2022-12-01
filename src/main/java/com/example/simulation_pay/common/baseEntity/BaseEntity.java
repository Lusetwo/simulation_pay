package com.example.simulation_pay.common.baseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {

    @TableId(value = "id",type = IdType.AUTO) // 主键自增
    @ApiModelProperty(value = "主键ID")
    public Integer id;

    @TableField(exist = false) // 此字段数据库不存在
    @ApiModelProperty(value = "页数")
    private Integer currentPage;

    @TableField(exist = false)
    @ApiModelProperty(value = "条数")
    private Integer pageSize;
}
