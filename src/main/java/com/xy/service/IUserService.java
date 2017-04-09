package com.xy.service;

import com.xy.vo.BBSUser;
import org.apache.commons.fileupload.FileItemIterator;

/**
 * Created by MOMO on 2016/10/13.
 */
public interface IUserService {
    public BBSUser Login(BBSUser user);
    public BBSUser uploadPic(String tpath, FileItemIterator it);
    public boolean addUser(BBSUser user);
    public byte [] queryPicbyId(int id);
}
