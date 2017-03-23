package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;
import java.util.Collections;

import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/3/20.
 */

public interface IUserModel {
    void login(Context context,String userName,String password, OnCompleteListener<String> listener);
    void register(Context context,String userName,String nick,String password
                  ,OnCompleteListener<String> listener);
    void updateNick(Context context,String userName,String newNick,OnCompleteListener<String> listener);
    void uploadAvatar(Context context, String userName, File file, OnCompleteListener<String> listener);
    void collectGoods(Context context, String username, OnCompleteListener<MessageBean> listener);
    void loadCollect(Context context, String userName, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener);
}
