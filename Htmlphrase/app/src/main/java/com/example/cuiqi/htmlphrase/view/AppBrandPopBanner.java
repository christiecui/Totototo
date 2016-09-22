package com.example.cuiqi.htmlphrase.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cuiqi.htmlphrase.R;

/**
 * Created by cuiqi on 16/7/25.
 */
public class AppBrandPopBanner extends RelativeLayout {

    private Button button;

    public AppBrandPopBanner(Context context) {
        super(context);
        initView();
        initAnim();
    }
    public AppBrandPopBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAnim();
    }
    public AppBrandPopBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        initAnim();
    }

    private Animation mShowAnim;
    private Animation mDismissAnim;
    private void initAnim() {
        mShowAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translate_up);
        mDismissAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translate_down);
    }

    private void initView(){
        inflate(getContext(), R.layout.app_brand_pop_banner, this);
        button = (Button)findViewById(R.id.add);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show() {
        startAnimation(mShowAnim);
        this.setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        startAnimation(mDismissAnim);
        this.setVisibility(View.GONE);
    }
}
