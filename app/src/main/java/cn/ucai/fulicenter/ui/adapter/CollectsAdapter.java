package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
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
    IGoodsModel mModel=new GoodsModel();

    public void setMore(boolean more) {
        isMore = more;
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
        bean = mList.get(position);
        vh.tv1.setText(bean.getGoodsName());
        ImageLoader.downloadImg(context, vh.image, bean.getGoodsThumb());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDetails(context, bean.getGoodsId());
            }
        });
        vh.dd.setTag(bean);

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
        @OnClick(R.id.dd)
        public void onClick(){
            CollectBean collectBean= (CollectBean) dd.getTag();
            removeCollect(collectBean.getGoodsId(),collectBean);
        }
        public void removeCollect(int goodsId, final CollectBean bean) {
            mModel.loadCollectStatus(context, I.ACTION_DELETE_COLLECT, goodsId,
                    FuLiCenterApplication.getCurrentUser().getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if(result!=null&&result.isSuccess()){
                                mList.remove(bean);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }
}
