package com.zdnf.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zdnf.bbs.dao.UserApiDao;
import com.zdnf.bbs.domain.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by ZDNF on 2017/5/11.
 */
@Service
public class UserApiService {
    @Autowired
    UserApiDao UserApiDao;

    public User get_user(String name){return UserApiDao.get_user_info(name);}

    public List<User> get_user_replay(String name, int page){
        int low=10*(page-1);
        return UserApiDao.get_user_replay(name,low);
    }

    public List<User> get_user_post(String name,int page){
        int low=10*(page-1);
        return UserApiDao.get_user_post(name,low);
    }

    public boolean up(MultipartFile file,String username){
        if (file.isEmpty())return false;
        //文件路径
        String filePath = "d:/bbs/userimgs/";
        //创建文件
        File dest = new File(filePath + username+".jpg");
        //判断文件路径存不存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        //判断文件存不存在 存在就删掉
        if (dest.exists()&dest.isFile()){
            dest.delete();
        }
        try {
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String get_id(String name){
       return UserApiDao.get_id(name);
    }
}
