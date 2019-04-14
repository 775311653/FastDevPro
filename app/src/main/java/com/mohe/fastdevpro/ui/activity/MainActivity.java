package com.mohe.fastdevpro.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.aidl.IStudentAidlInterface;
import com.mohe.fastdevpro.service.StudentUtilsService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IStudentAidlInterface studentAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this, StudentUtilsService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                studentAidlInterface= IStudentAidlInterface.Stub.asInterface(service);
                try {
                    List<com.mohe.fastdevpro.aidl.StudentBean> studentBeans=studentAidlInterface.getStudents();
                    LogUtils.i(studentBeans.get(1).getAge(),studentBeans.get(1).getName());
                } catch (RemoteException e) {
                    LogUtils.e(e.getMessage());
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },0);
    }
}
