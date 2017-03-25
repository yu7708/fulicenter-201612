package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/24.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context context;
    List<CartBean> mList;
    CompoundButton.OnCheckedChangeListener listener;
    View.OnClickListener countAddListener,countDelListener;
    public CartAdapter(Context context, List<CartBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setListener(CompoundButton.OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setCountAddListener(View.OnClickListener countAddListener) {
        this.countAddListener = countAddListener;
    }

    public void setCountDelListener(View.OnClickListener countDelListener) {
        this.countDelListener = countDelListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_cart, null);
        ViewHolder holder= new CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        CartViewHolder holder= (CartViewHolder) parentHolder;
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    class CartViewHolder extends ViewHolder{
        @BindView(R.id.cb_cart_selected)
        CheckBox cbCartSelected;
        @BindView(R.id.iv_cart_thumb)
        ImageView ivCartThumb;
        @BindView(R.id.tv_cart_good_name)
        TextView tvCartGoodName;
        @BindView(R.id.iv_cart_add)
        ImageView ivCartAdd;
        @BindView(R.id.tv_cart_count)
        TextView tvCartCount;
        @BindView(R.id.iv_cart_del)
        ImageView ivCartDel;
        @BindView(R.id.tv_cart_price)
        TextView tvCartPrice;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            CartBean bean = mList.get(position);
            tvCartCount.setText("("+bean.getCount()+")");
            cbCartSelected.setChecked(bean.isChecked());//直接true,点击就不会商品的添加不变
            GoodsDetailsBean goods=bean.getGoods();
            ivCartAdd.setTag(bean);
            ivCartDel.setTag(bean);
            ivCartAdd.setOnClickListener(countAddListener);
            ivCartDel.setOnClickListener(countDelListener);
            if(goods!=null){
                ImageLoader.downloadImg(context,ivCartThumb,goods.getGoodsThumb());
                tvCartGoodName.setText(goods.getGoodsName());
                tvCartPrice.setText(goods.getCurrencyPrice());
            }
            cbCartSelected.setTag(position);//传个position好判断是哪个被选中
            cbCartSelected.setOnCheckedChangeListener(listener);
            //提取出了全局的点击事件,设置成setter,是为了外面能调么
        }
    }
}
