package com.zdnf.bbs.dao;

import com.zdnf.bbs.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ZDNF on 2017/5/11.
 */
public interface UserApiDao {
    public User get_user_info(@Param("name")String name);
}