package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.BoutiqueModel;
import cn.ucai.fulicenter.model.net.IBoutiqueModel;
import cn.ucai.fulicenter.model.net.INewGoodsModel;
import cn.ucai.fulicenter.model.net.NewGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {
    private static final String TAG=BoutiqueFragment.class.getSimpleName();

    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder bind;
    IBoutiqueModel model;
    int pageId=1;

    LinearLayoutManager gm;
    BoutiqueAdapter mAdapter;
    List<BoutiqueBean> mList=new ArrayList<>();

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
        model=new BoutiqueModel();
        initData();

        setListener();
    }

    private void setListener() {
        setPullDownListener();
    }
    private void setPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                initData();
            }
        });
    }

    private void initView(View view) {
        gm=new LinearLayoutManager(getContext());

        rv.setLayoutManager(gm);
        rv.setHasFixedSize(true);
        mAdapter=new BoutiqueAdapter(getContext(),mList);
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

    private void initData() {
        model.loadData(getActivity(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                L.e(TAG,"initData.result="+result);
                setRefresh(false);
                if(result!=null&&result.length>0){
                    L.e(TAG,"initData.result.length="+result.length);

                ArrayList<BoutiqueBean> BoutiqueBeanList = ResultUtils.array2List(result);
                        mList.clear();
                    mList.addAll(BoutiqueBeanList);
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
