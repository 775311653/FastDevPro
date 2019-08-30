package com.mohe.fastdevpro.bean;

public class ShopSearchBean {
    //搜索的关键字
    private String searchKeyWord;
    //我们商品的标题名字
    private String mySpecialWord;
    //是否点赞
    private boolean isClickGood = true;
    //是否点击我想要
    private boolean isClickWant = true;
    //是否看动态
    private boolean isLookDynamic = true;
    //是否查看卖家评价
    private boolean isLookSellerEvaluate = true;
    //比较商品的数量
    private int compareGoodsCnt = 3;

    public ShopSearchBean() {
    }

    public ShopSearchBean(String searchKeyWord, String mySpecialWord) {
        this.searchKeyWord = searchKeyWord;
        this.mySpecialWord = mySpecialWord;
    }

    public String getSearchKeyWord() {
        return searchKeyWord;
    }

    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }

    public String getMySpecialWord() {
        return mySpecialWord;
    }

    public void setMySpecialWord(String mySpecialWord) {
        this.mySpecialWord = mySpecialWord;
    }

    public boolean isClickGood() {
        return isClickGood;
    }

    public void setClickGood(boolean clickGood) {
        isClickGood = clickGood;
    }

    public boolean isClickWant() {
        return isClickWant;
    }

    public void setClickWant(boolean clickWant) {
        isClickWant = clickWant;
    }

    public boolean isLookDynamic() {
        return isLookDynamic;
    }

    public void setLookDynamic(boolean lookDynamic) {
        isLookDynamic = lookDynamic;
    }

    public boolean isLookSellerEvaluate() {
        return isLookSellerEvaluate;
    }

    public void setLookSellerEvaluate(boolean lookSellerEvaluate) {
        isLookSellerEvaluate = lookSellerEvaluate;
    }

    public int getCompareGoodsCnt() {
        return compareGoodsCnt;
    }

    public void setCompareGoodsCnt(int compareGoodsCnt) {
        this.compareGoodsCnt = compareGoodsCnt;
    }
}
