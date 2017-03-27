package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/27.
 */

public class SongHuoActivity extends AppCompatActivity {
    @BindView(R.id.et_yourName)
    EditText etYourName;
    @BindView(R.id.et_yourPhone)
    EditText etYourPhone;
    @BindView(R.id.et_yourStreet)
    EditText etYourStreet;

    int orderPirce = 0;
    @BindView(R.id.spArea)
    Spinner spArea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_songhuo);
        ButterKnife.bind(this);
        orderPirce = getIntent().getIntExtra(I.ORDER_BUY_PRICE, 0);
        //得到传出的Intent价格
        initView();
    }

    private void initView() {

    }

    @OnClick(R.id.btn_commit)
    public void onClick() {
    }

    @OnClick(R.id.backClickArea)
    public void onBack() {
        MFGT.finish(this);
    }
}
