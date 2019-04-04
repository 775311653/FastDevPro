package com.mohe.fastdevpro.pattern.filterPattern;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterDemoActivity extends AppCompatActivity {

    @BindView(R.id.btn_test)
    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_demo);
        ButterKnife.bind(this);
        filterDemoTest();
    }

    private void filterDemoTest() {
        List<Person> allPeoples = getInitData();
        List<Person> ageBig20People = PersonUtil.ageBig(allPeoples, 20);
        List<Person> sexWomanPeople = PersonUtil.sexWoman(allPeoples);
        List<Person> singlePeople = PersonUtil.singlePerson(allPeoples);
        List<Person> ageBig20WomanSingle = PersonUtil.andSatisfy(ageBig20People, sexWomanPeople, singlePeople);
        for (Person person:ageBig20WomanSingle){
            LogUtils.i(GsonUtils.toJson(person));
        }
    }

    private List<Person> getInitData() {
        List<Person> people = new ArrayList<>();
        people.add(new Person(15, "man", "single"));
        people.add(new Person(16, "man", "single"));
        people.add(new Person(17, "woman", "unSingle"));
        people.add(new Person(18, "woman", "single"));
        people.add(new Person(19, "man", "unSingle"));
        people.add(new Person(20, "woman", "unSingle"));
        people.add(new Person(21, "man", "unSingle"));
        people.add(new Person(22, "woman", "single"));
        people.add(new Person(23, "woman", "unSingle"));
        people.add(new Person(24, "woman", "single"));
        people.add(new Person(25, "man", "single"));
        return people;
    }

    @OnClick(R.id.btn_test)
    public void onViewClicked() {
        filterDemoTest();
    }
}
