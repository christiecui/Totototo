package chris.listener;

import android.os.Message;

/**
 * UI事件处理接口
 * Created by christiecui on 2014/11/6.
 * modified by christiecui on 2016/3/15
 */

public interface UIEventListener {
    public void handleUIEvent(Message msg);
}
