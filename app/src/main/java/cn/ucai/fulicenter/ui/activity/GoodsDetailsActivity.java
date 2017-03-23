package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2017/3/16.
 */
public class GoodsDetailsActivity extends AppCompatActivity {
    IGoodsModel model;
    int goodsId = 0;
    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;
    @BindView(R.id.tvCurrencyPrice)
    TextView tvCurrencyPrice;
    @BindView(R.id.tvGoodsEnglishName)
    TextView tvGoodsEnglishName;
    @BindView(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;

    GoodsDetailsBean bean;
    @BindView(R.id.ivGoodsCollect)
    ImageView ivGoodsCollect;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);

        goodsId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID, goodsId);

        if (goodsId == 0) {
            MFGT.finish(GoodsDetailsActivity.this);
            return;
        }
        model = new GoodsModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initAddCollect() {
        User user = FuLiCenterApplication.getCurrentUser();
        if (user != null) {
            model.loadCollectStatus(GoodsDetailsActivity.this, goodsId, user.getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean msg) {
                            if (msg != null && msg.isSuccess()) {
                                setCollectStatus(true);
                                Toast.makeText(GoodsDetailsActivity.this, "添加成功", Toast.LENGTH_SHORT);
                            } else {
                                setCollectStatus(false);
                                Toast.makeText(GoodsDetailsActivity.this, "添加失败", Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            setCollectStatus(false);
                            Toast.makeText(GoodsDetailsActivity.this, "添加失败", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }

    private void setCollectStatus(boolean isCollects) {//设置收藏图片的状态
        ivGoodsCollect.setImageResource(isCollects?R.mipmap.bg_collect_out:R.mipmap.bg_collect_in);
    }

    private void initData() {
        if (bean == null) {//避免多次加载
            model.loadData(GoodsDetailsActivity.this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
                @Override
                public void onSuccess(GoodsDetailsBean result) {
                    if (result != null) {
                        // showDetails(result);//显示结果
                        bean = result;
                        showDetails();
                    }
                    initAddCollect();
                }


                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast(error);
                }
            });
        }
    }

    private void showDetails() {
        tvCurrencyPrice.setText("   原价：" + bean.getCurrencyPrice());
        tvGoodsName.setText(bean.getGoodsName());
        tvGoodsEnglishName.setText(bean.getGoodsEnglishName());
        tvGoodsPrice.setText("   特卖：" + bean.getShopPrice());
        salv.startPlayLoop(indicator, getAlbumUrl(bean), getAlbumCount(bean));
        wvGoodBrief.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
        //简介
    }

    private int getAlbumCount(GoodsDetailsBean bean) {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            return bean.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumUrl(GoodsDetailsBean bean) {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            AlbumsBean[] albums = bean.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < albums.length; i++) {
                    urls[i] = albums[0].getImgUrl();
                }
                return urls;
            }
        }
        return null;
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {//点击返回键退出
        MFGT.finish(GoodsDetailsActivity.this);
    }

}
