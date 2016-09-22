package com.example.cuiqi.htmlphrase.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.example.cuiqi.htmlphrase.R;

import java.util.ArrayList;

/**
 * Created by cuiqi on 16/8/3.
 */
public class AppBrandViewHider implements View.OnTouchListener {

    private final int height;
    private final int wxHeight;
    private float mFirstY;
    private float mCurrentY;
    private int mTouchSlop;         //滑动限定停止值
    private View parent;

    View titlebar;
    View contentView;

    public AppBrandViewHider(View titlebar, View contentView, int height, int wxHeight) {
        this.titlebar = titlebar;
        this.contentView = contentView;
        this.height = height;
        this.wxHeight = wxHeight;
        mTouchSlop = ViewConfiguration.get(contentView.getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentY = event.getY();
                if (mCurrentY - mFirstY > mTouchSlop) {
                    showOrHide(true); //下滑 显示titlebar
                } else if (mFirstY - mCurrentY > mTouchSlop) {
                    showOrHide(false); //上滑 隐藏titlebar
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return false;
    }

    private Animator mAnimationTitle;
    private Animator mAnimationContent;

    private void showOrHide(boolean tag) {
        if (mAnimationTitle != null && mAnimationTitle.isRunning()) {
            mAnimationTitle.cancel();
        }
        if (mAnimationContent != null && mAnimationContent.isRunning()) {
            mAnimationContent.cancel();
        }
        if (tag) {
            mAnimationTitle = ObjectAnimator.ofFloat(titlebar, "translationY", titlebar.getTranslationY(), 0);
            mAnimationContent = ObjectAnimator.ofFloat(contentView, "translationY", contentView.getTranslationY(), contentView.getContext().getResources().getDimension(R.dimen.title));
        } else {
            mAnimationTitle = ObjectAnimator.ofFloat(titlebar,"translationY",titlebar.getTranslationY(),-titlebar.getHeight());
            mAnimationContent = ObjectAnimator.ofFloat(contentView,"translationY",contentView.getTranslationY(),0);
        }
        mAnimationTitle.start();
        mAnimationContent.start();
    }
}
