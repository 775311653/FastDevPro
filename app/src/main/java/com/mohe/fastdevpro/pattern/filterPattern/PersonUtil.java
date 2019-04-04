package com.mohe.fastdevpro.pattern.filterPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiePing on 2019/4/4 0004.
 * Description:
 */
public class PersonUtil {
    //年龄大于ageNum的人数。
    public static List<Person> ageBig(List<Person> personList,int ageNum){
        if (personList==null||personList.size()==0){
            return null;
        }
        List<Person> satisfyPeople=new ArrayList<>();
        for (Person person:personList){
            if (person.age>ageNum){
                satisfyPeople.add(person);
            }
        }
        return satisfyPeople;
    }

    //年龄小于ageNum的人数
    public static List<Person> ageSmall(List<Person> personList,int ageNum){
        if (personList==null||personList.size()==0){
            return null;
        }
        List<Person> satisfyPeople=new ArrayList<>();
        for (Person person:personList){
            if (person.age<ageNum){
                satisfyPeople.add(person);
            }
        }
        return satisfyPeople;
    }

    //年龄小于ageNum的人数
    public static List<Person> ageEquals(List<Person> personList,int ageNum){
        if (personList==null||personList.size()==0){
            return null;
        }
        List<Person> satisfyPeople=new ArrayList<>();
        for (Person person:personList){
            if (person.age==ageNum){
                satisfyPeople.add(person);
            }
        }
        return satisfyPeople;
    }

    //性别是男的人数
    public static List<Person> sexMan(List<Person> personList){
        if (personList==null||personList.size()==0){
            return null;
        }
        List<Person> satisfyPeople=new ArrayList<>();
        for (Person person:personList){
            if (person.sex.equals("man")){
                satisfyPeople.add(person);
            }
        }
        return satisfyPeople;
    }

    //性别是女的人数
    public static List<Person> sexWoman(List<Person> personList){
        if (personList==null||personList.size()==0){
            return null;
        }
        List<Person> satisfyPeople=new ArrayList<>();
        for (Person person:personList){
            if (person.sex.equals("woman")){
                satisfyPeople.add(person);
            }
        }
        return satisfyPeople;
    }

    //单身人数
    public static List<Person> singlePerson(List<Person> personList){
        if (personList==null||personList.size()==0){
            return null;
        }
        List<Person> satisfyPeople=new ArrayList<>();
        for (Person person:personList){
            if (person.status.equals("single")){
                satisfyPeople.add(person);
            }
        }
        return satisfyPeople;
    }

    //非单身人数
    public static List<Person> unSinglePerson(List<Person> personList){
        if (personList==null||personList.size()==0){
            return null;
        }
        List<Person> satisfyPeople=new ArrayList<>();
        for (Person person:personList){
            if (person.status.equals("unSingle")){
                satisfyPeople.add(person);
            }
        }
        return satisfyPeople;
    }


    //满足所有条件的人
    public static List<Person> andSatisfy(List<Person> ... personListList ){
        List<Person> satisfyPeople=new ArrayList<>();
        List<Person> personList=personListList[0];

        if (personList==null||personList.size()==0){
            return null;
        }

        for (Person person:personList){
            for (int j=0;j<personListList.length;j++){
                //循环从第一个数组里面取对象，如果后面的数组有一个没有包含，这个对象，那就剔除这个对象
                if (!personListList[j].contains(person)){
                    break;
                }
                //都走到最后一个也没结束比对第一个数组里面的这个对象，说明大家都有这个对象，则添加
                if (j==personListList.length-1){
                    satisfyPeople.add(person);
                }
            }

        }
        return satisfyPeople;
    }

    //满足该数组中一个条件就行的数组
    public static List<Person> orSatisfy(List<Person> ... personListList ){
        List<Person> personList=personListList[0];

        if (personList==null||personList.size()==0){
            return null;
        }

        List<Person> satisfyPeople = new ArrayList<>(personList);
        for (int j=0;j<personListList.length;j++){
            satisfyPeople.addAll(personListList[j]);
        }
        //移除重复的数据
        for (int i = 0; i < satisfyPeople.size() - 1; i++) {
            for (int j = i + 1; j < satisfyPeople.size(); j++) {
                if (satisfyPeople.get(i).equals(satisfyPeople.get(j))) {
                    satisfyPeople.remove(j);
                }
            }
        }
        return satisfyPeople;
    }
}
