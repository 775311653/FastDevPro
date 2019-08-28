package com.mohe.fastdevpro.bean;

public class ShopSearchBean {
    //搜索的关键字
    private String searchKeyWord;
    //我们商品的标题名字
    private String mySpecialWord;

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
}
