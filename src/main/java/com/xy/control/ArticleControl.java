package com.xy.control;

import com.xy.service.ArticleServiceImpl;
import com.xy.service.IArticleService;
import com.xy.vo.Article;
import com.xy.vo.BBSUser;
import com.xy.vo.PageBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by MOMO on 2016/10/22.
 */
@WebServlet(name = "ArticleControl",urlPatterns = {"/article"})
public class ArticleControl extends HttpServlet {
    private IArticleService service=new ArticleServiceImpl();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action=request.getParameter("action");

        switch (action){
            case "page":
                page(request, response);
                break;
            case "addz":
                addz(request, response);
                break;
            case "reply"://回复从贴

                addz(request, response);
                break;
            case "delz":
                del(request, response);
                break;
            case "queryid":
                String id=request.getParameter("id");
                String json=service.queryReply(Integer.parseInt(id));
                response.setContentType("text/html");
                response.setCharacterEncoding("utf-8");;
                PrintWriter out=response.getWriter();

                out.print(json);
                out.flush();
                out.close();
                break;
        }

    }

    private void del(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        if(service.delArrticle(Integer.parseInt(id))){
            RequestDispatcher dispatcher=request.getRequestDispatcher("article?action=page&curpage=1");
            try {
                dispatcher.forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void addz(HttpServletRequest request, HttpServletResponse response) {
        String rootid=request.getParameter("rootid");
        Article a=new Article();
        a.setRootid(Integer.parseInt(rootid));
        a.setTitle(request.getParameter("title"));
        a.setContent(request.getParameter("content"));
        BBSUser user=(BBSUser)request.getSession().getAttribute("user");
        a.setUser(user);
        if(service.addArticle(a)){
            RequestDispatcher dispatcher=null;
            if(rootid.equals("0")){//主贴
                dispatcher=request.getRequestDispatcher("article?action=page&curpage=1");

            }else{
                dispatcher=request.getRequestDispatcher("article?action=queryid&id="+rootid);
            }
            try {
                dispatcher.forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void page(HttpServletRequest request, HttpServletResponse response){
        int curPage=Integer.parseInt(request.getParameter("curpage"));
        int userid=0;
        if(request.getSession().getAttribute("user")==null){
            userid=999;
        }else{
            BBSUser user=(BBSUser)request.getSession().getAttribute("user");
            userid=user.getId();
        }

        PageBean pb=service.QueryAll(curPage,userid);
        RequestDispatcher dispatcher=request.getRequestDispatcher("show.jsp");
        request.setAttribute("pb",pb);
        try {
            dispatcher.forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
