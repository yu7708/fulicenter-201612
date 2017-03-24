package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.CartModel;
import cn.ucai.fulicenter.model.net.ICartModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CartAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private static final String TAG = CartFragment.class.getSimpleName();
    ICartModel mModel;
    @BindView(R.id.tv_cart_sum_price)
    TextView tvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView tvCartSavePrice;
    @BindView(R.id.tv_cart_buy)
    TextView tvCartBuy;
    @BindView(R.id.layout_cart)
    RelativeLayout layoutCart;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.tv_nothing)
    TextView tvNothing;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    User user;
    ArrayList<CartBean> cartList=new ArrayList<>();
    LinearLayoutManager mManager;
    CartAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = new CartModel();
        initData();
        initView();
        setListener();
    }

    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                initData();
            }
        });
    }
    public void setRefresh(boolean refresh) {
        srl.setRefreshing(refresh);
        tvRefresh.setVisibility(refresh?View.VISIBLE:View.GONE);
    }
    public void setCartListerLayout(boolean isShow){
        //没商品时显示 空空如也
        tvNothing.setVisibility(isShow?View.GONE:View.VISIBLE);
        //没商品时不显示结账那一条,有数据才显示
        layoutCart.setVisibility(isShow?View.VISIBLE:View.GONE);

    }
    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
            mManager=new LinearLayoutManager(getContext());
        rv.setLayoutManager(mManager);
        rv.setHasFixedSize(true);//不是很懂这句话什么意思
        mAdapter=new CartAdapter(getContext(),cartList);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
        setCartListerLayout(false);
    }

    private void initData() {
         user= FuLiCenterApplication.getCurrentUser();
        if(user!=null){
            showCartList();
        }
    }

    private void showCartList() {
        mModel.loadData(getContext(), user.getMuserName(),
                new OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        setRefresh(false);
                        setCartListerLayout(true);
                        if(result!=null){
                            cartList.clear();
                            if(result.length>0) {
                                ArrayList<CartBean> list = ResultUtils.array2List(result);
                                cartList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            }else if(result.length==0){
                                setCartListerLayout(false);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        setRefresh(false);
                        Log.e(TAG,"showCartList.onError="+error);
                    }
                });
    }


}
