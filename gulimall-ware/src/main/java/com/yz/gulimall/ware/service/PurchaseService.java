package com.yz.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yz.common.utils.PageUtils;
import com.yz.gulimall.ware.entity.PurchaseEntity;
import com.yz.gulimall.ware.vo.MergeVo;
import com.yz.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 15:24:09
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void receivedIds(List<Long> ids);

    void done(PurchaseDoneVo purchaseDoneVo);
}

