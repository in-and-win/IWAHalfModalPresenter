package com.inandwin.halfmodalpresenter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Represents the view that will be shown in the bottom part of the screen
 */
public class HalfModalExpandedView extends LinearLayout {
    public HalfModalExpandedView(Context context) {
        super(context);
        commonInit();
    }

    public HalfModalExpandedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public HalfModalExpandedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    private void commonInit() {
        this.setClickable(true);
    }
}
