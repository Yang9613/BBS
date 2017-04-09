package com.xy.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MOMO on 2017/1/25.
 */
public class ReArticle implements Serializable {
    private String title;
    private List<Article> article;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Article> getArticle() {
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }
}
