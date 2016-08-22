package com.inandwin.halfmodalpresenterdemo;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inandwin.halfmodalpresenter.HalfModalView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HalfModalView hfm = (HalfModalView) findViewById(R.id.halfModal);
        hfm.setBackgroundButtonTrigger(ContextCompat.getDrawable(this, R.color.other));
        hfm.setBackgroundButtonExpanded(ContextCompat.getDrawable(this, R.color.other));
    }
}
