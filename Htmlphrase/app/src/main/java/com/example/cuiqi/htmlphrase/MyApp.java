package com.example.cuiqi.htmlphrase;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by cuiqi on 16/6/8.
 */
public class MyApp extends Application {
    private WebView myState;
    public WebView getState(){
        return myState;
    }
    public void setState(WebView s){
        myState = s;
        WebSettings settings = myState.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUserAgentString(settings.getUserAgentString());
        settings.setJavaScriptEnabled(true);
    }

}

