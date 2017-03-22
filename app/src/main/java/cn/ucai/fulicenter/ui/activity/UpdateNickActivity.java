package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.model.utils.SharedPreferenceUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/22.
 */
public class UpdateNickActivity extends AppCompatActivity {
    private static final String TAG = UpdateNickActivity.class.getSimpleName();
    @BindView(R.id.updateNick_title)
    TextView updateNickTitle;
    @BindView(R.id.updateNick)
    EditText updateNick;
    @BindView(R.id.btn_save)
    Button btnSave;
    User user;
    IUserModel mModel;
    String newNick;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        mModel=new UserModel();
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getCurrentUser();
        if(user==null){
            backArea();
        }else{
            updateNick.setText(user.getMuserNick());
            updateNick.selectAll();
        }
    }

    @OnClick(R.id.backClickArea)
    public void backArea() {
        MFGT.finish(UpdateNickActivity.this);
    }
    @OnClick(R.id.btn_save)
    public void onClick(){
    if(checkInput()){
        mModel.updateNick(UpdateNickActivity.this, user.getMuserName(), newNick,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String r) {
                        Result result = ResultUtils.getResultFromJson(r, User.class);
                        if(result!=null) {
                            if (result.isRetMsg()) {
                                User user = (User) result.getRetData();
                                updateSuccess(user);
                            }else {
                                if(result.getRetCode()== I.MSG_USER_SAME_NICK){
                                    CommonUtils.showShortToast(R.string.update_nick_fail_unmodify);
                                }
                                if(result.getRetCode()==I.MSG_USER_UPDATE_NICK_FAIL){
                                    CommonUtils.showShortToast(R.string.update_fail);
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

    private void updateSuccess(final User user) {
        L.e(TAG,"loginSuccess,user"+user);
        FuLiCenterApplication.setCurrentUser(user);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(UpdateNickActivity.this).saveUserInfo(user);
                L.e(TAG,"loginSuccess,b="+b);
            }
        }).start();
        MFGT.finish(UpdateNickActivity.this);
    }

    private boolean checkInput() {
        newNick=updateNick.getText().toString().trim();
        if(TextUtils.isEmpty(newNick)){
            updateNick.requestFocus();
            updateNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if(newNick.equals(user.getMuserNick())){
            updateNick.requestFocus();
            updateNick.setError(getString(R.string.update_nick_fail_unmodify));
            return false;
        }
        return true;
    }
}
