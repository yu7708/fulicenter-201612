package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.INewGoodsModel;
import cn.ucai.fulicenter.model.net.NewGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    private static final String TAG=NewGoodsFragment.class.getSimpleName();

    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder bind;
    INewGoodsModel model;
    int pageId=1;
    int cat_id=0;
    GridLayoutManager gm;
    GoodsAdapter mAdapter;
    List<NewGoodsBean> mList=new ArrayList<>();

    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mRefreshHint;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
         bind= ButterKnife.bind(this, view);

        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model=new NewGoodsModel();
        cat_id=getActivity().getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID,cat_id);//得到传输的图片的id
        initData(I.ACTION_DOWNLOAD);
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

    private void initView(View view) {
        gm=new GridLayoutManager(getActivity(), I.COLUM_NUM);
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
        mAdapter=new GoodsAdapter(getContext(),mList);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.srl);
        mRefreshHint= (TextView) view.findViewById(R.id.tvRefreshHint);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
    }

    private void initData(final int action) {
        model.loadData(getActivity(),cat_id, pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                L.e(TAG,"initData.result="+result);
                setRefresh(false);
                mAdapter.setMore(true);
                if(result!=null&&result.length>0){
                    L.e(TAG,"initData.result.length="+result.length);

                ArrayList<NewGoodsBean> newGoodsBeenList = ResultUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        mList.clear();
                    }
                    mList.addAll(newGoodsBeenList);
                    if(newGoodsBeenList.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(String error) {
                L.e(TAG,"initData.error="+error);
                setRefresh(false);
            }
        });
    }
    private void setRefresh(boolean refresh){
        mSwipeRefreshLayout.setRefreshing(refresh);
        mRefreshHint.setVisibility(refresh?View.VISIBLE:View.GONE);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bind!=null){
            bind.unbind();
        }
    }
}
