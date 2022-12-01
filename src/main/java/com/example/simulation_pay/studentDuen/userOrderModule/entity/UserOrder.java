package com.example.simulation_pay.studentDuen.userOrderModule.entity;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.io.Serializable;
    import java.util.Date;

    import com.example.simulation_pay.common.baseEntity.BaseEntity;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 订单表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class UserOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

            /**
            * 用户外键uuid
            */
    private String userUuid;


    /**
     * 订单号
     */
    private String orderNumber;

            /**
            * 订单金额
            */
    private BigDecimal orderPrice;



            /**
            * 订单中的商品信息
            */
    private String goodsJson;

            /**
            * 备注
            */
    private String remark;

            /**
             * 商品条码
             * */
    private String goodsBarcode;

            /**
            * 删除标记
            */
    private String delFlag;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 商品数量
     * */
    private Integer goodsSum;

    /**
     * 是否完成支付
     * */
    private String status;

    /**
     * 商品单价
     * */
    private BigDecimal goodsPrice;
}
