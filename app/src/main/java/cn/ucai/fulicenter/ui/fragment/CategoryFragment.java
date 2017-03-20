package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;
    ICategoryModel model;
    CategoryAdapter mAdapter;
    int mParent_id=0;
    List<CategoryGroupBean> groupBeanList;
    ArrayList<ArrayList<CategoryChildBean>> childBeanList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model=new CategoryModel();

        initData();
        initGroupData();
    }

    private void initData() {
        groupBeanList=new ArrayList<>();
        childBeanList=new ArrayList<>();
        mAdapter=new CategoryAdapter(getContext(),groupBeanList,childBeanList);
        elvCategory.setAdapter(mAdapter);
        elvCategory.setGroupIndicator(null);//二级分类的前面的小标不显示

    }

    private void initGroupData() {
        model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
            if(result!=null){
                ArrayList<CategoryGroupBean> list= ResultUtils.array2List(result);
                groupBeanList.clear();
                groupBeanList.addAll(list);
                for(int i=0;i<groupBeanList.size();i++){
                    childBeanList.add(new ArrayList<CategoryChildBean>());
                    initChildData(list.get(i).getId(),i);
                }
            }
            }


            @Override
            public void onError(String error) {

            }
        });
    }
    private void initChildData(int mParent_id, final int index){
        model.loadChildData(getContext(), mParent_id, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
            if(result!=null){
                ArrayList<CategoryChildBean> list=ResultUtils.array2List(result);
                childBeanList.set(index,list);
            }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
