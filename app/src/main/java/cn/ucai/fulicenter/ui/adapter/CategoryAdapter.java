package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/16.
 */

public class CategoryAdapter extends BaseExpandableListAdapter{
    Context context;
    List<CategoryGroupBean> groupBeanList;
    List<List<CategoryChildBean>> childBeanList;

    public CategoryAdapter(Context context, List<CategoryGroupBean> groupBeanList, List<List<CategoryChildBean>> childBeanList) {
        this.context = context;
        this.groupBeanList = groupBeanList;
        this.childBeanList = childBeanList;
    }

    @Override
    public int getGroupCount() {
        return groupBeanList!=null?groupBeanList.size():0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childBeanList!=null&&childBeanList.get(groupPosition)!=null
                ?childBeanList.get(groupPosition).size():0;
        //要拿到小的数量，先判断小的是否为空，在判断他的上一级数组传来的是否为空，
        //不同的得到的数量也是不同的
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {//得到当前的数
        return groupBeanList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return childBeanList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.item_group_category, null);
            groupHolder=new GroupHolder();
            groupHolder.ivGroup= (ImageView) convertView.findViewById(R.id.ivGroup);
            groupHolder.ivExpand= (ImageView) convertView.findViewById(R.id.ivExpand);
            groupHolder.tvGroup= (TextView) convertView.findViewById(R.id.tvGroup);
            convertView.setTag(groupHolder);
        }
        else{
            groupHolder= (GroupHolder) convertView.getTag();
        }
        //先判断第一屏,然后就是绑定数据
        CategoryGroupBean group= (CategoryGroupBean) getGroup(groupPosition);
        groupHolder.tvGroup.setText(group.getName());
        ImageLoader.downloadImg(context,groupHolder.ivGroup,group.getImageUrl());
        if(isExpanded){
            groupHolder.ivExpand.setImageResource(R.mipmap.expand_off);
        }else{
            groupHolder.ivExpand.setImageResource(R.mipmap.expand_on);
        }
        //先有对象再传数据
        return convertView;
    }
    class GroupHolder{
        ImageView ivGroup,ivExpand;
        TextView tvGroup;
    }
    class ChildHolder{
        ImageView ivChild;
        TextView tvChild;
        LinearLayout llChild;
    }
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder=null;
        if(convertView==null){
            convertView=View.inflate(context,R.layout.item_child_category,null);
            holder=new ChildHolder();
            holder.ivChild= (ImageView) convertView.findViewById(R.id.ivChild);
            holder.tvChild= (TextView) convertView.findViewById(R.id.tvChild);
            holder.llChild= (LinearLayout) convertView.findViewById(R.id.llChild);
            convertView.setTag(holder);
        }else{
            holder= (ChildHolder) convertView.getTag();
        }
        final CategoryChildBean list= (CategoryChildBean) getChild(groupPosition,childPosition);
        holder.tvChild.setText(list.getName());
        ImageLoader.downloadImg(context,holder.ivChild,list.getImageUrl());
        holder.llChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoCategoryChild(context,list.getId(),getGroup(groupPosition).getName());
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
