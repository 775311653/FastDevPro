package com.mohe.fastdevpro.study.sqliteDBDemo;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.bean.CompanyBean;
import com.mohe.fastdevpro.study.sqliteDBDemo.dbTable.CompanyTHelper;
import com.mohe.fastdevpro.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 教程地址
 * https://blog.csdn.net/midnight_time/article/details/80834198
 * https://www.runoob.com/sqlite/sqlite-insert.html
 */
public class SqliteDBDemoActivity extends BaseActivity {

    private CompanyTHelper companyTHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_dbdemo);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        companyTHelper = new CompanyTHelper(this);
    }

    @OnClick({R.id.btn_insert, R.id.btn_query, R.id.btn_update
            , R.id.btn_delete,R.id.btn_changeName,R.id.btn_input_db})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                showInsert();
                break;
            case R.id.btn_query:
                showQuery();
                break;
            case R.id.btn_update:
                showUpdate();
                break;
            case R.id.btn_delete:
                showDelete();
                break;
            case R.id.btn_changeName:
                showChangeTableColumn();
                break;
            case R.id.btn_input_db:
                companyTHelper.inputNewDb(this);
                break;
        }
    }

    /**
     * 展示修改表字段
     */
    private void showChangeTableColumn() {
        companyTHelper.changeTableColumn();
    }

    private void showDelete() {
        companyTHelper.delete(new CompanyBean(1,"墨禾公司2",11,"地址1", 12305.3f));
    }

    private void showUpdate() {
        companyTHelper.update(new CompanyBean(1,"墨禾公司2",11,"地址1", 12305.3f));
    }

    private void showQuery() {
        companyTHelper.query(companyTHelper.getWritableDatabase());
    }

    private void showInsert() {
        try{//防止重复插入
            companyTHelper.insert(new CompanyBean(1,"墨禾公司",11,"地址1", 12305.3f));
        }catch (Exception e){
        }
    }
}
