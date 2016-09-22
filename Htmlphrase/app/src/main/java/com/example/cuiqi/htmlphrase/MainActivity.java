package com.example.cuiqi.htmlphrase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.cuiqi.htmlphrase.download.FileDownloader;
import com.example.cuiqi.htmlphrase.java_websocket.client.WebSocketClient;
import com.example.cuiqi.htmlphrase.java_websocket.handshake.ServerHandshake;
import com.example.cuiqi.htmlphrase.upload.Upload;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "WebViewUI";
    private static final String CUI_QI = "MYTIME";
    private static final String UA_PREFIX = "tototo";
    private static final String FETCH_QUEUE = "javascript:WeixinJSBridge._fetchQueue()";
    private Context context;
    private WebView webView;
    private ProgressBar loadingView;
    private Button button;
    private RelativeLayout browserContentView;
    RelativeLayout ll ;
//        private String url = "file:///android_asset/tttt.html";
    private String url = "www.adad.com";
//    private String url = "http://www.baidu.com";
    private PageframeContentReaderTask myTask;
    private WebSocketClient mWebSocketClient;

    Thread writeThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "on create");
        initData(getIntent());
        initView();
        initSetting();
        Log.i(CUI_QI,"all start");
        File file = new File(HtmlParseUtil.getSdCardPath(),"page-frame-1.htm");
        Log.i(CUI_QI,"new file 1");
        if(!file.exists()) {
            Log.i(CUI_QI,"file not exist");
            preparePageFrame();
        }

//        connectWebSocket();


        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendMessage();

//                FileDownloader mDownloader  = FileDownloader.getInstance(getApplication());
//                mDownloader.setExtend(".apk");
//                mDownloader.setFilePath(Environment.getExternalStorageDirectory() + "/apk/");
//                mDownloader.addFile("http://dlied5.myapp.com/myapp/363/hlddz/10000145_fy_com.qqgame.hlddz_166.apk");
//                mDownloader.setDownloadObserver(new com.example.cuiqi.htmlphrase.download.DownloadListener() {
//                    @Override
//                    public void onUpdate(String apkUrl, String file, int completeSize, int apkFileSize) {
//                        //do your thing
//                        Log.i("downlaodaa",completeSize+"     "+ apkFileSize);
//                    }
//                    @Override
//                    public void onComplete(String file) {
//                        //do your thing
//                        Log.i("downlaodaa",file);
//                    }
//
//                    @Override
//                    public void onError(String apkUrl, int type, int state) {
//                        //do your thing
//                        Log.i("downlaodaa","error");
//                    }
//                });
                Upload up = new Upload();
                up.upload();
            }
        });

    }


    @Override
    public void run() {

    }




    AppBrandNetWorkClient.AppBrandNetWorkCallback callback = new AppBrandNetWorkClient.AppBrandNetWorkCallback() {

        @Override
        public void onResult(int errMsg, String data) {

        }

        @Override
        public void onException(HttpURLConnection httpURLConnection, Exception e) {
            Log.i(TAG,e.toString());
        }
    };



    private String getNavi(String content) {
        String result= "";
        String name = "<navigation-bar-title=\\s\"(.*?)\"";
        Pattern p = Pattern.compile(name);
        Matcher m = p.matcher(content);

        while (m.find()) {
            result = m.group(1);
        }

        return result;
    }






    public String NAVIGATION_STYLE = "status-bar-style";
    public String NAVIGATION_BACKGROUD_COLOR = "navigation-bar-background-color";
    public String NAVIGATION_BAR_COLOR = "navigation-bar-color";
    private HashMap<String,String> getNavigationbar(String content){
        HashMap<String,String> navigtionMap = new HashMap<>();
        String style = getValue(content,NAVIGATION_STYLE);
        String backgroudColor = getValue(content,NAVIGATION_BACKGROUD_COLOR);
        String barColor = getValue(content,NAVIGATION_BAR_COLOR);
        navigtionMap.put(NAVIGATION_STYLE,style);
        navigtionMap.put(NAVIGATION_BACKGROUD_COLOR,backgroudColor);
        navigtionMap.put(NAVIGATION_BAR_COLOR,barColor);
        return navigtionMap;
    }

    private String getValue(String content,String key) {
        String result= "";
        String name = "<meta.* name=\\s*\""+key+"\".*content\\s*=\"(.*?)\".*/>";
        Pattern p = Pattern.compile(name);
        Matcher m = p.matcher(content);

        while (m.find()) {
            Log.i("cuiqiaa","asdasd:"+ m.group(1));
            Log.i("cuiqiaa","asdasd:"+ m.group(0));
           result = m.group(1);
        }

        return result;
    }


    private String getValue2(String content,String key) {
        String result= "";
//        String name ="<meta name=\"navigation-bar-background-color\" .*content=\"(.*?)\"/>";
//        String name = "<meta name=\""+key+"\".*content=\"(.*?)\"/>";
        String name = "<meta[^/]+?/>";
        Pattern p = Pattern.compile(name);
        Matcher m = p.matcher(content);

        while (m.find()) {
//            result = m.group(1);
            Log.i("cuiqiaa","asdasd:"+ m.group(0));
//            Log.i("cuiqiaa","lalaaaa:"+ m.group(1));
        }

        return result;
    }


    private String getStatus2(String content) {
        String result= "";

        String name = "<meta name=\"(.*)\".*content=\"(.*)\"/>";
//        String name = "<meta[^/]+?/>";
        Pattern p = Pattern.compile(name);
        Matcher m = p.matcher(content);

        while (m.find()) {
            Log.i("cuiqiaa","lalaaaa:"+ m.group(1));
            Log.i("cuiqiaa","lalaaaa:"+ m.group(2));
            Log.i("cuiqiaa","lalaaaa:"+ m.group(0));
        }


        return result;
    }

    private void doRefresh() {
        if(!TextUtils.isEmpty(url)) {
            Log.i(CUI_QI,"load url");
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

        String url1 = "http://180.163.9.104/release/wx0c839b115f564d77/index.html ";

        loadingView = (ProgressBar) findViewById(R.id.loading_view);




    }

    private void initSetting() {
        Log.i(TAG, "initSetting");
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


//        settings.setAppCachePath(FileUtil.getWebViewCacheDir());
//        settings.setDatabasePath(FileUtil.getWebViewCacheDir());
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);

        settings.setUseWideViewPort(true);



        webView.setWebChromeClient(mWebChromeClient);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebViewChromeClient());
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
            Log.i(CUI_QI,"load: finished "+ url);
//            injectCode();
            loadingView.setVisibility(View.GONE);
            setSupportZoom(true);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(CUI_QI,"load: start "+ url);
            loadingView.setVisibility(View.VISIBLE);
            setSupportZoom(false);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.i("cuiqi","~~~~~~~~~~~~~~~~~~~~~~~~~~1111111111+url= "+ url);
            if (TextUtils.isEmpty(url)) {
                return false;
            }

            if (url.startsWith("http") || url.startsWith("https") ) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            return super.shouldOverrideUrlLoading(view,url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request)  {
            Log.i("cuiqi","~~~~~~~~~~~gaoji~~intercept+url= "+ request.getUrl().toString());

//            webView.evaluateJavascript(FETCH_QUEUE, null);
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          String url) {
            Log.i("cuiqi","~~~~~~~~~~~~~intercept+url= "+ url);
//            webView.evaluateJavascript(FETCH_QUEUE, null);
            return null;
        }

    }

    private class MyWebViewChromeClient extends WebChromeClient {
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.i("cuiqi","~~~~~~~~~~~~~onJsPrompt+defaultValue= "+ defaultValue );
            Log.i("cuiqi","~~~~~~~~~~~~~onJsPrompt+message= "+ message );
            Log.i("cuiqi","~~~~~~~~~~~~~onJsPrompt+url= "+ url );
            Log.i("cuiqi","~~~~~~~~~~~~~onJsPrompt+result= "+ result );
            if(message.startsWith("cuiqi")){
                result.confirm();
                return true;
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.i("cuiqi","~~~~~~~~~~~~~consoleMessage= "+ consoleMessage.message() );
            return super.onConsoleMessage(consoleMessage);
        }
    }
    private void setSupportZoom(boolean isEanable) {
        if (webView != null) {
            webView.getSettings().setSupportZoom(isEanable);
        }
    }



    private void testReplace() {
        String sdPath = HtmlParseUtil.getSdCardPath();
    }

    private void preparePageFrame() {
        String pageContent = "";
        String jsContent = "";
        String weuiContent = "";

        String sdPath = HtmlParseUtil.getSdCardPath();

        File pageFile = new File(sdPath,"page-frame.htm");

        pageContent = HtmlParseUtil.readFile(pageFile);


        File jsFile = new File(sdPath,"jssdk.js");

        jsContent = HtmlParseUtil.readFile(jsFile);


        String jsResult = HtmlParseUtil.replaceWxScript(pageContent,jsContent);
        File weuiFile = new File(sdPath,"weui.css");

        weuiContent = HtmlParseUtil.readFile(weuiFile);

        String weuiResult = HtmlParseUtil.replaceWxCss(jsResult,weuiContent);
        Log.i("cuiqi","Result~~~~"+ weuiResult);

        File file2 = new File(sdPath,"page-frame-1.htm");
        HtmlParseUtil.writeFile(file2,weuiResult);

    }

    private PageframeContentReaderTask.OnTaskCompleted onTaskCompleted = new PageframeContentReaderTask.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String result) {
            String script = HtmlParseUtil.extractScript(result);
            String body =HtmlParseUtil.extractPage(result);
            String style = "";

            String injectScript =  String.format("javascript: "+
                    "{var s = document.createElement('style'); s.innerHTML = '%s'; document.body.appendChild(s);}" +
                    "{var s = document.createElement('page'); s.innerHTML = '%s'; document.body.appendChild(s); }" +
                    "%s",style,body,script);
            Log.i(CUI_QI,"injcetScript: start ");
            webView.evaluateJavascript(injectScript, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String var1) {
                    Log.i(CUI_QI,"injcetScript: over ");
                    JsonReader reader = new JsonReader(new StringReader(var1));
                    // Must set lenient to parse single values
                    reader.setLenient(true);

                    try {
                        if(reader.peek() != JsonToken.NULL) {
                            if(reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if(msg != null) {
                                    Log.i("cuiqi","injcetScript"+ msg);
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.e("TAG", "MainActivity: IOException", e);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            });
            Log.i("cuiqi","injcetScript all over");
        }
    };

    private void injectCode() {
        url = "http://203.195.235.76:8082/testjsapi/frame/index.html";
        myTask = new PageframeContentReaderTask(onTaskCompleted);
        myTask.execute(url);
    }

    @Override
    protected void onDestroy() {
        ll.removeAllViews();
        super.onDestroy();
    }


    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://203.195.235.76:3001");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Websocket","socket onmessage "+message);
                    }
                });
            }

            @Override
            public void onMessage( ByteBuffer bytes ) {
                final ByteBuffer message = bytes;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Websocket","socket onmessage "+message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }


    public void sendMessage() {
        EditText editText = (EditText)findViewById(R.id.message);
        mWebSocketClient.send(editText.getText().toString());
        byte[] byBuffer ;
        byBuffer= editText.getText().toString().getBytes();
        mWebSocketClient.send(byBuffer);
        editText.setText("");
    }
}
