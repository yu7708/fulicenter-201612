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
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
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
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setCountAddListener();
        setCountDelListener();
        setUpdateCart();
        setItemPrice();
        PullDown();

    }
    private void setUpdateCart() {
    }

    private void setItemPrice() {
        mAdapter.setListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position= (int) buttonView.getTag();
                Log.e(TAG,"setListener,onCheckedChanged="+isChecked+"position=  "+position);
                cartList.get(position).setChecked(isChecked);//列表的哪一项被点击监听到
                setPriceText();
            }
        });
    }

    private void PullDown() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                initData();
            }
        });
    }

    private void setCountAddListener(){
        mAdapter.setCountAddListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CartBean bean= (CartBean) v.getTag();
                mModel.cartAction(getContext(), I.ACTION_CART_UPDATA, bean.getId() + "", null, null, bean.getCount()
                        , new OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                Log.e(TAG,"setCountAddListener,Count="+bean.getCount());
                                if(result!=null&&result.isSuccess()){
                                    bean.setCount(bean.getCount()+1);
                                    mAdapter.notifyDataSetChanged();
                                    Log.e(TAG,"setCountAddListener,getCountAfterAdd="+bean.getCount());
//                                        showCartList();
                                }
                            }
                            @Override
                            public void onError(String error) {

                            }
                        });
            }
        });
    }
    private void setCountDelListener() {
        mAdapter.setCountDelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CartBean bean= (CartBean) v.getTag();
                if(bean.getCount()>1){
                    mModel.cartAction(getContext(), I.ACTION_CART_UPDATA, bean.getId() + "", null, null, bean.getCount()
                            , new OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean result) {
                                    Log.e(TAG,"setCountAddListener,Count="+bean.getCount());
                                    if(result!=null&&result.isSuccess()){
                                        bean.setCount(bean.getCount()-1);
                                        mAdapter.notifyDataSetChanged();
                                        Log.e(TAG,"setCountAddListener,getCountAfterDel="+bean.getCount());
//                                    showCartList();
                                    }
                                }
                                @Override
                                public void onError(String error) {

                                }
                            });}
                else if(bean.getCount()==1){
                    mModel.cartAction(getContext(), I.ACTION_CART_DEL, bean.getId()+"", null, null, 1,
                            new OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean result) {
                                    if(result!=null&&result.isSuccess()){
                                        //mAdapter.notifyDataSetChanged();
                                        showCartList();
                                    }
                                }
                                @Override
                                public void onError(String error) {

                                }
                            });
                }
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
        setPriceText();
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
    public void setPriceText(){//放在初始化中
        int sumPrice=0;//总数
        int RankPrice=0;//折扣总数
        for (CartBean cart:cartList){
            if (cart.isChecked()) {//如果选中,再计算价格
                GoodsDetailsBean goods=cart.getGoods();
                if(goods!=null){
                    sumPrice += getPrice(goods.getCurrencyPrice())*cart.getCount();//一条商品总价格
                    RankPrice += getPrice(goods.getRankPrice())*cart.getCount();//折扣价格
                }
            }
            tvCartSumPrice.setText("合计:￥"+sumPrice);//显示实价
            tvCartSavePrice.setText("节省:￥"+(sumPrice-RankPrice));//节省了多少钱
        }

    }
    private int getPrice(String p){//截取的字符是字符串,还得转换成int类型
        String pStr = p.substring(p.indexOf("￥") + 1);
        return Integer.valueOf(pStr);
    }

    @Override
    public void onResume() {
        super.onResume();
        showCartList();
    }
}


