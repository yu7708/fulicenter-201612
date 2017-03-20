package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/20.
 */
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.ed_nick)
    EditText edNick;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.ed_OkPassword)
    EditText edOkPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
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

    }
}
