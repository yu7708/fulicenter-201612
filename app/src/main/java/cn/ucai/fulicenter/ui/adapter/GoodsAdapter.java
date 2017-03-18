package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
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

    int sortBy=I.SORT_BY_ADDTIME_DESC;//默认为降序

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

    public void setSortBy(int sortBy) {//生成对应的方法,然后调用sortBy()
        this.sortBy = sortBy;
        sortBy();
    }

    private void sortBy(){//比较器用的是collection收集
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean l, NewGoodsBean r) {
                int result=0;
                switch (sortBy){
                    case I.SORT_BY_ADDTIME_ASC:
                        result= (int) (l.getAddTime()-r.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result= (int) (r.getAddTime()-l.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_ASC://价格前面有￥符号,要截取
                        result=getPrice(l.getCurrencyPrice())-getPrice(r.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result=getPrice(r.getCurrencyPrice())-getPrice(l.getCurrencyPrice());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();//每次更新都得记住要刷新,不然不知道改变
    }
    private int getPrice(String p){//截取的字符是字符串,还得转换成int类型
        String pStr = p.substring(p.indexOf("￥") + 1);
        return Integer.valueOf(pStr);
    }
}
