package com.inandwin.halfmodalpresenter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * View that shows a button at first step and when the button is clicked presents a view on bottom second half of the screen
 * Created by raphaelbischof on 22/08/16.
 */

public class HalfModalView extends ViewGroup implements View.OnClickListener {

    private boolean isExpanded = false;

    @Nullable
    private Drawable mBackgroundButtonTrigger;
    @Nullable
    private Drawable mBackgroundButtonExpanded;
    private float mPositionFromTop;

    private boolean isExpanding = false;
    private boolean isStretching = false;
    private int mFps = 120;
    private int mAnimationLength = 300;
    private int mAnimationFirstPartLength = 60;
    private long startTime;
    private View dismissHitbox;

    private Path mClipPath = new Path();
    private RectF mRectMask = new RectF();


    public HalfModalView(Context context) {
        super(context);
        initPaint(context);
    }

    public HalfModalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint(context);
    }

    public HalfModalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HalfModalView, 0, 0);

        try {
            mFps = a.getInteger(R.styleable.HalfModalView_fps, 120);
            mAnimationLength = a.getInteger(R.styleable.HalfModalView_animationLength, 300);
            mAnimationFirstPartLength = a.getInteger(R.styleable.HalfModalView_animationFirstPartLength, 60);
            mBackgroundButtonExpanded = a.getDrawable(R.styleable.HalfModalView_backgroundButtonExpanded);
            mBackgroundButtonTrigger = a.getDrawable(R.styleable.HalfModalView_backgroundButtonTrigger);
            mPositionFromTop = a.getFloat(R.styleable.HalfModalView_positionFromTop, 0.8f);
        } finally {
            a.recycle();
        }

    }

    private void initPaint(Context context) {
        setWillNotDraw(false);
        dismissHitbox = new View(context);
        this.addView(dismissHitbox);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getTriggerView().setOnClickListener(this);
        if (isExpanded){
            getTriggerView().setVisibility(GONE);
        }else{
            getExpandedView().setVisibility(GONE);
            dismissHitbox.setVisibility(GONE);
        }
        if (getChildCount()>1){
            showButton(l,t,r,b);
            showPanel(l,t,r,b);
            showDismissHitbox(l, t, r, b);
        }else{
            throw new IllegalArgumentException("HalfModalView must have two child views : HalfModalTriggerView and HalfModalExpandedView");
        }
    }

    private void showDismissHitbox(int l, int t, int r, int b) {
        int positionFromBottom = t + (b - t) / 2;
        dismissHitbox.layout(l, t, r, positionFromBottom);
    }

    private void showPanel(int l, int t, int r, int b) {
        HalfModalExpandedView triggerView = getExpandedView();
            int positionFromTop = t + (b-t)/2;
            triggerView.layout(l,positionFromTop,r,b);
    }

    private void showButton(int l, int t, int r, int b) {
        HalfModalTriggerView triggerView = getTriggerView();
            int buttonHeight = triggerView.getMeasuredHeight();
            int buttonWidth = triggerView.getMeasuredWidth();
            int positionFromLeft = (r-l)/2-buttonWidth/2;
        int positionFromTop = (int) (((float) (b - t)) * mPositionFromTop);
            triggerView.layout(positionFromLeft,positionFromTop,positionFromLeft+buttonWidth,positionFromTop+buttonHeight);
    }

    public HalfModalTriggerView getTriggerView() {
        if (getChildAt(0) instanceof HalfModalTriggerView){
            return (HalfModalTriggerView) getChildAt(0);
        }else if (getChildAt(1) instanceof HalfModalTriggerView){
            return (HalfModalTriggerView) getChildAt(1);
        } else if (getChildAt(2) instanceof HalfModalTriggerView) {
            return (HalfModalTriggerView) getChildAt(2);
        }else {
            throw new IllegalArgumentException("HalfModalView does not contain an HalfModalTriggerView");
        }
    }

    public HalfModalExpandedView getExpandedView() {
        if (getChildAt(0) instanceof HalfModalExpandedView){
            return (HalfModalExpandedView) getChildAt(0);
        }else if (getChildAt(1) instanceof HalfModalExpandedView){
            return (HalfModalExpandedView) getChildAt(1);
        } else if (getChildAt(2) instanceof HalfModalExpandedView) {
            return (HalfModalExpandedView) getChildAt(2);
        }else {
            throw new IllegalArgumentException("HalfModalView does not contain an HalfModalExpandedView");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isExpanded){
            getExpandedView().measure(getMeasuredWidth(), getMeasuredHeight());
        }else{
            getTriggerView().measure(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isExpanding){
            boolean endExpanding = false;
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (mBackgroundButtonTrigger != null && mAnimationLength > elapsedTime && elapsedTime / (float) mAnimationFirstPartLength <= 1) {
                mClipPath.reset();
                drawPositionForFirst(elapsedTime / (float) mAnimationFirstPartLength, canvas);
                mBackgroundButtonTrigger.setBounds(getExpandedView().getLeft(), getExpandedView().getTop(), getExpandedView().getRight(), getExpandedView().getBottom());
                mBackgroundButtonTrigger.draw(canvas);
                postInvalidateDelayed(1000/mFps);
            } else if (mBackgroundButtonExpanded != null && mAnimationLength > elapsedTime) {
                mClipPath.reset();
                drawPositionForSecond((elapsedTime - (float) mAnimationFirstPartLength) / ((float) mAnimationLength - (float) mAnimationFirstPartLength), canvas);
                mBackgroundButtonExpanded.setBounds(getExpandedView().getLeft(), getExpandedView().getTop(), getExpandedView().getRight(), getExpandedView().getBottom());
                mBackgroundButtonExpanded.draw(canvas);
                postInvalidateDelayed(1000/mFps);
            }else{
                endExpanding = true;
            }
            if (endExpanding){
                dismissHitbox.setOnClickListener(this);
                isExpanding = false;
            }
        }else if (isStretching){
            boolean endStretching = false;
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (mBackgroundButtonExpanded != null && mAnimationLength > elapsedTime && elapsedTime / (float) (mAnimationLength - mAnimationFirstPartLength) <= 1) {
                mClipPath.reset();
                float ratioElapsed = (((float) (mAnimationLength - mAnimationFirstPartLength) - elapsedTime)) / (float) (mAnimationLength - mAnimationFirstPartLength);
                drawPositionForSecond(ratioElapsed, canvas);
                mBackgroundButtonExpanded.setBounds(getExpandedView().getLeft(), getExpandedView().getTop(), getExpandedView().getRight(), getExpandedView().getBottom());
                mBackgroundButtonExpanded.draw(canvas);
                postInvalidateDelayed(1000/mFps);
            } else if (mBackgroundButtonTrigger != null && mAnimationLength > elapsedTime) {
                mClipPath.reset();
                float ratioElapsed = (mAnimationFirstPartLength + ((float) (mAnimationLength - mAnimationFirstPartLength) - elapsedTime)) / (float) mAnimationFirstPartLength;
                drawPositionForFirst(ratioElapsed, canvas);
                mBackgroundButtonTrigger.setBounds(getExpandedView().getLeft(), getExpandedView().getTop(), getExpandedView().getRight(), getExpandedView().getBottom());
                mBackgroundButtonTrigger.draw(canvas);
                postInvalidateDelayed(1000/mFps);
            }else{
                endStretching = true;
            }
            if (endStretching){
                getTriggerView().setOnClickListener(this);
                isStretching = false;
                getTriggerView().setVisibility(VISIBLE);
            }
        }
    }

    private void drawPositionForSecond(float ratioElapsed, Canvas canvas) {
        float verticalDelta = 35;
        float horizontalDelta = 15;
        float leftStarting = getTriggerView().getLeft() + horizontalDelta;
        float rightStarting = getTriggerView().getRight() - horizontalDelta;
        float topStarting = getTriggerView().getTop() - verticalDelta;
        float bottomStarting = getTriggerView().getBottom() - verticalDelta;

        float top = topStarting * (1-ratioElapsed) + getExpandedView().getTop()*ratioElapsed;
        float bottom = bottomStarting * (1-ratioElapsed) + getExpandedView().getBottom()*ratioElapsed;

        float radius = (bottom-top)/2;

        float left = leftStarting * (1-ratioElapsed) + (getExpandedView().getLeft()-radius)*ratioElapsed;
        float right = rightStarting * (1-ratioElapsed) + (getExpandedView().getRight()+radius)*ratioElapsed;


        mRectMask.set(left, top, right, bottom);

        mClipPath.addRoundRect(mRectMask, radius, radius, Path.Direction.CW);
        canvas.clipPath(mClipPath);
    }

    public void drawPositionForFirst(float ratioElapsed, Canvas canvas) {
        float verticalDelta = 35;
        float horizontalDelta = 15;
        float left = getTriggerView().getLeft() + ratioElapsed*horizontalDelta;
        float right = getTriggerView().getRight() - ratioElapsed*horizontalDelta;
        float top = getTriggerView().getTop() - ratioElapsed*verticalDelta;
        float bottom = getTriggerView().getBottom() - ratioElapsed*verticalDelta;

        float radius = (bottom-top)/2;
        mRectMask.set(left, top, right, bottom);

        mClipPath.addRoundRect(mRectMask, radius, radius, Path.Direction.CW);
        canvas.clipPath(mClipPath);
    }

    @Override
    public void onClick(View v) {
        isExpanded = !isExpanded;
        if (isExpanded){
            isExpanding = true;
            startTime = System.currentTimeMillis();
            getTriggerView().setVisibility(GONE);
            getTriggerView().setOnClickListener(null);
            getExpandedView().setVisibility(VISIBLE);
            dismissHitbox.setVisibility(VISIBLE);
        }else {
            isStretching = true;
            startTime = System.currentTimeMillis();
            getExpandedView().setVisibility(GONE);
            dismissHitbox.setOnClickListener(null);
        }
        invalidate();
    }

    public void setBackgroundButtonTrigger(Drawable backgroundButton) {
        this.mBackgroundButtonTrigger = backgroundButton;
    }

    public void setBackgroundButtonExpanded(Drawable backgroundButtonExpanded) {
        this.mBackgroundButtonExpanded = backgroundButtonExpanded;
    }
}
