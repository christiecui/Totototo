package chris.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import chris.assistant.FullscreenableChromeClient;
import chris.utils.CommonUtil;
import chris.utils.DeviceUtils;
import chris.utils.FileUtil;
import tototo.christiecui.tototo.R;

//todo 删除无用代码
public class WebViewUI extends TTFragmentActivity  {

    private static final String TAG = "WebViewUI";
    private static final String UA_PREFIX = "tototo";
    private Context context;
    private WebView webView;
    private ProgressBar loadingView;
    private RelativeLayout browserContentView;
    private String url = "https://www.baidu.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_ui);
        Log.i(TAG, "on create");
        initData(getIntent());
        initView();
        initSetting();
        doRefresh();
    }


    private void doRefresh() {
        loadingView.setProgress(0);
        if(!TextUtils.isEmpty(url)) {
            Log.i(TAG,"url is :"+ url);
            webView.loadUrl(url);
        }
    }

    private void initData(Intent intent){
        Log.i(TAG, "initData");
    }

    private void initView() {
        Log.i(TAG, "initView");
        browserContentView = (RelativeLayout)findViewById(R.id.browser_content_view);
        webView = (WebView) findViewById(R.id.my_webview);
        loadingView = (ProgressBar) findViewById(R.id.loading_view);

    }

    private void initSetting() {
        Log.i(TAG, "initSetting");
        final int sdkVersion = CommonUtil.getAndroidSDKVersion();
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUserAgentString(settings.getUserAgentString() + "/" + UA_PREFIX);
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

            //settings.setPluginsEnabled(true);
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

        try {
            Class<?> clazz = webView.getClass();
            Method method = clazz.getMethod("removeJavascriptInterface", String.class);
            if (method != null)
                method.invoke(webView, "searchBoxJavaBridge_");
        } catch (Exception e1) {
            e1.printStackTrace();
        }


        settings.setAppCachePath(FileUtil.getWebViewCacheDir());
        settings.setDatabasePath(FileUtil.getWebViewCacheDir());
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);

        settings.setUseWideViewPort(true);
        if(sdkVersion >= 7){  // Android2.1+才支持。设置全屏
            try {
                Class<?> clazz = settings.getClass();
                Method method = clazz.getMethod("setLoadWithOverviewMode", boolean.class);
                method.invoke(settings, true);
            } catch (Exception e) {
            }
        }
        if (DeviceUtils.IS_SURPORT_MUTITOUCH_GESTURER) { // 如果手机支持多点触摸，那么就屏蔽放大缩小栏
            if (sdkVersion < 11) {
                try {
                    Field field = WebView.class.getDeclaredField("mZoomButtonsController");
                    field.setAccessible(true);
                    ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(webView);
                    mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
                    field.set(webView, mZoomButtonsController);
                } catch (Exception e) {
                }
            } else {
                try {
                    Class<?> clazz = webView.getSettings().getClass();
                    Method method = clazz.getMethod("setDisplayZoomControls", boolean.class);
                    method.invoke(webView.getSettings(), false);
                } catch (Exception e) {
                }
            }
        }

        if(sdkVersion < 14){
            webView.setWebChromeClient(mWebChromeClient);
        }else{
            webView.setWebChromeClient(new FullscreenableChromeClient(new WebChromeClientListener(){

                @Override
                public Activity getActivity() {
                    return WebViewUI.this;
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    loadingView.setProgress(newProgress);
                }

                @Override
                public void onReceivedTitle(WebView view, String title){
//                    titleView.setTitle(title);
                    setTitle(title);
                }



            }));
        }

        webView.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        // 设置为layer_type_software模式，可以解决部分机型重复刷新导致crash的问题.
        // http://www.4byte.cn/question/694411/android-fatal-signal-11-sigsegv-in-webviewcorethre.html
        if (Build.VERSION.SDK_INT >= 11)
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }


    public interface WebChromeClientListener{

        Activity getActivity();

        public void onProgressChanged(WebView view, int newProgress);

        public void onReceivedTitle(WebView view, String title);

    }


    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView view, int newProgress) {
            loadingView.setProgress(newProgress);
        }

        public void onReceivedTitle(WebView view, String title){
//		    titleView.setTitle(title);
            setTitle(title);
        }

    };

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            loadingView.setVisibility(View.GONE);
            setSupportZoom(true);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadingView.setVisibility(View.VISIBLE);
            setSupportZoom(false);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(url)) {
                return false;
            }

            if (url.startsWith("http") || url.startsWith("https") ) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            return super.shouldOverrideUrlLoading(view,url);
        }

    }

    private void setSupportZoom(boolean isEanable) {
        if (webView != null) {
            webView.getSettings().setSupportZoom(isEanable);
        }
    }

}
