package com.yz.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yz.common.to.SkuReductionTo;
import com.yz.common.utils.PageUtils;
import com.yz.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 15:14:48
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void savSkuReduction(SkuReductionTo skuReductionTo);
}

