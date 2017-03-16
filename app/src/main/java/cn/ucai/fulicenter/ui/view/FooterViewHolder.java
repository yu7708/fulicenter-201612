package cn.ucai.fulicenter.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvFooter)
        TextView tvFooter;
    public FooterViewHolder(View itemView) {//在外面调用这里得定义为公有
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
    public void setTvFooter(String footerString){//提供给外面修改的方法
        tvFooter.setText(footerString);
    }
    public void setTvFooter(int footerString){
        tvFooter.setText(footerString);
    }
}
