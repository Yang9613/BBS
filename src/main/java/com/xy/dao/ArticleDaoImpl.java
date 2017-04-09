package com.xy.dao;

import com.xy.db.DBCPDB;
import com.xy.vo.Article;
import com.xy.vo.BBSUser;
import com.xy.vo.PageBean;
import com.xy.vo.ReArticle;

import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOMO on 2016/10/21.
 */
public class ArticleDaoImpl implements IArticleDao {
    private Connection conn;
    public ArticleDaoImpl(){
        conn= DBCPDB.getDBCPConnection();
    }

    public static void main(String[] args) {
        new ArticleDaoImpl().QueryAll(1,999);
    }
    @Override
    public PageBean QueryAll(int curPage,int userid) {
        CallableStatement cs=null;
        ResultSet rs=null;
        ArrayList <Article> list=new ArrayList<Article>();
        PageBean pb=new PageBean();
            try {
            String sql="call q3(?,?,?,?,?)";
            cs=conn.prepareCall(sql);
            cs.setInt(1,curPage);
            cs.setInt(2,userid);
            cs.registerOutParameter(3, Types.INTEGER);//maxRowCount
            cs.registerOutParameter(4, Types.INTEGER);//rowsPerPage
            cs.registerOutParameter(5, Types.INTEGER);//maxPage
            cs.execute();
            boolean flag=cs.execute();
            while(flag){
                rs=cs.getResultSet();
                pb.setCurPage(curPage);
                pb.setMaxPage(cs.getInt(5));
                pb.setMaxRowCount(cs.getInt(3));
                pb.setRowsPerPage(cs.getInt(4));

                while(rs.next()){
                    BBSUser user=new BBSUser();
                    Article a=new Article();
                    a.setId(rs.getInt("id"));
                    a.setTitle(rs.getString("title"));
                    a.setContent(rs.getString("content"));
                    a.setDatetime(rs.getString("datetime"));
                    user.setId(rs.getInt("userid"));
                    a.setUser(user);
                    list.add(a);
                }
                pb.setData(list);//每页的数据
                flag=cs.getMoreResults();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(cs!=null){
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return pb;
    }

    @Override
    public boolean addArticle(Article a) {
        PreparedStatement pstmt=null;
        boolean flag=false;
        try {
            String sql="insert into article (rootid,title,content,datetime,userid) values(?,?,?,now(),?) ";
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,a.getRootid());
            pstmt.setString(2,a.getTitle());
            //操作clob
            String s=a.getContent();
            StringReader sr=new StringReader(s);
            pstmt.setCharacterStream(3,sr,s.length());
            pstmt.setInt(4,a.getUser().getId());
            flag=pstmt.executeUpdate()>0?true:false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(pstmt!=null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    @Override
    public boolean delArrticle(int id) {//主贴的主键
        PreparedStatement pstmt=null;
        boolean flag=false;
        try {
            String sql="delete from article where id=? or rootid=?";
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.setInt(2,id);

            flag=(pstmt.executeUpdate()>0)?true:false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(pstmt!=null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }
    public ReArticle queryReply(int id){
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        ReArticle re=new ReArticle();
        try {
            String sql="select t.title,r.* \n" +
                    "from \n" +
                    "(select a.id,a.title,a.content,a.datetime,a.rootid,a.userid \n" +
                    " from article a \n" +
                    " where a.rootid=?) r,article t \n" +
                    " where t.id=?";
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.setInt(2,id);
            rs=pstmt.executeQuery();
            List<Article> list=new ArrayList<>();
            while(rs.next()){
                re.setTitle(rs.getString("t.title"));

                BBSUser user=new BBSUser();
                user.setId(rs.getInt("r.id"));

                Article a=new Article();
                a.setUser(user);
                a.setId(rs.getInt("r.id"));
                a.setTitle(rs.getString("r.title"));
                a.setContent(rs.getString("r.content"));
                a.setDatetime(rs.getString("r.datetime"));
                a.setRootid(rs.getInt("r.rootid"));

                list.add(a);
            }
            re.setArticle(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(pstmt!=null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return re;
    }
}
