package com.example.simulation_pay.manageDuen.adminModule.entity;

    import java.io.Serializable;
    import java.util.List;

    import com.example.simulation_pay.common.baseEntity.BaseEntity;
    import io.swagger.models.auth.In;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 商品分类表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class GoodsSort extends BaseEntity {

    private static final long serialVersionUID = 1L;

            /**
            * 商品分类名称
            */
    private String goodsSortName;

            /**
            * 商品分类别名
            */
    private String goodsSortAlias;

            /**
            * 商品分类描述
            */
    private String goodsSortDesc;

            /**
            * 商品分类是否显示
            * 1:显示,0不显示,3显示所有
            */
    private Integer isShow;

            /**
            * 商品分类排序
            */
    private Integer goodsSortRank;

            /**
            * 删除标记
            */
    private Integer delFlag;


}
