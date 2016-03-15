package chris.listener;

import android.os.Message;

/**
 * 缓存事件 处理接口
 * Created by christiecui on 2014/11/6.
 * modified by christiecui on 2016/3/15
 */
public interface CacheEventListener {
    public void handleCacheEvent(Message msg);
}
