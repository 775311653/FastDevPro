package com.mohe.fastdevpro.ui.mvp.presenter;

import com.mohe.fastdevpro.bean.User;
import com.mohe.fastdevpro.net.GithubService;
import com.mohe.fastdevpro.ui.base.BasePresenter;
import com.mohe.fastdevpro.ui.mvp.contract.LoginContract;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiePing on 2018/10/26 0026.
 * Description:
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    @Override
    public void getIdentify() {
        // interval隔一秒发一次，到120结束
        Disposable mDisposable = Observable
                .interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .take(120)
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onComplete() {
                        getMRootView().countdownComplete();
                    }
                    @Override
                    public void onError(Throwable e) {
                        getMRootView().showError("倒计时出现错误！");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        getMRootView().countdownNext(String.valueOf(Math.abs(aLong - 120)));
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void requestLogin(String mobile, String code) {
        if (mobile.length()!=11){
            getMRootView().showError("手机号码不正确");
            return;
        }
        if(code.length() != 6){
            getMRootView().showError("验证码不正确");
            return;
        }

        getMRootView().showLoading();
        GithubService.createGithubService()
                .getUser("simplezhli")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        getMRootView().hideLoading();
                    }
                })
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(User user) {
                        getMRootView().showError("登录成功");
                        getMRootView().loginSuccess(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMRootView().showError("登录失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
