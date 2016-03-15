package chris.event;

import android.os.Message;

/**
 * 抽象事件接口
 * Created by christiecui on 2014/11/6.
 * modified by christiecui on 2016/3/15
 */
public interface EventListener {
    public void handleEvent(Message msg);
}
