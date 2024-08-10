package com.auction.service.impl;

import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.NewsDTO;
import com.auction.entity.News;
import com.auction.mapper.NewsMapper;
import com.auction.service.NewsService;
import com.auction.service.SearchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    SearchService searchService;

    @Override
    public Page<NewsDTO> newsList(NewsDTO search, PageSearch page) throws Exception {
        Map<Object, Object> map = new HashMap<>();

        map.put("title", search.getTitle());
        map.put("state", search.getState());
        map.put("publishBy", search.getPublishBy());
        map.put("module", search.getModule());

        int start = page.getStart() * page.getCount();
        map.put("sortorder", page.getSortorder());
        map.put("sortname", page.getSortname());
        map.put("start", start);
        map.put("count", page.getCount());
        Page<NewsDTO> result = new Page<NewsDTO>();
        int pageSize = page.getCount() < 1 ? 10 : page.getCount();
        int pageIndex = page.getStart() < 0 ? 0 : start / pageSize;
        Long totalCount = searchService.selectNewsCount(map);
        List<NewsDTO> list = searchService.selectNewsList(map);
        /*List<NewsDTO> dtoList = this.convertDTO(list);*/

        result.setCount(page.getCount());
        result.setPage(pageIndex + 1);
        result.setTotalCount(totalCount);
        result.setTotalPages(totalCount.intValue() / pageSize + 1);
        result.setItems(list);
        return result;
    }
/*
    private List<NewsDTO> convertDTO(List<NewsDTO> list) {
        List<NewsDTO> dtoList = new ArrayList<>();
        for (NewsDTO newsDTO : list) {
            NewsDTO dto = new NewsDTO();
            dto.setId(newsDTO.getId());
            dto.setTitle(newsDTO.getTitle());
            dto.setModule(newsDTO.getModule());
            dto.setPublishBy(newsDTO.getPublishBy());
            dto.setPublishTime(newsDTO.getPublishTime());
            dto.setCreateTime(newsDTO.getCreateTime());
            dto.setState(newsDTO.getState());
            dto.setReadAmount(newsDTO.getReadAmount());
            dto.setReject(newsDTO.getReject());
            dto.setContent(newsDTO.getContent());
            dtoList.add(dto);
        }
        return dtoList;
    }*/

    @Override
    public void add(NewsDTO dto) throws Exception {
        logger.info("新闻管理——新增");
        if (null == dto) {
            throw new Exception("前台传递的数据异常");
        }
        //判断数据不为空
        if (null == dto.getTitle() || null == dto.getModule() || null == dto.getContent() || null == dto.getPublishBy()) {
            throw new Exception("必填参数不得为空");
        }
        News news = new News();
        news.setTitle(dto.getTitle());
        news.setModule(dto.getModule());
        news.setContent(dto.getContent());
        news.setPublishBy(dto.getPublishBy());
        news.setCreateTime(new Date());
        news.setState(0);
        this.save(news);
        logger.info("新增id为：" + news.getId());
    }

    @Override
    public void modify(NewsDTO dto) throws Exception {
        logger.info("新闻管理——修改");
        if (null == dto || null == dto.getId()) {
            throw new Exception("数据异常！");
        }
        //判断数据不为空
        if (null == dto.getTitle() || null == dto.getModule() || null == dto.getContent() || null == dto.getPublishBy()) {
            throw new Exception("必填参数不得为空");
        }
        News news = newsMapper.selectById(dto.getId());
        if (dto.getState().equals(0) || dto.getState().equals(2)  ) {
            news.setTitle(dto.getTitle());
            news.setModule(dto.getModule());
            news.setContent(dto.getContent());
            news.setPublishBy(dto.getPublishBy());
            news.setUpdateTime(new Date());
            this.newsMapper.updateById(news);
        } else {
            throw new Exception("审核当中的新闻不可修改");
        }
        logger.info("修改id为：" + news.getId());
    }

    @Override
    public void delete(Long id) throws Exception {
        logger.info("新闻管理——删除");
        if (null == id) {
            throw new Exception("数据异常！");
        }
        News news = newsMapper.selectById(id);
        if (null == news) {
            throw new Exception("此信息不存在！");
        }
        if (news.getState().equals(0)) {
            this.newsMapper.deleteById(id);
        } else {
            throw new Exception("审核当中的新闻不可删除");
        }


        logger.info("删除id为：" + news.getId());
    }

    @Override
    public NewsDTO selectById(Long id) throws Exception {
        logger.info("新闻管理——查看详情");

        NewsDTO newsDetail = newsMapper.getNewsById(id);
        if (newsDetail == null) {
            throw new Exception("没有找到对应id的新闻");
        }
        return newsDetail;

    }

    @Override
    public void publishById(Long id) throws Exception {
        logger.info("新闻管理——发布");

        News news = newsMapper.selectById(id);
        if (news == null) {
            throw new Exception("没有找到对应id的新闻");
        }
        if (news.getState() == 1) {
            throw new Exception("该新闻资讯已经发布，请不要重复发布");
        }
        news.setState(1);
        news.setPublishTime(new Date());
        this.updateById(news);
        logger.info("新闻管理——发布,发布id为：" + id);
    }

    @Override
    public void submitById(Long id) throws Exception {
        logger.info("新闻管理——提交");

        News news = newsMapper.selectById(id);
        if (news == null) {
            throw new Exception("没有找到对应id的新闻");
        }
        if (news.getState() == 3) {
            throw new Exception("该新闻资讯已提交，等待审核中，请不要重复提交");
        }
        news.setState(3);
        news.setUpdateTime(new Date());
        this.updateById(news);
        logger.info("新闻管理——提交,提交审核的新闻id为：" + id);
    }

    @Override
    public void reject(NewsDTO dto) throws Exception {
        logger.info("新闻管理——驳回");
        if (null == dto || null == dto.getId()) {
            throw new Exception("数据异常！");
        }
        News news = newsMapper.selectById(dto.getId());
        if (news == null) {
            throw new Exception("没有找到对应id的新闻");
        }
        if (dto.getState() == 1) {
            news.setState(2);
            news.setReject(dto.getReject());
            news.setPublishTime(null);
            news.setUpdateTime(new Date());
        } else if (dto.getState() == 3) {
            news.setState(2);
            news.setReject(dto.getReject());
            news.setUpdateTime(new Date());
        } else {
            throw new Exception("所选新闻状态不是已发布和待审核，不可驳回！");
        }
        this.updateById(news);
        logger.info("新闻管理——驳回，驳回新闻的id为：" + dto.getId());
    }

    @Override
    public void newsReadAmount(Long id) throws Exception {
        News news = this.newsMapper.selectById(id);
        if (null != news) {
            Long readAmount = news.getReadAmount();
            if (null == readAmount) {
                news.setReadAmount(1L);
            } else {
                readAmount += 1;
                news.setReadAmount(readAmount);
            }
            this.updateById(news);
        }
        logger.info("新闻管理——浏览人数+1,id为：" + id);
    }


}
