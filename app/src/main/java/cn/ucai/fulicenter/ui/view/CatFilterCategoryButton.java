package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.MainActivity;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;

/**
 * Created by Administrator on 2017/3/18.
 */

public class CatFilterCategoryButton extends Button {
    boolean isExpand;
    Context mContext;
    PopupWindow mPopupWindow;

    GridView gv;
    catfilterChildAdapter mAdapter;
    ArrayList<CategoryChildBean> mList;

    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        showArrow();
    }

    private void initPopu() {//设置弹窗
        if(mPopupWindow==null) {
            mPopupWindow = new PopupWindow(mContext);
            mPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            //mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xaaaaa));

            mPopupWindow.setContentView(gv);
        }
        mPopupWindow.showAsDropDown(this);

    }

    private void showArrow() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpand) {
                    initPopu();
                } else {
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                Drawable end = getResources().getDrawable(isExpand ? R.drawable.arrow2_down : R.drawable.arrow2_up);
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                isExpand = !isExpand;
            }
        });
    }

    public void initView(String groupName, ArrayList<CategoryChildBean> list) {
        if(groupName==null || list==null){
            CommonUtils.showShortToast("小类数据异常");
            return;
        }
        this.setText(groupName);
        mList=list;
        gv = new GridView(mContext);
        mAdapter = new catfilterChildAdapter(mContext, mList);
        gv.setAdapter(mAdapter);
    }


    class catfilterChildAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<CategoryChildBean> mList;

        public catfilterChildAdapter(Context mContext, ArrayList<CategoryChildBean> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList != null ? mList.size() : 0;
        }

        @Override
        public CategoryChildBean getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CatFilterViewHolder vh=null;
            if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_cat_filter, null);
                vh=new CatFilterViewHolder(convertView);
                convertView.setTag(vh);
            }else{
                vh= (CatFilterViewHolder) convertView.getTag();
            }
            CategoryChildBean bean=mList.get(position);
            vh.tvChild.setText(bean.getName());
            ImageLoader.downloadImg(mContext,vh.ivChild,bean.getImageUrl());
            return convertView;
        }

         class CatFilterViewHolder {
            @BindView(R.id.ivChild)
            ImageView ivChild;
            @BindView(R.id.tvChild)
            TextView tvChild;
            @BindView(R.id.llChild)
            LinearLayout llChild;

            CatFilterViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
