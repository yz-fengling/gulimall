package com.yz.gulimall.product.service.impl;

import com.yz.gulimall.product.entity.AttrEntity;
import com.yz.gulimall.product.service.AttrService;
import com.yz.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yz.common.utils.PageUtils;
import com.yz.common.utils.Query;

import com.yz.gulimall.product.dao.AttrGroupDao;
import com.yz.gulimall.product.entity.AttrGroupEntity;
import com.yz.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((obj)->{
               obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }

        if(catelogId==0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper);
            return new PageUtils(page);
        }else {
            queryWrapper.eq("catelog_id",catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper);
            return new PageUtils(page);
        }

    }

    /**
     * 根据分类查出所有的分组以及这些组里面的信息
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long catelogId) {
        //1.查询分组信息
        List<AttrGroupEntity> catelog_id = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        //2.查询所有属性
        List<AttrGroupWithAttrsVo> attrsVos = catelog_id.stream().map(group -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group,attrGroupWithAttrsVo);
            List<AttrEntity> attr = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attr);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());
        return attrsVos;
    }

}