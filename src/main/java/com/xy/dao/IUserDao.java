package com.xy.dao;

import com.xy.vo.BBSUser;

/**
 * Created by MOMO on 2016/10/13.
 */
public interface IUserDao {
    public BBSUser Login(BBSUser user);
    public boolean addUser(BBSUser user);
    public byte [] queryPicbyId(int id);
}
