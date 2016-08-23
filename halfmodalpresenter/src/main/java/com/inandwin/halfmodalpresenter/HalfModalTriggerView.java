package com.inandwin.halfmodalpresenter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Represents the view used as a button that launches the view expansion
 */
public class HalfModalTriggerView extends LinearLayout {
    public HalfModalTriggerView(Context context) {
        super(context);
    }

    public HalfModalTriggerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public HalfModalTriggerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        for (int i = 0; i < this.getChildCount(); i++) {
            getChildAt(i).setOnClickListener(l);
        }
    }
}
