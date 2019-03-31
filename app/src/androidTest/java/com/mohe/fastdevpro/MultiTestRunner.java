package com.mohe.fastdevpro;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by xiePing on 2019/2/14 0014.
 * Description:
 */
public class MultiTestRunner extends AndroidJUnitRunner {

//    public MultiTestRunner() {
//    }

    @Override
    public void onCreate(Bundle arguments) {
        MultiDex.install(getTargetContext());
        super.onCreate(arguments);
    }
}
