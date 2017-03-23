package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/15.
 */

public class NewGoodsModel implements INewGoodsModel{
    @Override
    public void loadData(Context context,int cat_id, int pageId, OnCompleteListener listener) {
        String url=I.REQUEST_FIND_NEW_BOUTIQUE_GOODS;
        if(cat_id>0){
            url=I.REQUEST_FIND_GOODS_DETAILS;
        }
        OkHttpUtils<NewGoodsBean[]> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(url)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(cat_id))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String .valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
}
