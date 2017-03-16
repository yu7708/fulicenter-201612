package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface ICategoryModel {
void loadGroupData(Context context, OnCompleteListener<CategoryGroupBean[]> listener);
void loadChildData(Context context, int parent_id, OnCompleteListener<CategoryChildBean[]> listener);
}
