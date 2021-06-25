package com.yz.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.yz.gulimall.product.entity.ProductAttrValueEntity;
import com.yz.gulimall.product.service.ProductAttrValueService;
import com.yz.gulimall.product.vo.AttrRespVo;
import com.yz.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yz.gulimall.product.entity.AttrEntity;
import com.yz.gulimall.product.service.AttrService;
import com.yz.common.utils.PageUtils;
import com.yz.common.utils.R;



/**
 * 商品属性
 *
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-19 15:08:54
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService attrValueService;



    // /product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttr(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> list = attrValueService.baseAttr(spuId);
        return R.ok().put("data",list);
    }

    ///product/attr/base/list/{catelogId}
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId
    ,@PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,attrType);
        return R.ok().put("page",page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);

		AttrRespVo respVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     *
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
//		attrService.updateById(attr);

        attrService.updateAttr(attr);
        return R.ok();
    }
    // /product/attr/update/{spuId}
    @RequestMapping("/update/{spuId}")
    //@RequiresPermissions("product:attr:update")
    public R updateSpuAttr(@PathVariable("spuId")Long spuId,@RequestBody List<ProductAttrValueEntity> entities){
        attrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
