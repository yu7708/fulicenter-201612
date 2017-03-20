package cn.ucai.fulicenter.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.CatFilterCategoryButton;

public class CategoryChildActivity extends AppCompatActivity {
    boolean change;
    boolean sortPrice;
    boolean sortAddTime;
    int sortBy = I.SORT_BY_ADDTIME_DESC;//当前的排序

    String groupName;
    ArrayList<CategoryChildBean> mList;

    NewGoodsFragment newGoodsFragment;
    @BindView(R.id.sss)
    RelativeLayout sss;
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addTime)
    Button btnSortAddTime;
    @BindView(R.id.cfcb_filter)
    CatFilterCategoryButton cfcbFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        newGoodsFragment = new NewGoodsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, newGoodsFragment).commit();
        groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        mList= (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.DATA);
        initView();
    }

    private void initView() {
        cfcbFilter.initView(groupName,mList);
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addTime})
    public void sortList(View view) {//如何才能得到升降序呢,设置标志位
        Drawable end = null;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                sortBy = sortPrice ? I.SORT_BY_PRICE_ASC : I.SORT_BY_PRICE_DESC;
                sortPrice = !sortPrice;
                end = getResources().getDrawable(sortPrice ?
                        R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                btnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
            case R.id.btn_sort_addTime:
                sortBy = sortAddTime ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                sortAddTime = !sortAddTime;
                end = getResources().getDrawable(sortAddTime ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                btnSortAddTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
        }
        newGoodsFragment.sortBy(sortBy);
    }

    @OnClick(R.id.tvChange)
    public void onChange() {
        sss.setVisibility(change ? View.GONE : View.VISIBLE);
        change = !change;
    }
}
