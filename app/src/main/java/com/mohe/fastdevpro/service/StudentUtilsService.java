package com.mohe.fastdevpro.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.mohe.fastdevpro.aidl.IStudentAidlInterface;
import com.mohe.fastdevpro.aidl.StudentBean;

import java.util.ArrayList;
import java.util.List;

public class StudentUtilsService extends Service {

    private List<StudentBean> studentBeans=new ArrayList<>();

    public StudentUtilsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        studentBeans.add(new StudentBean(18,"哈哈1"));
        studentBeans.add(new StudentBean(19,"哈哈2"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new IStudentAidlInterface.Stub() {
            @Override
            public void addStudent(com.mohe.fastdevpro.aidl.StudentBean student) throws RemoteException {
                studentBeans.add(student);
            }

            @Override
            public List<com.mohe.fastdevpro.aidl.StudentBean> getStudents() throws RemoteException {
                return studentBeans;
            }
        };
    }
}
