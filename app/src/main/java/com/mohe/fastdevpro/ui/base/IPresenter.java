package com.mohe.fastdevpro.ui.base;

/**
 * Created by xiePing on 2018/10/27 0027.
 * Description:
 */
public interface IPresenter<V extends IView> {
    void attachView(V mRootView);
    void detachView();
}
