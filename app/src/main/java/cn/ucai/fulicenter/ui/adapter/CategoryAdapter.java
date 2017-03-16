package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;

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
    public Object getGroup(int groupPosition) {//得到当前的数
        return groupBeanList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
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
        if(convertView==null){

        }
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
