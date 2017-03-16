package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

/**
 * Created by Administrator on 2017/3/16.
 */
public class BoutiqueChildActivity extends AppCompatActivity {
    @BindView(R.id.tvBoutiqueTitle)
    TextView tvBoutiqueTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        tvBoutiqueTitle.setText(getIntent().getStringExtra(I.Boutique.TITLE));//二级页面的标题拿到
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new NewGoodsFragment())
                .commit();
    }

    @OnClick(R.id.backClickArea)//二级页面跳转回精选页面
    public void onClick() {
        finish();
    }
}
