package com.example.cuiqi.htmlphrase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuiqi.htmlphrase.R;

/**
 * Created by cuiqi on 16/7/28.
 */
public class AppBrandTagView extends TextView {

    public AppBrandTagView(Context context) {
        super(context);
        init();
    }
    public AppBrandTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public AppBrandTagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        inflate(getContext(), R.layout.app_brand_tag_view, null);
    }
}
