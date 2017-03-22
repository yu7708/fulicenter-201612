package cn.ucai.fulicenter.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.ui.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.ui.activity.CategoryChildActivity;
import cn.ucai.fulicenter.ui.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.ui.activity.LoginActivity;
import cn.ucai.fulicenter.ui.activity.MainActivity;
import cn.ucai.fulicenter.ui.activity.RegisterActivity;
import cn.ucai.fulicenter.ui.activity.SettingsActivity;
import cn.ucai.fulicenter.ui.activity.UpdateNickActivity;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity(Activity activity,Class cls){
        activity.startActivity(new Intent(activity,cls));
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity(Activity activity,Intent intent){
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoMain(Activity activity){
        startActivity(activity, MainActivity.class);
    }
    public static void gotoBoutiqueChild(Context activity, BoutiqueBean bean){
        startActivity((Activity) activity,new Intent(activity, BoutiqueChildActivity.class)
        .putExtra(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(bean.getId()))
        .putExtra(I.Boutique.TITLE,bean.getTitle()));
    }

    public static void gotoDetails(Context activity, int goodsId) {
       startActivity((Activity) activity,new Intent(activity,GoodsDetailsActivity.class)
               .putExtra(I.Goods.KEY_GOODS_ID,goodsId));
    }

    public static void gotoCategoryChild(Context activity, int cat_id, String groupName, ArrayList<CategoryChildBean> list) {
        startActivity((Activity) activity,new Intent(activity, CategoryChildActivity.class)
        .putExtra(I.NewAndBoutiqueGoods.CAT_ID,cat_id)
        .putExtra(I.CategoryGroup.NAME,groupName)
        .putExtra(I.CategoryChild.DATA,list));//这里是跳的哪里
    }

    public static void gotoLogin(Activity activity, int requestCode) {
        //startActivity(activity,LoginActivity.class);
        startActivityForResult(activity,new Intent(activity,LoginActivity.class),requestCode);
    }

    public static void gotoRegister(Activity activity) {//设置返回结果集,使跳转的登录页面有返回集,
        startActivityForResult(activity,new Intent(activity,RegisterActivity.class),I.REQUEST_CODE_REGISTER);
    }
    public static void startActivityForResult(Activity activity,Intent intent,int requestCode){
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoSettings(Activity activity) {
        startActivity(activity,SettingsActivity.class);
    }

    public static void gotoUpdateNick(Activity activity) {
        startActivity(activity,UpdateNickActivity.class);
    }
}
