package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.collect_goods_num)
    TextView collectGoodsNum;

    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = FuLiCenterApplication.getCurrentUser();//得到application里的数据
        //如果为空,没登陆跳转至登录页面,否则显示信息
        if(user==null){
            MFGT.gotoLogin(getActivity());
        }else{
            showUserInfo();
        }
    }
    @OnClick({R.id.btn_center_setting,R.id.ll_avatar})//点击设置或者二维码那一行的监听
    public void goSettings(){
        MFGT.gotoSettings(getActivity());
    }
    private void showUserInfo() {
        tvUsername.setText(user.getMuserName());
        ImageLoader.downloadImg(getContext(),ivAvatar,user.getAvatar());
    }
}
