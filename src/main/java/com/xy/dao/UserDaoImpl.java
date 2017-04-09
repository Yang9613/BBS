package com.xy.dao;

import com.xy.db.DruidDB;
import com.xy.vo.BBSUser;
import java.io.FileInputStream;
import java.sql.*;

/**
 * Created by MOMO on 2016/10/13.
 */
public class UserDaoImpl implements IUserDao {
    private Connection conn=null;
    public UserDaoImpl(){
        conn= DruidDB.getDruidConnection();
    }
    /**
     *
     * @param user 传递用户名和密码
     * @return true 登录成功
     *          false登录失败
     */
    public BBSUser Login(BBSUser user) {
        BBSUser user1=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        boolean flag=false;
        try {
            pstmt=conn.prepareStatement("select * from bbsuesr where username=? and password=?");
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getPassword());
            rs=pstmt.executeQuery();

            if(rs.next()){
                user1=new BBSUser();
                user1.setId(rs.getInt("id"));
                user1.setUsername(rs.getString("username"));
                user1.setPassword(rs.getString("password"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
        return user1;
    }

    @Override
    public boolean addUser(BBSUser user) {
        PreparedStatement pstmt=null;
        boolean flag=false;
        try {
            pstmt=conn.prepareStatement("insert into bbsuesr (username,password,pic,pagenum) values(?,?,?,?)");
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getPassword());
            //blob
            FileInputStream is=new FileInputStream(user.getPath());
            pstmt.setBinaryStream(3,is,is.available());
            pstmt.setInt(4,user.getPagenum());
            flag=pstmt.executeUpdate()>0?true:false;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
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
    public byte [] queryPicbyId(int id) {
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        byte [] buffer=null;
        try {
            pstmt=conn.prepareStatement("select * from bbsuesr where id=?");
            pstmt.setInt(1,id);
            rs=pstmt.executeQuery();
            if(rs.next()){
                //读取blob
                Blob b=rs.getBlob("pic");
                buffer=b.getBytes(1,(int)b.length());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
        return buffer;
    }
}
