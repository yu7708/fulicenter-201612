package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.health.SystemHealthManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/21.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    @BindView(R.id.iv_user_profile_avatar)
    ImageView ivUserProfileAvatar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_nick)
    TextView userNick;
    OnSetAvatarListener onSetAvatarListener;
    String avatarName;
    User user;
    IUserModel mModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getCurrentUser();
        if (user != null) {
            showUserInfo();
        } else {
            backArea();
        }
    }

    private void showUserInfo() {
        userName.setText(user.getMuserName());
        userNick.setText(user.getMuserNick());
        ImageLoader.downloadImg(SettingsActivity.this, ivUserProfileAvatar, user.getAvatar());
    }

    @OnClick(R.id.backClickArea)
    public void backArea() {
        MFGT.finish(SettingsActivity.this);
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {
        UserDao.getInstance(SettingsActivity.this).logout();//清空Application数据,清空sharedPreference,数据库关闭
        MFGT.gotoLogin(SettingsActivity.this, I.REQUEST_CODE_LOGIN);
        finish();
    }

    @OnClick(R.id.rl_updateNick)
    public void updateNick() {
        MFGT.gotoUpdateNick(SettingsActivity.this);
    }

    @OnClick(R.id.layout_user_profile_avatar)
    public void avatarOnClick() {
        onSetAvatarListener=new OnSetAvatarListener(SettingsActivity.this,R.id.layout_user_profile_avatar,
                user.getMuserName(),I.AVATAR_TYPE_USER_PATH);
    }

    private String getAvatarName() {
        avatarName=user.getMuserName()+ System.currentTimeMillis();//名字加当前时间戳
        L.e(TAG,"getAvatarName"+avatarName);
        return avatarName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(resultCode==RESULT_OK){
            L.e(TAG,"requestCode="+requestCode+",data="+data);
            //设置图片
           // onSetAvatarListener.setAvatar(requestCode,data,ivUserProfileAvatar);
            Uri data1=getIntent().getData();
            //判定图片的加载类型
            L.e(TAG,"requestCode="+requestCode+",data1111="+data1);
            if(requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO){
                //上传图片
                uploadAvatar();
            }
            onSetAvatarListener.setAvatar(requestCode,data,ivUserProfileAvatar);
        }*/
        onSetAvatarListener.setAvatar(requestCode,data,ivUserProfileAvatar);
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO){
            uploadAvatar();
        }
    }
    private void updateSuccess(final User user) {
        L.e(TAG,"loginSuccess,user"+user);
        FuLiCenterApplication.setCurrentUser(user);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(SettingsActivity.this).saveUserInfo(user);
                L.e(TAG,"loginSuccess,b="+b);
            }
        }).start();
    }
    private void uploadAvatar() {
        mModel=new UserModel();
        File file = new File(OnSetAvatarListener.getAvatarPath(SettingsActivity.this,
                I.AVATAR_TYPE_USER_PATH+"/"+user.getMuserName()+user.getMavatarSuffix()));
        mModel.uploadAvatar(SettingsActivity.this, user.getMuserName(), file,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String r) {
                        Result result = ResultUtils.getResultFromJson(r, User.class);
                        if(result!=null) {
                            if (result.isRetMsg()) {
                                User user = (User) result.getRetData();
                                updateSuccess(user);
                            }else {
                                if(result.getRetCode()== I.MSG_UPLOAD_AVATAR_FAIL){
                                    CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
