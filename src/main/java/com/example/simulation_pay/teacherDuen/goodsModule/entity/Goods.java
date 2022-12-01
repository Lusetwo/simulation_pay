package com.example.simulation_pay.teacherDuen.goodsModule.entity;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.io.Serializable;
    import java.util.Date;
    import java.util.List;

    import com.baomidou.mybatisplus.annotation.TableField;
    import com.example.simulation_pay.common.baseEntity.BaseEntity;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 商品表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Goods extends BaseEntity {

    private static final long serialVersionUID = 1L;

            /**
            * 商品名称
            */
    private String goodsName;

            /**
            * 商品分类
            */
    private String goodsSort;

            /**
            * 商品图片接受
            */
            @TableField(exist =false)
    private List<String> goodsPicture2;//数据库不存在

    /**
     * 商品图片存储
     * */
    private String goodsPicture;
            /**
            * 商品库存
            */
    private Integer goodsStore;

            /**
            * 商品价格
            */
    private BigDecimal goodsPrice;

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
            * 商品条形码
            */
    private String goodsBarcode;


}
