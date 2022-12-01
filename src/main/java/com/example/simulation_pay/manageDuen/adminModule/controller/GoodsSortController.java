package com.example.simulation_pay.manageDuen.adminModule.controller;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.simulation_pay.common.baseEntity.BaseEntity;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.config.BaseList;
import com.example.simulation_pay.manageDuen.adminModule.entity.GoodsSort;
import com.example.simulation_pay.manageDuen.adminModule.service.IGoodsSortService;
import com.mysql.cj.QueryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@RestController
@RequestMapping("api/adminModule/goods-sort")
public class GoodsSortController {

    @Autowired
    private IGoodsSortService goodsSortService;


    @ApiOperation("查询商品分类列表")
    @PostMapping("getGoodsSortList")
    public CommonResult getGoodsSortList(@RequestBody BaseEntity baseEntity){
        Page<GoodsSort> page = new Page<>(baseEntity.getCurrentPage(),baseEntity.getPageSize());
        QueryWrapper<GoodsSort> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("del_flag",1);
        IPage<GoodsSort>  goodsIPage = goodsSortService.page(page, wrapper2);
        return CommonResult.success(goodsIPage,goodsIPage.getRecords().size()>0?"查询到以下商品分类":"未查询到商品分类信息");
    }

    @ApiOperation("添加后台商品分类")
    @PostMapping("saveGoodsSort")
    public CommonResult saveGoodsSort(@RequestBody GoodsSort goodsSort){
        if (goodsSort.getGoodsSortName() == null||goodsSort.getGoodsSortName().equals("")
                || goodsSort.getGoodsSortAlias() == null||goodsSort.getGoodsSortAlias().equals("")
                || goodsSort.getIsShow() == null
                || goodsSort.getGoodsSortRank() == null
                || goodsSort.getGoodsSortDesc()==null||goodsSort.getGoodsSortDesc().equals("")){
            return CommonResult.failed("后台商品信息缺失,请补充完整商品分类信息后添加.");
        }
        GoodsSort one = goodsSortService.getOne(new QueryWrapper<GoodsSort>()
                .eq("goods_sort_name", goodsSort.getGoodsSortName()).eq("del_flag", 1));
        if (one != null){
            return CommonResult.failed("商品分类已经添加，请别重复添加");
        }
        GoodsSort sort = new GoodsSort();
        sort.setGoodsSortName(goodsSort.getGoodsSortName());
        sort.setGoodsSortAlias(goodsSort.getGoodsSortAlias());
        sort.setIsShow(goodsSort.getIsShow());
        sort.setGoodsSortRank(goodsSort.getGoodsSortRank());
        sort.setGoodsSortDesc(goodsSort.getGoodsSortDesc());
        boolean save = goodsSortService.save(sort);
        if (!save){
            return CommonResult.failed("后台商品分类添加失败");
        }
        return CommonResult.success(save,"后台商品分类成功.");
    }

    @ApiOperation("编辑商品分类")
    @PostMapping("updateGoodsSort")
    public CommonResult updateGoodsSort(@RequestBody GoodsSort goodsSort){
        if (goodsSort.getGoodsSortName() == null||goodsSort.getGoodsSortName().equals("")
                || goodsSort.getGoodsSortAlias() == null||goodsSort.getGoodsSortAlias().equals("")
                || goodsSort.getIsShow() == null
                || goodsSort.getGoodsSortRank() == null
                || goodsSort.getGoodsSortDesc()==null||goodsSort.getGoodsSortDesc().equals("")){
            return CommonResult.failed("后台商品信息缺失,请补充完商品分类的必填项.");
        }
        QueryWrapper<GoodsSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_sort_name",goodsSort.getGoodsSortName())
                .eq("del_flag","1");
        GoodsSort one1 = goodsSortService.getOne(queryWrapper);
        if (one1!=null){
            if (one1.getId()!=goodsSort.getId()){
                return CommonResult.failed("当前商品分类名称已经存在");
            }
        }
        QueryWrapper<GoodsSort> wrapper = new QueryWrapper<>();
        wrapper.eq("id", goodsSort.getId())
                .eq("del_flag", 1);
        GoodsSort one = goodsSortService.getOne(wrapper);
        if (one == null){
            return CommonResult.failed("修改失败,商品分类不存在.");
        }
        boolean update = goodsSortService.update(goodsSort, wrapper);
        return CommonResult.success(update,"商品分类编辑成功");
    }

    @ApiOperation("删除后台商品分类")
    @PostMapping("deleteGoodsSort")
    public CommonResult deleteGoodsSort(@RequestBody BaseList baseList){
        if (baseList==null||baseList.namesOrIds.size()==0){
            return CommonResult.failed("请至少选择一样商品分类进行删除.");
        }
        for (int i = 0; i < baseList.namesOrIds.size(); i++) {
            QueryWrapper<GoodsSort> wrapper = new QueryWrapper<>();
            wrapper.eq("id",baseList.namesOrIds.get(i)).eq("del_flag",1);
            GoodsSort one = goodsSortService.getOne(wrapper);
            if (one==null){
                return CommonResult.failed("该商品分类已经被删除.");
            }
            one.setDelFlag(0);
            boolean update = goodsSortService.update(one, wrapper);
            if (!update){
                return CommonResult.failed("删除商品分类失败");
            }
        }
        return CommonResult.success("后台商品分类删除成功");
    }

    @ApiOperation("条件查询商品分类")
    @PostMapping("QueryGoodsSortBy")
    public CommonResult QueryGoodsSortBy(@RequestBody GoodsSort goodsSort){
        System.out.println("条件查询商品分类");
        QueryWrapper<GoodsSort> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",1);
        if (goodsSort.getGoodsSortName()!=null){
            wrapper.like("goods_sort_name",goodsSort.getGoodsSortName());
        }
        if (goodsSort.getGoodsSortAlias()!=null){
            wrapper.like("goods_sort_alias",goodsSort.getGoodsSortAlias());
        }
        if (goodsSort.getGoodsSortDesc()!=null){
            wrapper.like("goods_sort_desc",goodsSort.getGoodsSortDesc());
        }
        if (goodsSort.getIsShow()!=null){
            if (goodsSort.getIsShow()==3){

            }else {
                wrapper.eq("is_show",goodsSort.getIsShow());
            }
        }
        List<GoodsSort> list = goodsSortService.list(wrapper);
        return CommonResult.success(list,"查询到以下商品分类");
    }
}


























































