package cn.ucai.fulicenter.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DBManager {
    DBOpenHelper mHelper;
    static DBManager dbManager=new DBManager();
    public synchronized void initDB(Context context){//先实例化,只实例化一次,再实例,
        mHelper=new DBOpenHelper(context);
    }
    public static DBManager getInstance(){
        return dbManager;
    }
    public boolean saveUserInfo(User user){
        SQLiteDatabase database=mHelper.getWritableDatabase();
        if(database.isOpen()){
            ContentValues values=new ContentValues();
            values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
            values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
            values.put(UserDao.USER_COLUMN_AVATAR,user.getMavatarId());
            values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
            values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
            values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
            values.put(UserDao.USER_COLUMN_AVATAR_UPDATE_TIME,user.getMavatarLastUpdateTime());
            //return database.insert(UserDao.USER_TABLE_NAME,null,values)!=-1;
            return database.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
            //如果错误返回-1
        }
        return false;
    }
    public User getUserInfo(String username){//上面是写入,现在是读出
        SQLiteDatabase database=mHelper.getReadableDatabase();
        if(database.isOpen()){
            //查询指定名字的数据
            String sql= "select * from "+UserDao.USER_TABLE_NAME
                    +" where "+ UserDao.USER_COLUMN_NAME +"='"+username +"'";
            //database.execSQL(sql);这个返回值为空
            Cursor cursor = database.rawQuery(sql, null);
            if(cursor.moveToNext()){
                User user=new User();
                user.setMuserName(username);
                user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
                user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR)));
                user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
                user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
                user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
                user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_UPDATE_TIME)));
                return user;
            }
        }
        return null;
    }

    public void CloseDB() {
        mHelper.CloseDB();
    }
}
