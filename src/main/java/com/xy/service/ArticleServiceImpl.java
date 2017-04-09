package com.xy.service;

import com.alibaba.fastjson.JSON;
import com.xy.dao.ArticleDaoImpl;
import com.xy.dao.IArticleDao;
import com.xy.vo.Article;
import com.xy.vo.PageBean;
import com.xy.vo.ReArticle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MOMO on 2016/10/22.
 */
public class ArticleServiceImpl implements IArticleService {
    private IArticleDao dao=new ArticleDaoImpl();
    @Override
    public PageBean QueryAll(int curPage, int userid) {
        return dao.QueryAll(curPage,userid);
    }

    @Override
    public boolean addArticle(Article a) {
        return dao.addArticle(a);
    }

    @Override
    public boolean delArrticle(int id) {
        return dao.delArrticle(id);
    }

    @Override
    public String queryReply(int id) {
        ReArticle re=dao.queryReply(id);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("title",re.getTitle());
        map.put("list",re.getArticle());
        return JSON.toJSONString(map,true);
    }
}
