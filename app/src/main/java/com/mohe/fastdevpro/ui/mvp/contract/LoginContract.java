package com.mohe.fastdevpro.ui.mvp.contract;

import com.mohe.fastdevpro.bean.User;
import com.mohe.fastdevpro.ui.base.IPresenter;
import com.mohe.fastdevpro.ui.base.IView;

/**
 * Created by xiePing on 2018/10/26 0026.
 * Description:
 */
public interface LoginContract {

    interface View extends IView {
        void loginSuccess(User user);
        void loginFail();
        /**
         * 倒计时完成
         */
        void countdownComplete();

        /**
         * 倒计时中
         * @param time 剩余时间
         */
        void countdownNext(String time);
    }

    interface Presenter extends IPresenter<View> {
        void getIdentify();
        void requestLogin(String mobile,String code);
    }
}
