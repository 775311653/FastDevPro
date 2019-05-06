package com.mohe.fastdevpro.study.sqliteDBDemo.dbTable;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.mohe.fastdevpro.bean.CompanyBean;
import com.mohe.fastdevpro.utils.CommonUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by xiePing on 2019/5/3 0003.
 * Description:公司数据库表的帮助类，通用的增删改查操作。
 * 修改表名，修改字段的功能
 */
public class CompanyTHelper extends SQLiteOpenHelper {

    private static final int VERSION=1;
    public static final String DBNAME="test_db";
    public static final String COMPANY_TABLE_NAME="company";

    private String id_key="id";
    private String name_key="name";
    private String age_key="age";
    private String address_key="address";
    private String salary_key="salary";

    private String dbPath="";

    public CompanyTHelper(Context context) {
        this(context, DBNAME, null,VERSION);
    }

    public CompanyTHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        String nameKey=SPUtils.getInstance().getString("name");
        if (!StringUtils.isEmpty(nameKey)) name_key=nameKey;

        dbPath=context.getCacheDir().getPath()+"/db/"+"test_db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //sqlite是大小写不敏感的语言。
        // 使用create table table_name (id int primary key not null,name text);
        // 格式是列名 类型 条件。其中条件可以没有,primary key是主键的意思。
        String sql="create table company(id int primary key not null,name text,age integer,address text,salary real)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(CompanyBean companyBean){
        ContentValues values = getContentValues(companyBean);
        getWritableDatabase().insert(COMPANY_TABLE_NAME,null,values);
    }

    /**
     * 把companyBean转化为可以数据库保存的ContentValues
     */
    @NonNull
    private ContentValues getContentValues(CompanyBean companyBean) {
        ContentValues values=new ContentValues();
        values.put(id_key,companyBean.getId());
        values.put(name_key,companyBean.getName());
        values.put(age_key,companyBean.getAge());
        values.put(address_key,companyBean.getAddress());
        values.put(salary_key,companyBean.getSalary());

        LogUtils.i(name_key,companyBean.getName());
        return values;
    }

    public ArrayList<CompanyBean> query(SQLiteDatabase db){
        ArrayList<CompanyBean> companies=new ArrayList<>();
        //搜索id>0的公司
        Cursor cursor=db.query(COMPANY_TABLE_NAME
                ,null//查询列如果只选几列，那么没选中的列就无法查询
                ,id_key+">?"
                ,new String[]{"0"}//里面可以有多个条件。比如上面如果是id=?，下面多个值就更好理解。
                ,null,null,null);
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(id_key));
            String name=cursor.getString(cursor.getColumnIndex(name_key));
            LogUtils.i(name_key,name);
            int age=cursor.getInt(cursor.getColumnIndex(age_key));
            String address=cursor.getString(cursor.getColumnIndex(address_key));

            float salary = 0;
            //由于下面写了修改数据库字段的方法，把salary的字段给去掉了，所以就只在name时赋值
            if (!name_key.equals("company_name")){
                salary=cursor.getFloat(cursor.getColumnIndex(salary_key));
            }

            CompanyBean companyBean=new CompanyBean(id,name,age,address,salary);
            companies.add(companyBean);
        }
        cursor.close();

        for (int i = 0; i < companies.size(); i++) {
            CompanyBean companyBean = companies.get(i);
            LogUtils.i(GsonUtils.toJson(companyBean));
        }
        return companies;
    }

    public void update(CompanyBean companyBean){
        ContentValues contentValues=getContentValues(companyBean);
        getWritableDatabase().update(COMPANY_TABLE_NAME,contentValues,id_key+"=?",new String[]{"1"});
    }

    public void delete(CompanyBean companyBean){
        int id=companyBean.getId();
        getWritableDatabase().delete(COMPANY_TABLE_NAME,id_key+"=?",new String[]{id+""});
    }

    /**
     * 复制旧表内容到新表，修改了字段name为company_name
     */
    public void changeTableColumn(){
        //改company表名为temp_company
        String sqlRename="alter table company rename to temp_company";
        getWritableDatabase().execSQL(sqlRename);
        //重新创建一个表名为company的空表，去掉salary字段，修改name字段为company_name;
        String sqlCreate="create table company(id int primary key not null,company_name text,age integer,address text)";
        getWritableDatabase().execSQL(sqlCreate);

        name_key="company_name";
        SPUtils.getInstance().put("name",name_key);

        //将旧表内容根据新的列名来添加
        String sqlCopyChange="insert into company (id,company_name,age,address) select id,name,age,address from temp_company";
        getWritableDatabase().execSQL(sqlCopyChange);
        //删除临时表
        String sqlDeleteTable="drop table temp_company";
        getWritableDatabase().execSQL(sqlDeleteTable);
    }

    /**
     * 导入assets文件夹下的test_db数据库进来,并查询
     */
    public void inputNewDb(Context context){
        AssetManager assetManager=context.getAssets();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is=assetManager.open("test_db");
            FileUtils.createFileByDeleteOldFile(dbPath);
            fos=new FileOutputStream(dbPath);
            CommonUtils.copyFile(is,fos);
            //把数据库文件打开成数据库
            SQLiteDatabase newDb= SQLiteDatabase.openOrCreateDatabase(dbPath,null);
            //查询新数据库
            query(newDb);
        }catch (Exception e){
            LogUtils.i(e.getMessage());
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }
    }


}
