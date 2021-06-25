package com.yz.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yz.common.utils.PageUtils;
import com.yz.gulimall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 15:24:09
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);
}

