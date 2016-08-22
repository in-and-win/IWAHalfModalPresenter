package com.inandwin.halfmodalpresenter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by raphaelbischof on 22/08/16.
 */
public class HalfModalTriggerView extends LinearLayout {
    public HalfModalTriggerView(Context context) {
        super(context);
    }

    public HalfModalTriggerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

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
