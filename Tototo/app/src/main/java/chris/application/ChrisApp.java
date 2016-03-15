package chris.application;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import chris.event.EventController;
import chris.event.EventDispatcher;
import chris.event.EventDispatcherEnum;
import chris.ui.TTFragmentActivity;

/**
 * Created by christiecui on 15/6/24.
 * modified by christiecui on 2016/3/15
 */
public class ChrisApp extends Application {
    public static final String TAG = "ChrisApp";
    private static final String PRE_VERSION = "prevision_code";

    private static volatile ChrisApp mApp;

    private EventDispatcher mEventDispatcher;
    private EventController mEventController;

    private boolean isAppFront = false;

    private static TTFragmentActivity mCurActivity = null;

    private static Activity mTabActivity = null;

    long start = 0;

    private volatile static boolean bRunDelayTask = false;
    private static boolean isShowFirstPageDownloadBubble=false;//全局存一个标志位，用于标记从启动到现在，有没有显示首页的下载未安装提示。只有这里最合适放这个。


    public ChrisApp()
    {
        super();
        if(mApp==null)
        {
            mApp=this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //初始化
        initApp();
    }

    private void initApp() {
        Log.i(TAG, "ChrisApp  initApp()");

        mEventDispatcher = EventDispatcher.getInstance(null);
        mEventController = EventController.getInstance();
        mEventDispatcher.setListener(mEventController);
    }

    public static ChrisApp self() {
        //获取mApp时一定要保证mApp是不为空的，否则整个生命周期会出现问题。例如数据库访问对象无法构建等
        if(mApp==null)
        {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(mApp==null)//还是空啊，你怎么办呢
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return mApp;
    }

    /**
     * Get the event dispatcher.
     *
     * @return The event dispaccctcher.
     */
    public EventDispatcher getEventDispatcher() {
        return mEventDispatcher;
    }

    /**
     * Get the controller.
     *
     * @return The controller.
     */
    public EventController getEventController() {
        return mEventController;
    }

    public boolean isAppFront() {
        return isAppFront;
    }

    public void setAppFront(boolean front, int delayTime) {
        if (isAppFront && !front) {
            isAppFront = front;
            mEventDispatcher.sendMessageDelayed(mEventDispatcher.obtainMessage(EventDispatcherEnum.UI_EVENT_APP_GOBACKGROUND),delayTime);
        } else if (!isAppFront && front) {
            isAppFront = front;
            mEventDispatcher.sendMessageDelayed(mEventDispatcher.obtainMessage(EventDispatcherEnum.UI_EVENT_APP_GOFRONT),delayTime);
        }
    }

    /**
     * 一些比较耗时需延时加载的任务
     */
    private static Runnable delayTaskRunner = new Runnable() {

        @Override
        public void run() {
            if (!isRunDelayTask()) {
                synchronized (mApp) {
                    if (!isRunDelayTask()) {
                        /**
                         * 耗时任务
                         */
                    }
                }
            }
        }
    };

    public static TTFragmentActivity getCurActivity() {
        return mCurActivity;
    }

    public static void setCurActivity(TTFragmentActivity mCurActivity) {
        ChrisApp.mCurActivity = mCurActivity;
    }

    public static Activity getTabActivity() {
        return mTabActivity;
    }

    public static void setTabActivity(Activity tabActivity) {
        mTabActivity = tabActivity;
    }

    public static Runnable getDelayTaskRunner() {
        return delayTaskRunner;
    }

    public static boolean isRunDelayTask() {
        return bRunDelayTask;
    }

    private static void disableComponent(PackageManager pm, String name) {
        try {
            ComponentName component = new ComponentName(self().getPackageName(), name);
            pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
}
