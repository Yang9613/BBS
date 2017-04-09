package com.xy.db;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by MOMO on 2016/9/30.
 */
public class DBCPDB {
    private static DataSource ds=null;
    private static Connection conn=null;
    static {
        Properties pro = new Properties();
        try {
            pro.load(DBCPDB.class.getClassLoader().getResourceAsStream("dbcp.ini"));
            ds= BasicDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getDBCPConnection(){
        try {
            conn=ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        System.out.print(DBCPDB.getDBCPConnection());
    }
}