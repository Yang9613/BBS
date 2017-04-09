package com.xy.dao;

import com.xy.vo.Article;
import com.xy.vo.PageBean;
import com.xy.vo.ReArticle;

import java.util.List;

/**
 * Created by MOMO on 2016/10/21.
 */
public interface IArticleDao {
    public PageBean QueryAll(int curPage,int userid);
    public boolean addArticle(Article a);
    public boolean delArrticle(int id);
    public ReArticle queryReply(int id);
}
