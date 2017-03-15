package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
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

/**
 * Created by Administrator on 2017/3/15.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context context;
    List<BoutiqueBean> mList;
    boolean isMore;

    public void setMore(boolean more) {
        isMore = more;
    }

    public BoutiqueAdapter(Context context, List<BoutiqueBean> mList) {
        this.context = context;
        this.mList = mList;
        isMore = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = null;
        if (viewType == I.TYPE_FOOTER) {
            vh = new FooterViewHolder(View.inflate(context, R.layout.item_footer, null));
        } else {
            vh = new BoutiqueViewHolder(View.inflate(context, R.layout.item_boutique, null));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.tvFooter.setText(getFooterString());
            return;
        }
        BoutiqueViewHolder vh = (BoutiqueViewHolder) holder;
        BoutiqueBean bean = mList.get(position);
        ImageLoader.downloadImg(context,vh.ivBoutique,bean.getImageurl());
        vh.tvBoutiqueTitle.setText(bean.getTitle());
        vh.tvBoutiqueName.setText(bean.getName());
        vh.tvBoutiqueDescription.setText(bean.getDescription());
    }

    private int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? I.TYPE_FOOTER : I.TYPE_ITEM;
    }

    public boolean isMore() {
        return isMore;
    }


    class FooterViewHolder extends ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
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
