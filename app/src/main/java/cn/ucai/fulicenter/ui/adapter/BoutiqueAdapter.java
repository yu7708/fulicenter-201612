package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.BoutiqueChildActivity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context context;
    List<BoutiqueBean> mList;



    public BoutiqueAdapter(Context context, List<BoutiqueBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = null;
            vh = new BoutiqueViewHolder(View.inflate(context, R.layout.item_boutique, null));

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BoutiqueViewHolder vh = (BoutiqueViewHolder) holder;
        final BoutiqueBean bean = mList.get(position);
        ImageLoader.downloadImg(context,vh.ivBoutique,bean.getImageurl());
        vh.tvBoutiqueTitle.setText(bean.getTitle());
        vh.tvBoutiqueName.setText(bean.getName());
        vh.tvBoutiqueDescription.setText(bean.getDescription());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,BoutiqueChildActivity.class)
                .putExtra(I.NewAndBoutiqueGoods.CAT_ID,bean.getId())
                .putExtra(I.Boutique.TITLE,bean.getTitle()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList != null ? mList.size()  : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? I.TYPE_FOOTER : I.TYPE_ITEM;
    }



     class BoutiqueViewHolder extends ViewHolder{
        @BindView(R.id.ivBoutique)
        ImageView ivBoutique;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView tvBoutiqueDescription;

         BoutiqueViewHolder(View view) {
             super(view);
            ButterKnife.bind(this, view);
        }
    }
}
