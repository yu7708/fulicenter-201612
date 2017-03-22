package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;
import cn.ucai.fulicenter.ui.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.ui.fragment.CartFragment;
import cn.ucai.fulicenter.ui.fragment.CategoryFragment;
import cn.ucai.fulicenter.ui.fragment.MyFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.MFGT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.btnNewGoods)
    RadioButton btnNewGoods;
    @BindView(R.id.btnBoutique)
    RadioButton btnBoutique;
    @BindView(R.id.btnCategory)
    RadioButton btnCategory;
    @BindView(R.id.btnCart)
    RadioButton btnCart;
    @BindView(R.id.btnMy)
    RadioButton btnMy;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    Unbinder bind;
    int index=0;
    int currentIndex=0;
    Fragment[] mFragment;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    CartFragment mCartFragment;
    MyFragment mMyFragment;

    RadioButton[] mRadioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         bind= ButterKnife.bind(this);
        initFragment();
        initRadioButton();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,mNewGoodsFragment)
                .add(R.id.fragment_container,mBoutiqueFragment)
                .add(R.id.fragment_container,mCategoryFragment)
                .add(R.id.fragment_container,mCartFragment)
                .add(R.id.fragment_container,mMyFragment)
                .hide(mBoutiqueFragment)
                .hide(mCategoryFragment)
                .hide(mMyFragment)
                .hide(mCartFragment)
                .show(mNewGoodsFragment)
                .commit();
    }

    private void initRadioButton() {
        mRadioButton=new RadioButton[5];
        mRadioButton[0]=btnNewGoods;
        mRadioButton[1]=btnBoutique;
        mRadioButton[2]=btnCategory;
        mRadioButton[3]=btnCart;
        mRadioButton[4]=btnMy;
    }

    private void initFragment() {
        mFragment=new Fragment[5];
        mNewGoodsFragment=new NewGoodsFragment();
        mBoutiqueFragment=new BoutiqueFragment();
        mCategoryFragment=new CategoryFragment();
        mCartFragment=new CartFragment();
        mMyFragment=new MyFragment();
        mFragment[0]=mNewGoodsFragment;
        mFragment[1]=mBoutiqueFragment;
        mFragment[2]=mCategoryFragment;
        mFragment[3]=mCartFragment;
        mFragment[4]=mMyFragment;
    }

    public void onCheckedChange(View view) {
switch (view.getId()) {
    case R.id.btnNewGoods:
        index=0;
        break;
    case R.id.btnBoutique:
        index=1;
        break;
    case R.id.btnCategory:
        index=2;
        break;
    case R.id.btnCart:
        if(FuLiCenterApplication.getCurrentUser()==null){
            MFGT.gotoLogin(MainActivity.this, I.REQUEST_CODE_LOGIN_FROM_CART);
        }else{
            index=3;
        }
        break;
    case R.id.btnMy:
        if(FuLiCenterApplication.getCurrentUser()==null){
            MFGT.gotoLogin(MainActivity.this,I.REQUEST_CODE_LOGIN);
        }else{
            index=4;
        }
        break;
}
        setFragment();
    }

    private void setFragment() {
        if(currentIndex!=index){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragment[currentIndex]);
            if(!mFragment[index].isAdded()){
                ft.add(R.id.fragment_container,mFragment[index]);
            }
            ft.show(mFragment[index]).commit();
            currentIndex=index;
        }
    }

    @Override
    protected void onResume() {//在跳转后,返回的是这句话
        super.onResume();
        Log.e(TAG,"index="+index+",currentIndex="+currentIndex);
        if(index==4){
            if(FuLiCenterApplication.getCurrentUser()==null){
                index=0;
            }
            setFragment();
        }
        setRadioButton();
    }

    private void setRadioButton() {//返回现在的按钮
        for (int i=0;i<mRadioButton.length;i++){
            if(i==currentIndex){
                mRadioButton[i].setChecked(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==I.REQUEST_CODE_LOGIN){
                index=4;
            }else if(requestCode==I.REQUEST_CODE_LOGIN_FROM_CART){
                index=3;
            }
            setFragment();
            setRadioButton();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bind!=null){
            bind.unbind();
        }
    }
}
