package com.yz.gulimall.member.dao;

import com.yz.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhengyang
 * @email zhengyang@gmail.com
 * @date 2021-06-10 15:19:18
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
