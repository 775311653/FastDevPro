// IStudentAidlInterface.aidl
package com.mohe.fastdevpro.aidl;
import com.mohe.fastdevpro.aidl.StudentBean;
// Declare any non-default types here with import statements

interface IStudentAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    void addStudent(in StudentBean student);
    List<StudentBean> getStudents();

}
