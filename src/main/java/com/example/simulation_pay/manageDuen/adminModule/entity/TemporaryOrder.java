package com.example.simulation_pay.manageDuen.adminModule.entity;

import com.example.simulation_pay.teacherDuen.goodsModule.entity.Goods;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 临时订单载体
 * 数据库中不存在
 * */
@Data
public class TemporaryOrder implements Serializable {

    /**
     *商品id
     *  */
    private Integer id;
    /**
     * 商品总数
     * */
    private Integer sum;
    /**
     * 商品名称
     * */
    private String goodBarCode;
    /**
     * 商品单价
     * */
    private BigDecimal price;
}
