package com.example.cuiqi.htmlphrase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by cuiqi on 16/6/8.
 */
public class MyWebView extends WebView{

    public MyWebView(Context context) {
        super(context);
    }

    private void initSetting() {
//        final int sdkVersion = CommonUtil.getAndroidSDKVersion();
        WebSettings settings = getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUserAgentString(settings.getUserAgentString() + "/" );
        settings.setJavaScriptEnabled(true);
        {
            Class<?> clazz = settings.getClass();
            try {
                Method method = clazz.getMethod("setPluginsEnabled", boolean.class);
                if( method != null)
                {
                    method.invoke(settings, true);
                }
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  catch(Throwable e)
            {
                e.printStackTrace();
            }

        }


        try {
            Class<?> clazz = settings.getClass();
            Method method = clazz.getMethod("setDomStorageEnabled", boolean.class);
            if( method != null)
                method.invoke(settings, true);
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }



        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);

        settings.setUseWideViewPort(true);


    }


}
