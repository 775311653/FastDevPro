package com.mohe.fastdevpro.study.accessibility;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.bean.ShopSearchBean;
import com.mohe.fastdevpro.ui.base.BaseActivity;
import com.mohe.fastdevpro.utils.GsonUtils;
import com.mohe.fastdevpro.utils.JSONUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XianYuHelperActivity extends BaseActivity {

    @BindView(R.id.et_search_key_word)
    EditText etSearchKeyWord;
    @BindView(R.id.et_contain_content)
    EditText etContainContent;
    @BindView(R.id.rv)
    RecyclerView rv;

    public static final String SHOP_SEARCH_TABLE = "shopSearch";
    public static final String JS_SHOP_DATA_KEY = "jsShopData";
    private BaseQuickAdapter adapter;
    private List<ShopSearchBean> shopSearchBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xian_yu_helper);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        String jsShopData = SPUtils.getInstance(SHOP_SEARCH_TABLE).getString(JS_SHOP_DATA_KEY);
        JSONArray ja = JSONUtil.getJSONArray(jsShopData);
        for (int i = 0; i < ja.length(); i++) {
            ShopSearchBean shopSearchBean = GsonUtils.fromJson(JSONUtil.getJSONObject(ja, i).toString(), ShopSearchBean.class);
            shopSearchBeans.add(shopSearchBean);
        }
    }

    private void initView() {
        adapter = new BaseQuickAdapter<ShopSearchBean, BaseViewHolder>(R.layout.layout_shop_item, shopSearchBeans) {
            @Override
            protected void convert(final BaseViewHolder helper, final ShopSearchBean item) {
//                helper.setText(R.id.et_search_key_word, item.getSearchKeyWord());
//                helper.setText(R.id.et_contain_content, item.getMySpecialWord());
//                helper.setOnClickListener(R.id.btn_save_shop, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        shopSearchBeans.get(helper.getLayoutPosition()).setSearchKeyWord(
//                                ((EditText) helper.getView(R.id.et_search_key_word)).getText().toString());
//                        shopSearchBeans.get(helper.getLayoutPosition()).setMySpecialWord(
//                                ((EditText) helper.getView(R.id.et_contain_content)).getText().toString());
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//                helper.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        shopSearchBeans.remove(item);
//                        adapter.notifyDataSetChanged();
//                    }
//                });
            }

            @Override
            public void onBindViewHolder(@NonNull final BaseViewHolder helper, int position) {
                final ShopSearchBean item = shopSearchBeans.get(position);
                helper.setText(R.id.et_search_key_word, item.getSearchKeyWord());
                helper.setText(R.id.et_contain_content, item.getMySpecialWord());
                helper.setOnClickListener(R.id.btn_save_shop, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shopSearchBeans.get(helper.getLayoutPosition()).setSearchKeyWord(
                                ((EditText) helper.getView(R.id.et_search_key_word)).getText().toString());
                        shopSearchBeans.get(helper.getLayoutPosition()).setMySpecialWord(
                                ((EditText) helper.getView(R.id.et_contain_content)).getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                });
                helper.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shopSearchBeans.remove(item);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    @OnClick({R.id.btnStartAccessibility, R.id.btn_save_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnStartAccessibility:
                startAccessibility();
                break;
            case R.id.btn_save_shop:
                addSearchShop();
                break;
        }
    }

    //添加商品搜索
    private void addSearchShop() {
        ShopSearchBean shopSearchBean = new ShopSearchBean(etSearchKeyWord.getText().toString(), etContainContent.getText().toString());
        shopSearchBeans.add(shopSearchBean);
        String jsShopSearch = GsonUtils.toJson(shopSearchBeans);
        SPUtils.getInstance(SHOP_SEARCH_TABLE).put(JS_SHOP_DATA_KEY, jsShopSearch);
        adapter.notifyDataSetChanged();
    }

    //启动无障碍服务
    private void startAccessibility() {
        Intent intent3 = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent3);
    }

}
