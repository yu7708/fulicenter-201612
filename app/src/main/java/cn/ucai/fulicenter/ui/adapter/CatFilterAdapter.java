package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/20.
 */

public class CatFilterAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<CategoryChildBean> mList;
    String groupName;
    public CatFilterAdapter(Context mContext, ArrayList<CategoryChildBean> mList,String name) {
        this.mContext = mContext;
        this.mList = mList;
        groupName = name;
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
        final CategoryChildBean bean=mList.get(position);
        vh.tvChild.setText(bean.getName());
        ImageLoader.downloadImg(mContext,vh.ivChild,bean.getImageUrl());
        vh.llChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoCategoryChild(mContext,bean.getId(),groupName,mList);
            }
        });
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
