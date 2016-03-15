package chris.event;

import android.os.Handler;
import android.os.Message;

/**
 * 事件分发类.
 * Created by christiecui on 2014/11/6.
 * modified by christiecui on 2016/3/15
 */
public class EventDispatcher extends Handler {

    private static EventDispatcher sInstance = null;
    private EventListener mListener = null;

    public static EventDispatcher getInstance(EventListener listener) {

        if(sInstance == null){
            sInstance = new EventDispatcher(listener);
        }
        return sInstance;
    }

    private EventDispatcher(EventListener listener) {
        mListener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        if(mListener != null){
            mListener.handleEvent(msg);
        }
        super.handleMessage(msg);
    }

    public void setListener(EventListener listener) {
        mListener = listener;
    }

}

