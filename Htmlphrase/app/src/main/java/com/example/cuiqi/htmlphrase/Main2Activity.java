package com.example.cuiqi.htmlphrase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cuiqi.htmlphrase.upload.Upload;

public class Main2Activity extends AppCompatActivity {

    private String url = "file:///android_asset/page-frame-1.htm";
    private WebView webView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        webView = new WebView(this);

        webView.loadUrl(url);


        MyApp appState = ((MyApp)getApplicationContext());
        appState.setState(webView);

        TextView textView = (TextView) findViewById(R.id.lalala);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }



}
