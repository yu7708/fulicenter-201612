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
    List<List<CategoryChildBean>> childBeanList;
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
        initChildData();
    }

    private void initData() {
        elvCategory=new ExpandableListView(getContext());
        groupBeanList=new ArrayList<>();
        childBeanList=new ArrayList<>();
        mAdapter=new CategoryAdapter(getContext(),groupBeanList,childBeanList);
        elvCategory.setAdapter(mAdapter);


    }

    private void initGroupData() {
        model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {

            }


            @Override
            public void onError(String error) {

            }
        });
    }
    private void initChildData(){
        model.loadChildData(getContext(), mParent_id, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
