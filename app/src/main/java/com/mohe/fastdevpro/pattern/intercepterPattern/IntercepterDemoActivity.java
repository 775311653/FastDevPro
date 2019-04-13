package com.mohe.fastdevpro.pattern.intercepterPattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.R;

public class IntercepterDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercepter_demo);
        Student student=new Student("墨禾","01");
        StudentUtils.getInstance().addStudentIntercepter(new ReNameIntercepter("墨禾2"));
        StudentUtils.getInstance().addStudentIntercepter(new ChangIdIntercepter("02"));
        StudentUtils.getInstance().showStudent(student);
    }

    private class ReNameIntercepter implements IStudentIntercepter{

        private String name;

        public ReNameIntercepter(String name) {
            this.name=name;
        }

        @Override
        public Student doSomeThing(Student student) {
            student.name=name;
            LogUtils.i("改名拦截器，改名为"+name);
            return student;
        }
    }

    private class ChangIdIntercepter implements IStudentIntercepter{

        private String id;

        public ChangIdIntercepter(String id) {
            this.id = id;
        }

        @Override
        public Student doSomeThing(Student student) {
            student.id= id;
            LogUtils.i("改id拦截器，改id为"+ id);
            return student;
        }
    }
}
