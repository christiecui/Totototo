package chris.assistant;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import chris.ui.WebViewUI;
import chris.utils.CommonUtil;

/**
 * 支持的最低版本Api 8+，适用于Android sdk>=14，解决flash在4.0+的系统上无法全屏的问题
 */
//todo 删除无用代码
public class FullscreenableChromeClient extends WebChromeClient{
    protected WebViewUI.WebChromeClientListener mListener = null;
    protected Activity mActivity = null;

    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;

    private FrameLayout mContentView;
    private FrameLayout mFullscreenContainer;

    private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT);
	private static final String TAG = "FullscreenableChromeClient";

    public FullscreenableChromeClient(WebViewUI.WebChromeClientListener listener) {
        mListener = listener;
        this.mActivity = listener.getActivity();
    }
    
    public void onProgressChanged(WebView view, int newProgress) {
        mListener.onProgressChanged(view, newProgress);
    }
    
    public void onReceivedTitle(WebView view, String title){
        mListener.onReceivedTitle(view, title);
        
    	//以下只了为了解决安全审计问题，初阶已经在BrowserActivity中移除过了
		if (view != null) {
			try {
				Class<?> clazz = view.getClass();
				Method method = clazz.getMethod("removeJavascriptInterface", String.class);
				if (method != null)
					method.invoke(view, "searchBoxJavaBridge_");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
    }

    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        if (Build.VERSION.SDK_INT >= 14) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
 
            mOriginalOrientation = mActivity.getRequestedOrientation();
            FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
            mFullscreenContainer = new FullscreenHolder(mActivity);
            mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
            decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
            mCustomView = view;
            setFullscreen(true);
            mCustomViewCallback = callback;
            mActivity.setRequestedOrientation(requestedOrientation);
        }
 
    }

    public void onHideCustomView() {
        if (mCustomView == null) {
            return;
        }

        setFullscreen(false);
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(mFullscreenContainer);
        mFullscreenContainer = null;
        mCustomView = null;
        mCustomViewCallback.onCustomViewHidden();
        mActivity.setRequestedOrientation(mOriginalOrientation);
    }

    private void setFullscreen(boolean enabled) {
        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (enabled) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
            if (mCustomView != null) {
                setSystemUiVisibility(mCustomView);
            } else {
                setSystemUiVisibility(mContentView);
                
            }
        }
        win.setAttributes(winParams);
    }

    private static class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }
    
    private void setSystemUiVisibility(View view){
        if(CommonUtil.getAndroidSDKVersion() >= 14){
            try {
                Class<?> clazz = view.getClass();
                Method method = clazz.getMethod("setSystemUiVisibility", int.class);
                method.invoke(view, 0x00000000);
            } catch (Exception e) {
            }
        }
    }
    
    
    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
    	super.onConsoleMessage(message, lineNumber, sourceID);
    }
}
