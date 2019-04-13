package com.mohe.fastdevpro.pattern.intercepterPattern;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiePing on 2019/4/13 0013.
 * Description:
 */
public class StudentUtils {

    private List<IStudentIntercepter> studentIntercepters=new ArrayList<>();

    private static StudentUtils mStudentUtils;

    private static class StudentUtilsInstance{
        private static StudentUtils studentUtils=new StudentUtils();
    }

    public static StudentUtils getInstance(){
        if (mStudentUtils==null){
            mStudentUtils=StudentUtilsInstance.studentUtils;
        }
        return mStudentUtils;
    }

    public void addStudentIntercepter(IStudentIntercepter studentIntercepter){
        studentIntercepters.add(studentIntercepter);
    }


    public void showStudent(Student student){
        for (int i=0;i<studentIntercepters.size();i++){
            studentIntercepters.get(i).doSomeThing(student);
        }
        LogUtils.i(student.getId(),student.getName());
    }
}
