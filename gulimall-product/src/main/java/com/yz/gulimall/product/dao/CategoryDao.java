package com.yz.gulimall.product.dao;

import com.yz.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 13:44:28
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
