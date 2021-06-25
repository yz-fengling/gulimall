package com.yz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yz.common.utils.PageUtils;
import com.yz.gulimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 13:44:28
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuinfoDesc(SpuInfoDescEntity spuInfoDescEntity);
}

