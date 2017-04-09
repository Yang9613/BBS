package com.xy.control;

import com.xy.service.IUserService;
import com.xy.service.UserServiceImpl;
import com.xy.vo.BBSUser;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MOMO on 2016/10/13.
 */
@WebServlet(name = "UserControl",urlPatterns = {"/user"},initParams = {
    @WebInitParam(name="success",value="/article?action=page&curpage=1")
})
public class UserControl extends HttpServlet {
    IUserService service=new UserServiceImpl();
    private Map <String,String> map=new HashMap<String, String>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("success",config.getInitParameter("success"));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(ServletFileUpload.isMultipartContent(request)){//流
            String tpath=request.getServletContext().getRealPath("/");

            String tmppath=""+System.getProperty("file.separator")+"tmpdir";
            File tmpdir=new File(tmppath);;//头像临时存储目录
            if(!tmpdir.isDirectory()){
                tmpdir.mkdir();
            }

            DiskFileItemFactory dif=new DiskFileItemFactory();
            dif.setRepository(tmpdir);//指定上传文件的临时目录
            dif.setSizeThreshold(1024*1024*10);//指定在内存中缓存数据大小，单位为byte

            ServletFileUpload sf=new ServletFileUpload(dif);
            sf.setFileSizeMax(1024*1024*5);//单个文件大小
            sf.setSizeMax(1024*1024*50);//总文件大小
            try {
                FileItemIterator it=sf.getItemIterator(request);

                BBSUser user=service.uploadPic(tpath,it);
                if(service.addUser(user)){
                    RequestDispatcher dispatcher=null;
                    String success=map.get("success");
                    dispatcher=request.getRequestDispatcher(success);
                    dispatcher.forward(request,response);
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }else{//键值对（默认）
            String action=request.getParameter("action");
            switch (action){
                case "login":
                    login(request, response);
                    break;
                case "read":
                    read(request, response);
                    break;
            }

        }

    }
    //读取头像
    private void read(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        byte [] buffer=service.queryPicbyId(Integer.parseInt(id));
        try {
            OutputStream os=response.getOutputStream();
            os.write(buffer);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=request.getParameter("username");
        String pwd=request.getParameter("password");
        BBSUser user=new BBSUser(username,pwd);
        RequestDispatcher dispatcher=null;
        String success=map.get("success");
        BBSUser user1=service.Login(user);
        if(user1!=null){//进主页传递成功消息，操作cookie，放session

            request.setAttribute("info","登陆成功");
            request.getSession().setAttribute("user",user1);

            Cookie cu=new Cookie("http://www.papok.org/username",username);//键唯一，不能存中文
            cu.setMaxAge(3600*24*7);
            response.addCookie(cu);
            Cookie cp=new Cookie("http://www.papok.org/password",pwd);
            cp.setMaxAge(3600*24*7);
            response.addCookie(cp);

        }else{//进主页传递登录失败消息

            request.setAttribute("info","登陆失败，请重新登录");
        }
        dispatcher=request.getRequestDispatcher(success);
        dispatcher.forward(request,response);
    }
}
