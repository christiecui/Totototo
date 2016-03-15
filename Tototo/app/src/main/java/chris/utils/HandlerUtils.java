package chris.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cuiqi on 15/6/24.
 */
public class HandlerUtils {

    private static Handler mManinHandler;

    private static Object mMainHandlerLock = new Object();

    private static final int HANDER_THREAD_SIZE=3;
    private static Handler[] handlersForDownload;
    private static final AtomicInteger currentHandlerIndex=new AtomicInteger(0);

    static
    {
        handlersForDownload=new Handler[HANDER_THREAD_SIZE];
        for(int i=0;i<HANDER_THREAD_SIZE;i++)
        {
            HandlerThread handlerThread=new HandlerThread("download-handler-thread-"+i);
            handlerThread.start();
            try
            {
                handlersForDownload[i]=new Handler(handlerThread.getLooper());
            }
            catch (Exception ex )
            {
                ex.printStackTrace();
                try{
                    handlersForDownload[i]=new Handler(handlerThread.getLooper());
                }
                catch (Exception ex2){}
            }
        }
    }

    /**
     * 取得UI线程Handler
     * @return
     */
    public static synchronized Handler getMainHandler(){
        if (mManinHandler == null) {
            mManinHandler = new Handler(Looper.getMainLooper());
        }
        return mManinHandler;
//		return new Handler(Looper.getMainLooper());
    }

    /**
     * 取得一个非主线Handler，每次获得这个handler都会产生一个线程，所以获得到的这个handler必须做复用！！！
     * @param threadName
     * @return
     */
    public static Handler getHandler(String threadName){
        if(TextUtils.isEmpty(threadName)){
            threadName = "default-thread";
        }
        HandlerThread handlerThread = new HandlerThread(threadName);
        handlerThread.start();
        Looper loop = handlerThread.getLooper();
        if( loop != null)
            return new Handler(loop);
        else
            return null;

    }


    /**
     * 取得下载专用的handler，这个handler和对应的HandlerThread是复用的线程
     * @return
     */
    public static Handler getDownloadHandler(){
        return handlersForDownload[currentHandlerIndex.getAndIncrement() % HANDER_THREAD_SIZE];
    }

}
