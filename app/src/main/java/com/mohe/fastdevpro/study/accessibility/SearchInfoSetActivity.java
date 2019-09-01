package com.mohe.fastdevpro.study.accessibility;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
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

import static com.mohe.fastdevpro.study.accessibility.XianYuHelperActivity.JS_SHOP_DATA_KEY;
import static com.mohe.fastdevpro.study.accessibility.XianYuHelperActivity.SHOP_SEARCH_TABLE;

public class SearchInfoSetActivity extends BaseActivity {

    @BindView(R.id.etSearchKeyWord)
    EditText etSearchKeyWord;
    @BindView(R.id.etSpecialWord)
    EditText etSpecialWord;
    @BindView(R.id.cbIsClickGood)
    CheckBox cbIsClickGood;
    @BindView(R.id.cbIsClickWant)
    CheckBox cbIsClickWant;
    @BindView(R.id.cbIsLookDynamic)
    CheckBox cbIsLookDynamic;
    @BindView(R.id.cbIsLookSellerEvaluate)
    CheckBox cbIsLookSellerEvaluate;
    @BindView(R.id.etCompareGoodsCnt)
    EditText etCompareGoodsCnt;
    @BindView(R.id.btnSave)
    Button btnSave;

    //点进来的某个要刷单的商品id
    private int index;
    private List<ShopSearchBean> shopSearchBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_info_set);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        index = getIntent().getIntExtra("index", -1);
        String jsShopData = SPUtils.getInstance(SHOP_SEARCH_TABLE).getString(JS_SHOP_DATA_KEY);
        JSONArray ja = JSONUtil.getJSONArray(jsShopData);
        for (int i = 0; i < ja.length(); i++) {
            ShopSearchBean shopSearchBean = GsonUtils.fromJson(JSONUtil.getJSONObject(ja, i).toString(), ShopSearchBean.class);
            shopSearchBeans.add(shopSearchBean);
        }

    }

    private void initView() {
        ShopSearchBean shopSearchBean = shopSearchBeans.get(index);
        etSearchKeyWord.setText(shopSearchBean.getSearchKeyWord());
        etSpecialWord.setText(shopSearchBean.getMySpecialWord());
        etCompareGoodsCnt.setText(shopSearchBean.getCompareGoodsCnt()+"");
        cbIsClickGood.setChecked(shopSearchBean.isClickGood());
        cbIsClickWant.setChecked(shopSearchBean.isClickWant());
        cbIsLookDynamic.setChecked(shopSearchBean.isLookDynamic());
        cbIsLookSellerEvaluate.setChecked(shopSearchBean.isLookSellerEvaluate());
    }

    //更新数据
    private void updateSearchShop(int index) {
        if (!verifyDataIsOk()) {
            return;
        }
        ShopSearchBean shopSearchBean = shopSearchBeans.get(index);
        shopSearchBean.setSearchKeyWord(etSearchKeyWord.getText().toString());
        shopSearchBean.setMySpecialWord(etSpecialWord.getText().toString());
        shopSearchBean.setCompareGoodsCnt(Integer.parseInt(etCompareGoodsCnt.getText().toString()));
        shopSearchBean.setClickGood(cbIsClickGood.isChecked());
        shopSearchBean.setClickWant(cbIsClickWant.isChecked());
        shopSearchBean.setLookDynamic(cbIsLookDynamic.isChecked());
        shopSearchBean.setLookSellerEvaluate(cbIsLookSellerEvaluate.isChecked());

        String jsShopSearch = GsonUtils.toJson(shopSearchBeans);
        SPUtils.getInstance(SHOP_SEARCH_TABLE).put(JS_SHOP_DATA_KEY, jsShopSearch);

        finish();
    }

    //验证参数是否正确
    private boolean verifyDataIsOk() {
        if (StringUtils.isEmpty(etSearchKeyWord.getText().toString())) {
            ToastUtils.showShort("搜索关键字不能为空");
            return false;
        }
        if (StringUtils.isEmpty(etSpecialWord.getText().toString())) {
            ToastUtils.showShort("特殊关键字不能为空");
            return false;
        }
        if (StringUtils.isEmpty(etCompareGoodsCnt.getText().toString())){
            ToastUtils.showShort("对比商家数量不能为空或为负数");
            return false;
        }
        return true;
    }

    @OnClick(R.id.btnSave)
    public void onViewClicked() {
        updateSearchShop(index);
    }
}
