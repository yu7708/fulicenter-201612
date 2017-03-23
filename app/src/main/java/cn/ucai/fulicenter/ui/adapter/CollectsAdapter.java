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
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.FooterViewHolder;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/15.
 */

public class CollectsAdapter extends RecyclerView.Adapter {
    Context context;
    List<CollectBean> mList;
    boolean isMore;
    CollectBean bean;


    public void setMore(boolean more) {
        isMore = more;
    }
    public void removeCollect(CollectBean bean) {
        mList.remove(bean);
        notifyDataSetChanged();
    }

    public CollectsAdapter(Context context, List<CollectBean> mList) {
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
            vh = new GoodsViewHolder(View.inflate(context, R.layout.item_collects, null));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.setTvFooter(getFooterString());
            return;
        }
        GoodsViewHolder vh = (GoodsViewHolder) holder;
        vh.bind(position);
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


    class GoodsViewHolder extends ViewHolder {
        @BindView(R.id.imageGoods)
        ImageView image;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.dd)
        ImageView dd;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }




        //拿出来放在bind里
        //这样做的好处就是view就是view,adapter就是adapter
        public void bind(int position) {
            bean = mList.get(position);
            tv1.setText(bean.getGoodsName());
            ImageLoader.downloadImg(context, image, bean.getGoodsThumb());
            dd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCollect(bean);
                    notifyDataSetChanged();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoDetails(context, bean.getGoodsId());
                }
            });
        }
    }
}
