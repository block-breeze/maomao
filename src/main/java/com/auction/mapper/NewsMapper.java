package com.auction.mapper;

import com.auction.dto.NewsDTO;
import com.auction.dto.WebNewsDTO;
import com.auction.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NewsMapper extends BaseMapper<News>{
    //查询新闻表总数
    Long selectNewsCount(Map<Object, Object> map);
    //查询新闻表
    List<NewsDTO> selectNewsList(Map<Object, Object> map);

    NewsDTO getNewsById(Long id);

    List<WebNewsDTO> getWebNews(Map<String, Object> map);
    List<WebNewsDTO>  getWebNewsList(@Param("start") int start, @Param("count") int count);
    int getWebNewsCount();


}
