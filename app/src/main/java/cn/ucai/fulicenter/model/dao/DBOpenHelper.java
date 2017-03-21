package cn.ucai.fulicenter.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.fulicenter.application.FuLiCenterApplication;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DBOpenHelper extends SQLiteOpenHelper{
    //创建一个表,数据库三件套,打开数据库,数据库操作,逻辑放在userDao里面
    private static final String CREATE_USER_TABLE=
            "CREATE TABLE " + UserDao.USER_TABLE_NAME + "("
            + UserDao.USER_COLUMN_NAME + " TEXT PRIMARY KEY,"
            + UserDao.USER_COLUMN_NICK + " TEXT,"
            + UserDao.USER_COLUMN_AVATAR + " INTEGER,"
            + UserDao.USER_COLUMN_AVATAR_PATH + " TEXT,"
            + UserDao.USER_COLUMN_AVATAR_SUFFIX + " INTEGER,"
            + UserDao.USER_COLUMN_AVATAR_TYPE + " TEXT,"
            + UserDao.USER_COLUMN_AVATAR_UPDATE_TIME + " TEXT);";

    private static final int DATABASE_VERSION=1;//版本号

    //实例化
    private static DBOpenHelper instance;
    public static DBOpenHelper getInstance(Context context){
        if(instance==null){
            instance=new DBOpenHelper(context);
        }
        return instance;
    }
    public DBOpenHelper(Context context) {
        super(context, getDatabaseNames(), null,DATABASE_VERSION);
    }
    private static String getDatabaseNames(){//拿到数据库的名称
        return "cn.ucai.fulicenter.db";
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);//执行,然后就是关闭,先在此类实例化instance
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void CloseDB(){//关闭数据库
        if(instance!=null){
            instance.close();
        }
        instance=null;//关闭后设置为空,不然总是释放
    }//创建,关闭,都搞完了,去DBManager,进行数据库的增删查改
}
