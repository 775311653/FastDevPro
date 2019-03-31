package com.mohe.fastdevpro.ui.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.jvm.internal.Intrinsics;

/**
 * Created by xiePing on 2018/10/27 0027.
 * Description:
 */
public class BasePresenter<V extends IView> implements IPresenter<V> {
    @Nullable
    private V mRootView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    public final V getMRootView() {
        return this.mRootView;
    }

    public final void setMRootView(@Nullable V var1) {
        this.mRootView = var1;
    }

    public void attachView(@NotNull V mRootView) {
        Intrinsics.checkParameterIsNotNull(mRootView, "mRootView");
        this.mRootView = mRootView;
    }

    public void detachView() {
        this.mRootView = (V)null;
        if (!this.compositeDisposable.isDisposed()) {
            this.compositeDisposable.clear();
        }

    }

    public final void addSubscription(@NotNull Disposable disposable) {
        Intrinsics.checkParameterIsNotNull(disposable, "disposable");
        this.compositeDisposable.add(disposable);
    }
}
