package com.example.simulation_pay.manageDuen.adminModule.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.simulation_pay.common.baseEntity.BaseEntity;
import com.example.simulation_pay.common.result.CommonResult;
import com.example.simulation_pay.common.util.FileUpload;
import com.example.simulation_pay.config.BaseList;
import com.example.simulation_pay.config.JwtConfig;
import com.example.simulation_pay.teacherDuen.goodsModule.entity.Goods;
import com.example.simulation_pay.teacherDuen.goodsModule.service.IGoodsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author 龚某鑫
 * @since 2021-07-02
 */
@RestController
@RequestMapping("api/adminModule/goods")
public class GoodsAdminController {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private JwtConfig jwtConfig;

    @ApiOperation("查询后台商品列表")
    @PostMapping("getGoodsList")
    public CommonResult getGoodsList(HttpServletRequest request, @RequestBody BaseEntity baseEntity){
        String uuid=jwtConfig.getUsernameFromToken(request.getHeader("token"));
        if (uuid == null){
            return CommonResult.failed("您还没登录，请重新登录");
        }
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "1");
        Page<Goods> page = new Page<>(baseEntity.getCurrentPage(),baseEntity.getPageSize());
        IPage<Goods>  goodsIPage = goodsService.page(page, wrapper);
       if (goodsIPage.getRecords().size()>0){
           for (int i = 0; i < goodsIPage.getRecords().size(); i++) {
               System.out.println("图片:"+goodsIPage.getRecords().get(i).getGoodsPicture());
//            System.out.println("图片2:"+JSON.parseArray(goodsIPage.getRecords().get(i).getGoodsPicture()));
               goodsIPage.getRecords().get(i).setGoodsPicture2(JSONArray.parseArray(goodsIPage.getRecords().get(i).getGoodsPicture(),String.class));
           }
       }
        return CommonResult.success(goodsIPage,goodsIPage.getRecords().size()>0?"查询到以下商品":"未查询到商品");
    }

    @ApiOperation("上传商品图片")
    @PostMapping("uploadGoodsPic")
    public CommonResult UploadGoodsPic( MultipartFile file){
        FileUpload upload = new FileUpload();
        String s = upload.UploadFile(file);
        return CommonResult.success(s,"商品图片上传成功.");
    }

    @ApiOperation("上传商品图片")
    @PostMapping("uploadGoodsPics")
    public CommonResult UploadGoodsPics(@RequestParam MultipartFile[]file){
        List<String>files=new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            FileUpload upload = new FileUpload();
            String s = upload.UploadFile(file[i]);
            files.add(s);
        }
        return CommonResult.success(JSON.toJSON(files),"商品图片上传成功.");
    }

    @ApiOperation("添加后台商品")
    @PostMapping("saveGoods")
    public CommonResult saveGoods(@RequestBody Goods goods){
        System.err.println(goods);
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_name",goods.getGoodsName())
                .eq("goods_sort",goods.getGoodsSort())
                .eq("goods_barcode",goods.getGoodsBarcode())
                .eq("del_flag","1");
        Goods one = goodsService.getOne(wrapper);
        if (one!=null){
            return CommonResult.failed("商品重复添加.");
        }
        boolean save = false;
        if (goods.getGoodsName()==null
                ||goods.getGoodsPrice()==null
                ||goods.getGoodsSort()==null
                ||goods.getGoodsStore()==null
                ||goods.getGoodsBarcode()==null)
        {
            return CommonResult.failed("商品信息缺失,请补充完整后添加商品.");

        }
        if (goods.getGoodsPicture2()==null){
            return CommonResult.failed("请至少添加一张商品图片");
        }
            Goods goods1 = new Goods();
            goods1.setGoodsName(goods.getGoodsName()); // 商品名称
            goods1.setGoodsSort(goods.getGoodsSort()); // 商品分类
            goods1.setGoodsPicture(JSON.toJSONString(goods.getGoodsPicture2()));
            goods1.setGoodsStore(goods.getGoodsStore()); // 商品库存
            goods1.setGoodsPrice(goods.getGoodsPrice()); // 商品价格
            goods1.setCreateTime(DateUtil.date());//入库时间
            goods1.setGoodsBarcode(goods.getGoodsBarcode());
            goods1.setRemark(goods.getRemark());//商品备注
            save = goodsService.save(goods1);
        if (!save){
            return CommonResult.failed("商品添加失败");
        }
        return CommonResult.success(save,"商品添加成功");
    }

    @ApiOperation("编辑后台商品")
    @PostMapping("updateGoods")
    public CommonResult updateGoods(@RequestBody Goods goods){
        System.out.println("商品图片A:"+goods);
        System.out.println("商品图片B:"+goods.getGoodsPicture());
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("id",goods.getId()).eq("del_flag","1");
        Goods one = goodsService.getOne(wrapper);
        if (one==null){
            return CommonResult.failed("未查询到该商品.");
        }
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_sort",goods.getGoodsSort()).eq("goods_name",goods.getGoodsName()).eq("del_flag","1");
        Goods one1 = goodsService.getOne(queryWrapper);
        if (one1!=null){
            if (one1.getId()!=goods.getId()){
                return CommonResult.failed("该商品分类下已存在该商品");
            }
        }
        goods.setCreateTime(DateUtil.date());
        if (goods.getGoodsPicture2()!=null){
            System.out.println("修改图片");
            goods.setGoodsPicture(JSON.toJSONString(goods.getGoodsPicture2()));
        }
        boolean update = goodsService.update(goods,wrapper);
        if (!update){
            return CommonResult.failed("修改商品失败");
        }
        QueryWrapper<Goods> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("id",goods.getId()).eq("del_flag","1");
        Goods one2 = goodsService.getOne(wrapper);
        if (one2==null){
            return CommonResult.failed("修改完商品后返回商品信息出错.");
        }
        return CommonResult.success(one2,"修改商品信息成功");
    }

    @ApiOperation("删除后台商品")
    @PostMapping("deleteGoods")
    public CommonResult deleteGoods(@RequestBody BaseList baseList){
        if (baseList.namesOrIds.isEmpty()){
            return CommonResult.failed("没有要删除的商品");
        }
        for (int i = 0; i < baseList.namesOrIds.size(); i++) {
            QueryWrapper<Goods> wrapper = new QueryWrapper<>();
            wrapper.eq("id",baseList.namesOrIds.get(i));
            Goods one = goodsService.getOne(wrapper);
            if (one.getDelFlag()==0){
                return CommonResult.failed("该商品已被删除");
            }
            one.setDelFlag(0);
            boolean update = goodsService.update(one,wrapper);
            if (!update){
                return CommonResult.failed("删除商品[ "+baseList.namesOrIds.get(i)+" ]时出现错误");
            }
        }
        return CommonResult.success("删除成功.");
    }

    @ApiOperation("条件查询商品列表")
    @PostMapping("QueryGoodsBy")
    public CommonResult QueryGoodsBy(@RequestBody Goods goods){
        System.out.println("条件查询商品");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag","1");
        if (goods.getGoodsBarcode()!=null){
            wrapper.like("goods_barcode",goods.getGoodsBarcode());
        }
        if (goods.getGoodsName()!=null){
            wrapper.like("goods_name",goods.getGoodsName());
        }
        if (goods.getGoodsSort()!=null){
            wrapper.like("goods_sort",goods.getGoodsSort());
        }
        List<Goods> list = goodsService.list(wrapper);
        return CommonResult.success(list,"查询到以下商品");
    }
}







































