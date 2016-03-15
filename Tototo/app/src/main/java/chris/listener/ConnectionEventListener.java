package chris.listener;

import android.os.Message;

/**
 * Created by christiecui on 2014/11/6.
 * modified by christiecui on 2016/3/15
 *	连接事件接口，加入全局消息控制中
 */
public interface ConnectionEventListener {

    public void handleConnectionEvent(Message msg);

}
