package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/20.
 */
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_OkPassword)
    EditText etOkPassword;

    IUserModel mModel;

    String userName;
    String nick;
    String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        mModel=new UserModel();
    }

    private void initView() {
        //我这里的tvTitle是固定的名字了,就不用传参了
        //tvTitle.setText(R.string.register_title);
    }


    @OnClick({R.id.ivBack, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(RegisterActivity.this);
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        if(checkInput()){                                     //加密,64位加密
        mModel.register(RegisterActivity.this, userName, nick, MD5.getMessageDigest(password)
                , new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Result s=ResultUtils.getResultFromJson(result, User.class);//转换为json
                if(s!=null){
                    if(s.isRetMsg()){
                        registerSuccess();
                    }else{
                        if(s.getRetCode()== I.MSG_REGISTER_USERNAME_EXISTS){
                            CommonUtils.showShortToast(R.string.register_fail_exists);
                        }else{
                            CommonUtils.showShortToast(R.string.register_fail);
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(R.string.register_fail);
            }
        });
        }
    }

    private void registerSuccess() {
        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,userName));//代表执行成功了,传递数据到登录的用户名
        CommonUtils.showShortToast(R.string.register_success);
        MFGT.finish(RegisterActivity.this);
    }

    private boolean checkInput() {
        userName=etUsername.getText().toString().trim();
        nick=etNick.getText().toString().trim();
        password=etPassword.getText().toString().trim();
        String okPassword=etOkPassword.getText().toString().trim();
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
        if(TextUtils.isEmpty(nick)){
            etNick.requestFocus();
            etNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if(TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        if(TextUtils.isEmpty(okPassword)){
            etOkPassword.requestFocus();
            etOkPassword.setError(getString(R.string.confirm_password_connot_be_empty));
            return false;
        }
        if(!password.equals(okPassword)){//两次密码是否相等,前面是!,判断不一致
            etOkPassword.requestFocus();
            etOkPassword.setError(getString(R.string.two_input_password));
            return false;
        }
        return true;
    }
}
