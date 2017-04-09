package com.xy.service;

import com.xy.vo.Article;
import com.xy.vo.PageBean;

import java.util.List;

/**
 * Created by MOMO on 2016/10/22.
 */
public interface IArticleService {
    public PageBean QueryAll(int curPage, int userid);
    public boolean addArticle(Article a);
    public boolean delArrticle(int id);
    public String queryReply(int id);
}
