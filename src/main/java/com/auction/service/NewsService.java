package com.auction.service;

import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.NewsDTO;
import com.auction.dto.NoticeDTO;
import com.auction.dto.UserDTO;

public interface NewsService {
    //新闻列表
    public Page<NewsDTO> newsList(NewsDTO dto, PageSearch page) throws Exception;
    //新增新闻
    public void add(NewsDTO dto) throws Exception;
    //修改新闻
    public void modify(NewsDTO dto) throws Exception;
    //根据id删除新闻
    public void delete(Long id) throws Exception;
    //新闻详情接口
    NewsDTO selectById (Long id) throws Exception;
    //新闻的发布
    public void publishById (Long id) throws Exception;

    //新闻的提交
    public void submitById (Long id) throws Exception;

    //新闻撤销
    public void reject (NewsDTO dto) throws Exception;

    //新闻浏览量
    public void newsReadAmount(Long id) throws Exception;
}
