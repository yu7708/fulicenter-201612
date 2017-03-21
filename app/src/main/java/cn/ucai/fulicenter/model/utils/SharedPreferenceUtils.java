package cn.ucai.fulicenter.model.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.ucai.fulicenter.application.FuLiCenterApplication;

/**
 * Created by Administrator on 2017/3/21.
 */

public class SharedPreferenceUtils {
    private static final String SHARE_PREFERENCE_NAME="share";
    private static final String SAVA_USERINFO_USERNAME="m_user_username";
    static SharedPreferenceUtils instance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    public SharedPreferenceUtils(){
       sharedPreferences = FuLiCenterApplication.getInstance().
                getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
      edit= sharedPreferences.edit();
    }

    public static SharedPreferenceUtils getInstance() {//如果数据为空,实例化
        if(instance==null){
            instance=new SharedPreferenceUtils();
        }
        return instance;
    }
    public void setUserName(String username){//存入名字
        edit.putString(SAVA_USERINFO_USERNAME,username).commit();
    }
    public String getUserName(){//外面得到数据
        return sharedPreferences.getString(SAVA_USERINFO_USERNAME,null);
    }
    public void removeString(){//删除数据
        edit.remove(SAVA_USERINFO_USERNAME).commit();
    }
}
