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
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.ui.view.FooterViewHolder;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/15.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context context;
    List<NewGoodsBean> mList;
    boolean isMore;

    public void setMore(boolean more) {
        isMore = more;
    }

    public GoodsAdapter(Context context, List<NewGoodsBean> mList) {
        this.context = context;
        this.mList = mList;
        isMore = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh=null;
        if(viewType==I.TYPE_FOOTER){
            vh=new FooterViewHolder(View.inflate(context, R.layout.item_footer, null));
        }else{
            vh = new GoodsViewHolder(View.inflate(context, R.layout.newgoods, null));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(getItemViewType(position)==I.TYPE_FOOTER){
            FooterViewHolder vh= (FooterViewHolder) holder;
            vh.setTvFooter(getFooterString());
            return;
        }
        GoodsViewHolder vh = (GoodsViewHolder) holder;
        final NewGoodsBean bean = mList.get(position);
        vh.tv1.setText(bean.getGoodsName());
        vh.tv2.setText(bean.getCurrencyPrice());
        ImageLoader.downloadImg(context, vh.image, bean.getGoodsThumb());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDetails(context,bean.getGoodsId());
            }
        });
    }

    private int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position==getItemCount()-1? I.TYPE_FOOTER:I.TYPE_ITEM;
    }

    public boolean isMore() {
        return isMore;
    }

    class GoodsViewHolder extends ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /*class FooterViewHolder extends ViewHolder{
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }*/
}
