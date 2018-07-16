package com.mohe.fastdevpro.ui.base;

import java.lang.ref.WeakReference;

/**
 * Created by xiePing on 2018/7/16 0016.
 */
public abstract class BasePresenter<M,V> {
    public M mModel;

    public WeakReference<V> mView;


    public void attachModelView(M pModel, V pView) {

        mView = new WeakReference<V>(pView);

        this.mModel = pModel;
    }


    public V getView() {
        if (isAttach()) {
            return mView.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
        return null != mView && null != mView.get();
    }


    public void onDettach() {
        if (null != mView) {
            mView.clear();
            mView = null;
        }
    }
}
