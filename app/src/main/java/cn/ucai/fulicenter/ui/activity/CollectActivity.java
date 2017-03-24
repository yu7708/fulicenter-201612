package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CollectsAdapter;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2017/3/23.
 */
public class CollectActivity extends AppCompatActivity {
    private static final String TAG = "CollectActivity";
    @BindView(R.id.tvRefreshHint)
    TextView tvRefreshHint;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    IUserModel mModel;
    Unbinder bind;
    int pageId = 1;
    int cat_id = 0;
    GridLayoutManager gm;
    List<CollectBean> mList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mRefreshHint;
    CollectsAdapter mAdapter;
    @BindView(R.id.tvBoutiqueTitle)
    TextView tvBoutiqueTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        bind = ButterKnife.bind(this);
        mModel = new UserModel();
        initData(I.ACTION_DOWNLOAD);
        initView();
        setListener();
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = gm.findLastVisibleItemPosition();
                if (lastPosition == mAdapter.getItemCount() - 1
                        && RecyclerView.SCROLL_STATE_IDLE == newState
                        && mAdapter.isMore()) {
                    pageId++;
                    initData(I.ACTION_PULL_UP);
                }
            }
        });
    }

    private void setPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                pageId = 1;
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initView() {
        gm = new GridLayoutManager(this, I.COLUM_NUM);
        mAdapter = new CollectsAdapter(this, mList);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mList.size()) {
                    return 2;
                }
                return 1;
            }
        });
        rv.setLayoutManager(gm);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        mRefreshHint = (TextView) findViewById(R.id.tvRefreshHint);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        tvBoutiqueTitle.setText("收藏的宝贝");
    }

    private void initData(final int action) {
        User user = FuLiCenterApplication.getCurrentUser();
        if(user==null){
            finish();
            return;
        }
        mModel.loadCollect(CollectActivity.this, user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                setRefresh(false);
                mAdapter.setMore(true);
                Log.e(TAG, "onSuccess=" + result);
                if (result != null && result.length > 0) {
                    ArrayList<CollectBean> list = ResultUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mList.clear();
                    }
                    mList.addAll(list);
                    if (mList.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                    mAdapter.notifyDataSetChanged();
                }else if(result==null &&result.length==0){
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void setRefresh(boolean refresh) {
        mSwipeRefreshLayout.setRefreshing(refresh);
        mRefreshHint.setVisibility(refresh ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        MFGT.finish(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==I.REQUEST_CODE_COLLECT){
            boolean isCollect = data.getBooleanExtra(I.GoodsDetails.KEY_IS_COLLECT, true);
            int goodsId = data.getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
            Log.e(TAG,"onActivityResult,isCollect="+isCollect);
            if(!isCollect){
                //更新
                mList.remove(new CollectBean(goodsId));
                mAdapter.notifyDataSetChanged();
            }
        }

    }
}
