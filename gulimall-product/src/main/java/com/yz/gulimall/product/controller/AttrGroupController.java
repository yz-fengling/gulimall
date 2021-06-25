package com.yz.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.yz.gulimall.product.entity.AttrEntity;
import com.yz.gulimall.product.service.AttrAttrgroupRelationService;
import com.yz.gulimall.product.service.AttrService;
import com.yz.gulimall.product.service.CategoryService;
import com.yz.gulimall.product.vo.AttrGroupRelationVo;
import com.yz.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yz.gulimall.product.entity.AttrGroupEntity;
import com.yz.gulimall.product.service.AttrGroupService;
import com.yz.common.utils.PageUtils;
import com.yz.common.utils.R;



/**
 * 属性分组
 *
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 14:18:28
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * /product/attrgroup/{catelogId}/withattr
     * 获取分类下所有分组&关联属性
     * @param vos
     * @return
     */
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId")Long catelogId){
        //1.查出当前分类下的所有分组
        //2.查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVo = attrGroupService.getAttrGroupWithAttrs(catelogId);
        return R.ok().put("data",attrGroupWithAttrsVo);
    }


    //http://localhost:88/api/product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        attrAttrgroupRelationService.saveBatch(vos);
        return R.ok();
    }

    // /product/attrgroup/attr/relation/delete
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }

    // /product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> attrEntities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",attrEntities);
    }
    //   /product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils pageUtils = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",pageUtils);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
