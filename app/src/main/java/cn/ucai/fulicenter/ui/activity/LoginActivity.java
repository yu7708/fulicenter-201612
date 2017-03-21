package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.model.utils.SharedPreferenceUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/20.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;

    String userName,password;
    IUserModel mModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mModel=new UserModel();
    }

    @OnClick({R.id.backArea, R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backArea:
                MFGT.finish(LoginActivity.this);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                MFGT.gotoRegister(LoginActivity.this);
                break;
        }
    }
    private void login() {
        if(checkInput()){
            mModel.login(LoginActivity.this, userName, MD5.getMessageDigest(password), new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String r) {
                    Result result = ResultUtils.getResultFromJson(r, User.class);
                    if(result!=null&&result.isRetMsg()){
                        User user= (User) result.getRetData();
                        if(user!=null){
                            loginSuccess(user);
                        }
                    }else{
                        if(result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showShortToast(R.string.login_fail_unknow_user);
                        }
                        if(result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showShortToast(R.string.login_fail_error_password);
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast(R.string.login_fail);
                }
            });
        }
    }

    private void loginSuccess(final User user) {
        L.e(TAG,"loginSuccess,user"+user);
        FuLiCenterApplication.setCurrentUser(user);
        SharedPreferenceUtils.getInstance().setUserName(user.getMuserName());//如果得到单例,设置名称
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(LoginActivity.this).saveUserInfo(user);
                L.e(TAG,"loginSuccess,b="+b);
            }
        }).start();
        MFGT.finish(LoginActivity.this);
    }

    private boolean checkInput() {
        userName=etUsername.getText().toString().trim();
        password=etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(userName)){
        etUsername.requestFocus();
        etUsername.setError(getString(R.string.user_name_connot_be_empty));
        return false;
    }
        if(!userName.matches("[a-zA-z]\\w{5,15}")){//判断违法字符,取6到16位,取非,判断不是所给情况
            etUsername.requestFocus();
            etUsername.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if(TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode== I.REQUEST_CODE_REGISTER){
            String username = data.getStringExtra(I.User.USER_NAME);
            etUsername.setText(username);
        }
    }
}
