package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/20.
 */

public class UserModel implements IUserModel{
    @Override
    public void login(Context context, String userName, String password, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.PASSWORD,password)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void register(Context context, String userName, String nick, String password, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD,password)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    @Override
    public void updateNick(Context context, String userName, String newNick, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,newNick)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void uploadAvatar(Context context, String userName, File file,OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,userName)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE)
                .addFile2(file)
                .post()
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void collectGoods(Context context, String username, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void loadCollect(Context context, String userName, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener) {
        OkHttpUtils<CollectBean[]> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME,userName)
                .addParam(I.PAGE_ID,pageId+"")
                .addParam(I.PAGE_SIZE,"10")
                .targetClass(CollectBean[].class)
                .execute(listener);
    }
}
