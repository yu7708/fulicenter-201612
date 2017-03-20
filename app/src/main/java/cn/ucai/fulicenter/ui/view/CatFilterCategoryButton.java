package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;

/**
 * Created by Administrator on 2017/3/18.
 */

public class CatFilterCategoryButton extends Button{
    boolean isExpand;
    Context mContext;
    PopupWindow mPopupWindow;


    CategoryAdapter mAdapter;
    LinearLayoutManager mManager;
    List<CategoryGroupBean> groupBeanList;
    List<List<CategoryChildBean>> childBeanList;



    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        showArrow();
    }

    private void initPopu() {//设置弹窗
        mPopupWindow=new PopupWindow(mContext);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xaaaaa));
       /* TextView tv=new TextView(mContext);
        tv.setText("CatFilterCategoryButton");
        tv.setTextColor(getResources().getColor(R.color.red));
        mPopupWindow.setContentView(tv);*/
        mAdapter=new CategoryAdapter(mContext,groupBeanList,childBeanList);
        mManager=new LinearLayoutManager(mContext);
        
        mPopupWindow.showAsDropDown(this);

    }

    private void showArrow(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isExpand){
                    initPopu();
                }else{
                    if(mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();}
                }
                Drawable end=getResources().getDrawable(isExpand? R.drawable.arrow2_down:R.drawable.arrow2_up);
                setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,end,null);
                isExpand = !isExpand;
            }
        });
    }
}
