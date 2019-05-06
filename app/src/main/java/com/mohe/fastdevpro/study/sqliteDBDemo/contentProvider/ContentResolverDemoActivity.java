package com.mohe.fastdevpro.study.sqliteDBDemo.contentProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.bean.CompanyBean;
import com.mohe.fastdevpro.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentResolverDemoActivity extends BaseActivity {

    private String id_key="id";
    private String name_key="name";
    private String age_key="age";
    private String address_key="address";
    private String salary_key="salary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_resolver_demo);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

    }

    @OnClick({R.id.btn_insert, R.id.btn_delete, R.id.btn_update, R.id.btn_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                showInsert();
                break;
            case R.id.btn_delete:
                showDelete();
                break;
            case R.id.btn_update:
                showUpdate();
                break;
            case R.id.btn_query:
                showQuery();
                break;
        }
    }

    private void showInsert() {
        getContentResolver().insert(DBContentProvider.URI_COMPANY
                ,getContentValues(new CompanyBean(3,"墨禾公司3",12,"地址3",33333)));
    }

    private void showDelete() {
        getContentResolver().delete(DBContentProvider.URI_COMPANY,"id=?",new String[]{"3"});
    }

    private void showUpdate() {
        ContentValues contentValues=getContentValues(new CompanyBean(3,"墨禾公司4",12,"地址3",33333));
        getContentResolver().update(DBContentProvider.URI_COMPANY,contentValues,"id=?",new String[]{"3"});
    }

    private void showQuery() {
        try {
            ArrayList<CompanyBean> companies=new ArrayList<>();
            Cursor cursor=getContentResolver().query(DBContentProvider.URI_COMPANY
                    ,null,"id>?",new String[]{"0"},null);
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
        }catch (Exception e){
            LogUtils.i(e.getMessage());
        }
    }

    /**
     * 把companyBean转化为可以数据库保存的ContentValues
     */
    @NonNull
    private ContentValues getContentValues(CompanyBean companyBean) {
        ContentValues values=new ContentValues();
        values.put("id",companyBean.getId());
        values.put("name",companyBean.getName());
        values.put("age",companyBean.getAge());
        values.put("address",companyBean.getAddress());
        values.put("salary",companyBean.getSalary());

        return values;
    }

}
