package com.example.cuiqi.htmlphrase.view;

/**
 * Created by cuiqi on 16/7/25.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cuiqi.htmlphrase.R;

public class Card extends RelativeLayout {
    private TextView header;
    private TextView description;
    private ImageView thumbnail;
    private ImageView icon;
    public Card(Context context) {
        super(context);
        init();
    }
    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public Card(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        inflate(getContext(), R.layout.card, this);
        description = (TextView)findViewById(R.id.description);
        thumbnail = (ImageView)findViewById(R.id.thumbnail);
        icon = (ImageView)findViewById(R.id.icon);
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("qq","kalalala");
            }
        });

        thumbnail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("qq","kalalala");
            }
        });

        description.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("qq","kalalala");
            }
        });
    }
}
