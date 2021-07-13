package com.yz.gulimall.search.service;

import com.yz.gulimall.search.vo.SearchParam;
import com.yz.gulimall.search.vo.SearchResult;

public interface MallSearchService {
    /**
     *
     * @param param 检索所有参数
     * @return 返回检索的结果
     */
    SearchResult search(SearchParam param);
}
