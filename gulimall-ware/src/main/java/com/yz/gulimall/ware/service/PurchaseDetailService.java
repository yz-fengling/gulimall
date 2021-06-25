package com.yz.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yz.common.utils.PageUtils;
import com.yz.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 15:24:09
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

