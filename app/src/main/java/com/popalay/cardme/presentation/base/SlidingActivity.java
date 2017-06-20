package com.popalay.cardme.presentation.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.popalay.cardme.R;

public abstract class SlidingActivity extends BaseActivity {

    private static final int GESTURE_THRESHOLD = 10;
    private static final long ANIMATION_DURATION = 200L;

    private View root;
    private float startX = 0f;
    private float startY = 0f;
    private boolean isSliding = false;
    private Point screenSize;
    private ColorDrawable windowScrim;
    private int statusBarColor;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        statusBarColor = ContextCompat.getColor(this, R.color.primary_dark);
        windowScrim = new ColorDrawable(Color.argb(0xE0, 0, 0, 0));
        windowScrim.setAlpha(0);
        getWindow().setBackgroundDrawable(windowScrim);
    }

    @Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        root = getRootView();
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((isSlidingDown(startX, startY, ev) && canSlideDown()) || isSliding) {
                    if (!isSliding) {
                        isSliding = true;
                        getWindow().setStatusBarColor(Color.TRANSPARENT);
                        onSlidingStarted();
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.dispatchTouchEvent(ev);
                    }
                    root.setY(Math.max((ev.getY() - startY), 0));
                    updateScrim();
                    handled = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isSliding) {
                    isSliding = false;
                    onSlidingFinished();
                    handled = true;
                    if (shouldClose(ev.getY() - startY)) {
                        closeDownAndDismiss();
                    } else {
                        getUp();
                    }
                }
                startX = 0f;
                startY = 0f;
                break;
        }
        return handled || super.dispatchTouchEvent(ev);
    }

    @Override public void onBackPressed() {
        closeDownAndDismiss();
    }

    protected abstract View getRootView();

    protected void onSlidingFinished() {
        //override to add logic
    }

    protected void onSlidingStarted() {
        //override to add logic
    }

    protected boolean canSlideDown() {
        return true;
    }

    private boolean shouldClose(float delta) {
        return delta > screenSize.y / 3;
    }

    private boolean isSlidingDown(float startX, float startY, MotionEvent ev) {
        final float deltaX = Math.abs(startX - ev.getX());
        if (deltaX > GESTURE_THRESHOLD) return false;
        final float deltaY = ev.getY() - startY;
        return deltaY > GESTURE_THRESHOLD;
    }

    private void closeDownAndDismiss() {
        final float start = root.getY();
        final float finish = (float) screenSize.y;
        final ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(root, "y", start, finish);
        positionAnimator.setDuration(ANIMATION_DURATION);
        positionAnimator.setInterpolator(new DecelerateInterpolator());
        positionAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {

            }

            @Override public void onAnimationEnd(Animator animator) {
                root.setY(screenSize.y);
                updateScrim();
                finish();
            }

            @Override public void onAnimationCancel(Animator animator) {

            }

            @Override public void onAnimationRepeat(Animator animator) {

            }
        });
        positionAnimator.addUpdateListener(valueAnimator -> updateScrim());
        positionAnimator.start();
    }

    private void getUp() {
        final float start = root.getY();
        final float finish = 0f;
        final ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(root, "y", start, finish);
        positionAnimator.setDuration(ANIMATION_DURATION);
        positionAnimator.setInterpolator(new DecelerateInterpolator());
        positionAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {

            }

            @Override public void onAnimationEnd(Animator animator) {
                getWindow().setStatusBarColor(statusBarColor);
            }

            @Override public void onAnimationCancel(Animator animator) {

            }

            @Override public void onAnimationRepeat(Animator animator) {

            }
        });
        positionAnimator.addUpdateListener(valueAnimator -> updateScrim());
        positionAnimator.start();
    }

    private void updateScrim() {
        final float progress = root.getY() / screenSize.y;
        if (progress == 0) getWindow().setStatusBarColor(statusBarColor);
        final int alpha = (int) (progress * 255f);
        windowScrim.setAlpha(255 - alpha);
        getWindow().setBackgroundDrawable(windowScrim);
    }
}