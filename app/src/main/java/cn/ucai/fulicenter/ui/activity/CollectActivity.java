package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;
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

    int pageId=1;
    int cat_id=0;
    GridLayoutManager gm;
    GoodsAdapter mAdapter;
    List<NewGoodsBean> mList=new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mRefreshHint;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_goods);
        ButterKnife.bind(this);
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
                int lastPosition=gm.findLastVisibleItemPosition();
                if(lastPosition==mAdapter.getItemCount()-1
                        && RecyclerView.SCROLL_STATE_IDLE==newState
                        && mAdapter.isMore()){
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
                pageId=1;
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }
    private void initView() {
            gm=new GridLayoutManager(this, I.COLUM_NUM);
            gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(position==mList.size()){
                        return 2;
                    }
                    return 1;
                }
            });
            rv.setLayoutManager(gm);
            rv.setHasFixedSize(true);
            mAdapter=new GoodsAdapter(this,mList);
            rv.setAdapter(mAdapter);
            rv.addItemDecoration(new SpaceItemDecoration(12));
            mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.srl);
            mRefreshHint= (TextView) findViewById(R.id.tvRefreshHint);
            mSwipeRefreshLayout.setColorSchemeColors(
                    getResources().getColor(R.color.google_blue),
                    getResources().getColor(R.color.google_green),
                    getResources().getColor(R.color.google_red),
                    getResources().getColor(R.color.google_yellow)
            );
        }

    private void initData(final int action) {
        User user = FuLiCenterApplication.getCurrentUser();
        mModel.loadCollect(this, user.getMuserName(), pageId, 10, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                if(result==null){

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    private void setRefresh(boolean refresh){
        mSwipeRefreshLayout.setRefreshing(refresh);
        mRefreshHint.setVisibility(refresh?View.VISIBLE:View.GONE);
    }

}
