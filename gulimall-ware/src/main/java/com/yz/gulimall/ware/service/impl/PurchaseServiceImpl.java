package com.yz.gulimall.ware.service.impl;

import com.yz.common.constant.WareConstant;
import com.yz.gulimall.ware.entity.PurchaseDetailEntity;
import com.yz.gulimall.ware.service.PurchaseDetailService;
import com.yz.gulimall.ware.service.WareSkuService;
import com.yz.gulimall.ware.vo.MergeVo;
import com.yz.gulimall.ware.vo.PurchaseDoneVo;
import com.yz.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yz.common.utils.PageUtils;
import com.yz.common.utils.Query;

import com.yz.gulimall.ware.dao.PurchaseDao;
import com.yz.gulimall.ware.entity.PurchaseEntity;
import com.yz.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService detailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status",0).or().eq("status",1);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId == null){
            //1.新建一个
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        //TODO 确认采购单状态是0，1才可以合并
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
        detailService.updateBatchById(collect);
        PurchaseEntity entity = new PurchaseEntity();
        entity.setId(purchaseId);
        entity.setUpdateTime(new Date());
        this.updateById(entity);
    }

    @Override
    public void receivedIds(List<Long> ids) {
        //确认当前采购单是新建或已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item->{
            if(item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()||
            item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()){
                return true;
            }
            return false;
        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());
        //改变采购单状态
        this.updateBatchById(collect);

        collect.forEach((item)->{
            List<PurchaseDetailEntity> entities = detailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> collect1 = entities.stream().map(entity -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(entity.getId());
                detailEntity.setStatus(WareConstant.PurchaseDetailEnum.RECEIVE.getCode());
                return detailEntity;
            }).collect(Collectors.toList());
            detailService.updateBatchById(collect1);
        });
    }

    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {
        //1.改变采购单的状态
        Long id = purchaseDoneVo.getId();


        //2.改变采购项的状态
        Boolean flag= true;
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        for (PurchaseItemDoneVo item:items ){
            //TODO 有待完善
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if(item.getStatus() == WareConstant.PurchaseDetailEnum.HASERROR.getCode()){
                flag=false;
                detailEntity.setStatus(item.getStatus());
            }else {
                detailEntity.setStatus(WareConstant.PurchaseDetailEnum.FINISH.getCode());
                //3.将成功采购的进行入库
                PurchaseDetailEntity byId = detailService.getById(item.getItemId());
                wareSkuService.addStock(byId.getSkuId(),byId.getWareId(),byId.getSkuNum());
            }
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);

        }
        detailService.updateBatchById(updates);
//        改变采购单的状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode():WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);




    }

}