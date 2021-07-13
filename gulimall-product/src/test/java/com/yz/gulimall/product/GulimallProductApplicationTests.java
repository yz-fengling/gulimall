package com.yz.gulimall.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.yz.gulimall.product.entity.BrandEntity;
import com.yz.gulimall.product.service.AttrGroupService;
import com.yz.gulimall.product.service.BrandService;
import com.yz.gulimall.product.service.SkuSaleAttrValueService;
import com.yz.gulimall.product.vo.SkuItemSaleAttrVo;
import com.yz.gulimall.product.vo.SpuItemAttrGroupVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Test
    public void test1(){
        List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueService.getSaleAttrsBySpuId(7L);
        System.out.println(saleAttrsBySpuId);
//        List<SpuItemAttrGroupVo> spuId = attrGroupService.getAttrGroupWithAttrsBySpuId(7L, 225L);
//        System.out.println(spuId);
    }

    @Test
    public void rediss(){
        System.out.println(redissonClient);
    }

    @Test
    public void testUpload() throws FileNotFoundException {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tL47i4wo3osGwRjGNN1";
        String accessKeySecret = "FEKX5kgYivT9np6fpT2ZZjBrTviTn5";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = new FileInputStream("E:\\学习视频\\java\\4.尚硅谷全套java教程--实战项目\\大型电商--谷粒商城\\1.分布式基础（全栈开发篇）\\课件和文档\\基础篇\\资料\\pics\\0d40c24b264aa511.jpg");
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject("gulimall-fengling", "0d40c24b264aa511.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传完成");
    }

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("华为");
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功");

    }

}
