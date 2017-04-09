package com.xy.service;

import com.xy.dao.IUserDao;
import com.xy.dao.UserDaoImpl;
import com.xy.vo.BBSUser;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by MOMO on 2016/10/13.
 */
public class UserServiceImpl implements IUserService{
    private IUserDao dao=new UserDaoImpl();
    public BBSUser Login(BBSUser user) {
        return dao.Login(user);
    }

    private String savepath;//真正的头像存储目录
    private Map<String ,String > types=new HashMap<String, String>();
    //存储能上传的文件类型
    public UserServiceImpl(){
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }
    public BBSUser uploadPic(String tpath, FileItemIterator it) {
        BBSUser user=new BBSUser();
        try {
            while(it.hasNext()){//遍历request中的所有字段
                FileItemStream fis=it.next();
                String name=fis.getFieldName();//文件域的名字
                InputStream is=fis.openStream();
                if((!fis.isFormField())&&(fis.getName().length()>0)){
                    //文件域,并且已选择了头像

                    //得到上传文件类型
                    String type=fis.getContentType();
                    //得到上传文件的的那个流

                    savepath=""+System.getProperty("file.separator")+"upload";

                    if(!types.containsKey(type)){
                        break;
                    }
                    //创建全球唯一文件名字
                    UUID uu=UUID.randomUUID();
                    String filename=uu.toString()+types.get(type);
                    File savedir=new File(tpath+savepath+"/"+filename);
                    //真实的含tomcat的上传头像路径

                    //上传，拷贝
                    try (BufferedInputStream bis=new BufferedInputStream(is);
                         FileOutputStream fos=new FileOutputStream(savedir);
                         BufferedOutputStream bos=new BufferedOutputStream(fos);){

                        Streams.copy(bis,bos,true);//拷贝完毕

                        //拷进数据库
                        user.setPath(tpath+savepath+"/"+filename);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {//字段域，取出用户名和密码
                    switch (name) {
                        case "reusername":
                            user.setUsername(Streams.asString(is, "utf-8"));
                            break;
                        case "repassword":
                            user.setPassword(Streams.asString(is, "utf-8"));
                            break;
                    }
                }
            }
            user.setPagenum(5);
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public boolean addUser(BBSUser user) {
        return dao.addUser(user);
    }

    @Override
    public byte [] queryPicbyId(int id) {
        return dao.queryPicbyId(id);
    }
}
