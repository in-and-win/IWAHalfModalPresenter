package com.inandwin.halfmodalpresenterdemo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.inandwin.halfmodalpresenter.HalfModalView;

public class MainActivity extends AppCompatActivity {

    private HalfModalView hfm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hfm = (HalfModalView) findViewById(R.id.halfModal);
        hfm.setBackgroundButtonTrigger(ContextCompat.getDrawable(this, R.color.other));
        hfm.setBackgroundButtonExpanded(ContextCompat.getDrawable(this, R.color.other));
    }
}
