package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    GridLayoutManager gm;
    GoodsAdapter mAdapter;
    List<NewGoodsBean> mList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
         bind= ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model=new NewGoodsModel();
        initData();
        initView();
    }

    private void initView() {
        gm=new GridLayoutManager(getActivity(), I.COLUM_NUM);
        rv.setLayoutManager(gm);
        rv.setHasFixedSize(true);
        mAdapter=new GoodsAdapter(getContext(),mList);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
    }

    private void initData() {
        model.loadData(getActivity(), pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                L.e(TAG,"initData.result="+result);
                if(result!=null&&result.length>0){
                    L.e(TAG,"initData.result.length="+result.length);
                ArrayList<NewGoodsBean> newGoodsBeenList = ResultUtils.array2List(result);
                    mAdapter.addList(newGoodsBeenList);
//                newGoodsBeenList.addAll(mList);
            //    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(String error) {
                L.e(TAG,"initData.error="+error);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bind!=null){
            bind.unbind();
        }
    }
}
