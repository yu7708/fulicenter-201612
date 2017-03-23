package cn.ucai.fulicenter.application;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.utils.SharedPreferenceUtils;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FuLiCenterApplication extends Application{
    static FuLiCenterApplication instance;
    static User currentUser;//登录里要判断是否登录过
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static Context getInstance(){
        return instance;
    }

    public static User getCurrentUser() {
        if(currentUser==null){
            String username= SharedPreferenceUtils.getInstance().getUserName();
            currentUser= UserDao.getInstance(instance).getUserInfo(username);
        }
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        FuLiCenterApplication.currentUser = currentUser;
    }
}
