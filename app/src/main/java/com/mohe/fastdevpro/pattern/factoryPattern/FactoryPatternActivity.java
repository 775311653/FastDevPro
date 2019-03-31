package com.mohe.fastdevpro.pattern.factoryPattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.R;

public class FactoryPatternActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_pattern);
        ShapeUtils.getShape(ShapeUtils.CIRCLE).draw();
        ShapeUtils.getShape(ShapeUtils.RECT).draw();
        ShapeUtils.getShape(ShapeUtils.SQUARE).draw();
    }
}
