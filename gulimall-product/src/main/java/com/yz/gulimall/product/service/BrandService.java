package com.yz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yz.common.utils.PageUtils;
import com.yz.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 13:44:28
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

