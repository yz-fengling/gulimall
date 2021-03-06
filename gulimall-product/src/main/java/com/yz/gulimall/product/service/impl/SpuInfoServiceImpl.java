package com.yz.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.yz.common.constant.ProductConstant;
import com.yz.common.to.SkuHasStockVo;
import com.yz.common.to.SkuReductionTo;
import com.yz.common.to.SpuBoundTo;
import com.yz.common.to.es.SkuEsModel;
import com.yz.common.utils.R;
import com.yz.gulimall.product.entity.*;
import com.yz.gulimall.product.feign.CouponFeignService;
import com.yz.gulimall.product.feign.SearchFeignService;
import com.yz.gulimall.product.feign.WareFeginServic;
import com.yz.gulimall.product.service.*;
import com.yz.gulimall.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yz.common.utils.PageUtils;
import com.yz.common.utils.Query;

import com.yz.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService descService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    
    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WareFeginServic wareFeginServic;

    @Autowired
    private SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * TODO ?????????????????????
     * @param vos
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vos) {
        //1.??????spu???????????? pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vos,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBasespuInfo(spuInfoEntity);

        //2.??????spu??????????????? pms_spu_info_desc
        List<String> decript = vos.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        descService.saveSpuinfoDesc(spuInfoDescEntity);

        //3.??????spu???????????? pms_spu_images
        List<String> images = vos.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);

        //4.??????spu??????????????? `pms_product_attr_value
        List<BaseAttrs> baseAttrs = vos.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity attrValueEntity = new ProductAttrValueEntity();
            attrValueEntity.setAttrId(attr.getAttrId());
            AttrEntity id = attrService.getById(attr.getAttrId());
            attrValueEntity.setAttrName(id.getAttrName());
            attrValueEntity.setAttrValue(attr.getAttrValues());
            attrValueEntity.setQuickShow(attr.getShowDesc());
            attrValueEntity.setSpuId(spuInfoEntity.getId());
            return attrValueEntity;
        }).collect(Collectors.toList());

        attrValueService.saveProductAttr(collect);

        //5.??????spu???????????????gulimall_sms->sms_spu_bounds
        Bounds bounds = vos.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if(r.getCode()!=0 ){
            log.error("????????????spu??????????????????");
        }

        //6.????????????spu????????????sku??????
        List<Skus> skus = vos.getSkus();
        if(skus !=null && skus.size()>0){
            //6.1 ??????sku??????????????? pms_sku_info
            skus.forEach(item->{
                String defaultImg = "";
                for (Images image: item.getImages()){
                    if(image.getDefaultImg() == 1){
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                //6.2 ??????sku??????????????? pms_sku_images
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    //??????true???????????????false????????????
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //TODO ???????????????????????????????????????????????????
                skuImagesService.saveBatch(imagesEntities);

                //6.3 ??????sku????????????????????? pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> saleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity saleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, saleAttrValueEntity);
                    saleAttrValueEntity.setSkuId(skuId);
                    return saleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(saleAttrValueEntities);

                //6.4 ??????sku????????? ??????????????? gulimall_sms->sms_sku_ladder sms_sku_full_reduction sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode()!=0 ){
                        log.error("????????????sku??????????????????");
                    }
                }

            });
        }


    }

    @Override
    public void saveBasespuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((w->{
               w.eq("id",key).or().like("spu_name",key);
            }));

        }
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            queryWrapper.eq("brand_id",brandId);
        }
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)&&!"0".equalsIgnoreCase(status)){
            queryWrapper.eq("publish_status",status);
        }
        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            queryWrapper.eq("catalog_id",catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
        
        
    }

    /**
     * ????????????
     * @param spuId
     */
    @Transactional
    @Override
    public void spuInfoUp(Long spuId) {
        //?????????????????????

        //TODO stream
        List<SkuInfoEntity> skusInfo = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skusInfo.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        //TODO ????????????sku?????????????????????????????????????????????
        List<ProductAttrValueEntity> baseAttrs = attrValueService.baseAttrlistforspu(spuId);

        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        Set<Long> idSet= new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            //TODO contains???????????????????????????
            System.out.println(idSet.contains(item.getAttrId()));
            return idSet.contains(item.getAttrId());
        }).map(item->{
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item,attrs);
            return attrs;
        }).collect(Collectors.toList());

        //TODO hasStock ?????????????????????????????????????????????????????? gulimall-ware
        Map<Long, Boolean> skuMap =null;
        try {
            R r = wareFeginServic.getSkusHasStock(skuIdList);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {
            };
            skuMap = r.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        }catch (Exception e){
            log.error("????????????????????????????????????",e);
        }

        //1.????????????spuid?????????sku????????????????????????
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
        //2.????????????sku?????????
        Map<Long, Boolean> finalSkuMap = skuMap;
        List<SkuEsModel> skuEsModelList = skus.stream().map(sku -> {
            //?????????????????????
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, esModel);
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());

            //??????????????????
            if (finalSkuMap == null) {
                esModel.setHasStock(true);
            } else {
                esModel.setHasStock(finalSkuMap.get(sku.getSkuId()));
            }

            //TODO  hotScore ???????????????
            esModel.setHotScore(0L);

            //TODO ????????????????????????????????????
            BrandEntity brand = brandService.getById(sku.getBrandId());
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());

            CategoryEntity category = categoryService.getById(sku.getCatalogId());
            esModel.setCatalogName(category.getName());

            //??????????????????
            esModel.setAttrs(attrsList);

            return esModel;
        }).collect(Collectors.toList());


        //TODO ??????????????????es???????????????gulimall-search
        R r = searchFeignService.productStatusUp(skuEsModelList);
        if(r.getCode() == 0){
            //??????????????????
            //TODO ????????????spu?????????
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else {
            //??????????????????
            //TODO ???????????? ??? ??????????????? ??????????????????
        }


    }


}