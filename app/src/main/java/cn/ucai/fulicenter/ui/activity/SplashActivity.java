package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import java.util.zip.Inflater;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.utils.SharedPreferenceUtils;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    int time=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username= SharedPreferenceUtils.getInstance().getUserName();
                if(username!=null){//判断是否登录过,提取信息
                    User user= UserDao.getInstance(SplashActivity.this).getUserInfo(username);
                    Log.e(TAG,"username="+user);
                    FuLiCenterApplication.setCurrentUser(user);//每次onDestory,Application都会清空
                }
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                SplashActivity.this.finish();
            }
        },time);
    }
}
