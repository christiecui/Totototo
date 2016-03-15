package chris.event;

import android.os.Message;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import chris.listener.CacheEventListener;
import chris.listener.ConnectionEventListener;
import chris.listener.UIEventListener;


/**
 * Created by christiecui on 2014/11/6.
 * modified by christiecui on 2016/3/15
 */
public class EventController implements EventListener {

    private ConcurrentHashMap<Integer, List<WeakReference<UIEventListener>>> mUIEventListeners = null;
    private ConcurrentHashMap<Integer, List<WeakReference<CacheEventListener>>> mCacheEventListeners = null;
    private ConcurrentHashMap<Integer, List<WeakReference<ConnectionEventListener>>> mConnectionEventListeners = null;
    // 用listener使用弱应用对象
    protected ReferenceQueue<UIEventListener> mUIListenerReferenceQueue;
    protected ReferenceQueue<CacheEventListener> mCacheListenerReferenceQueue;
    protected ReferenceQueue<ConnectionEventListener> mConListenerReferenceQueue;

    private static EventController sInstance = null;

    public static synchronized EventController getInstance() {
        if (sInstance == null) {
            sInstance = new EventController();
        }
        return sInstance;
    }

    private EventController() {
        mUIEventListeners = new ConcurrentHashMap<Integer, List<WeakReference<UIEventListener>>>();
        mCacheEventListeners = new ConcurrentHashMap<Integer, List<WeakReference<CacheEventListener>>>();
        mConnectionEventListeners = new ConcurrentHashMap<Integer, List<WeakReference<ConnectionEventListener>>>();
        mUIListenerReferenceQueue = new ReferenceQueue<UIEventListener>();
        mCacheListenerReferenceQueue = new ReferenceQueue<CacheEventListener>();
        mConListenerReferenceQueue = new ReferenceQueue<ConnectionEventListener>();
    }

    public void addCacheEventListener(int eventId, CacheEventListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (mCacheEventListeners) {

            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends CacheEventListener> releaseListener = null;
            List<WeakReference<CacheEventListener>> list = mCacheEventListeners.get(eventId);
            if (list != null) {
                while ((releaseListener = mCacheListenerReferenceQueue.poll()) != null) {
                    list.remove(releaseListener);
                }
            }
            // 开始注册
            if (list == null) {
                list = new ArrayList<WeakReference<CacheEventListener>>();
                mCacheEventListeners.put(eventId, list);
            }
            for (WeakReference<CacheEventListener> weakListener : list) {
                CacheEventListener listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            WeakReference<CacheEventListener> newListener = new WeakReference<CacheEventListener>(listener, mCacheListenerReferenceQueue);
            list.add(newListener);
        }
    }

    public void removeCacheEventListener(int eventId, CacheEventListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mCacheEventListeners) {
            List<WeakReference<CacheEventListener>> list = mCacheEventListeners.get(eventId);
            if (list != null) {
                for (Iterator<WeakReference<CacheEventListener>> i = list.iterator(); i.hasNext();) {
                    WeakReference<CacheEventListener> weakListener = i.next();
                    if (weakListener.get() == listener) {
                        list.remove(weakListener);
                        if (list.isEmpty()) {
                            mCacheEventListeners.remove(eventId);
                        }
                        return;
                    }
                }

            }
        }
    }

    public void addUIEventListener(int eventId, UIEventListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mUIEventListeners) {
            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends UIEventListener> releaseListener = null;
            List<WeakReference<UIEventListener>> list = mUIEventListeners.get(eventId);
            if (list != null) {
                while ((releaseListener = mUIListenerReferenceQueue.poll()) != null) {
                    list.remove(releaseListener);
                }
            }
            // 注册
            if (list == null) {
                list = new ArrayList<WeakReference<UIEventListener>>();
                mUIEventListeners.put(eventId, list);
            }
            for (WeakReference<UIEventListener> weakListener : list) {
                UIEventListener listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            WeakReference<UIEventListener> newListener = new WeakReference<UIEventListener>(listener, mUIListenerReferenceQueue);
            list.add(newListener);
        }
    }

    public void removeUIEventListener(int eventId, UIEventListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mUIEventListeners) {
            List<WeakReference<UIEventListener>> list = mUIEventListeners.get(eventId);
            if (list != null) {
                for (Iterator<WeakReference<UIEventListener>> i = list.iterator(); i.hasNext();) {
                    WeakReference<UIEventListener> weakListener = i.next();
                    if (weakListener.get() == listener) {
                        list.remove(weakListener);
                        if (list.isEmpty()) {
                            mUIEventListeners.remove(eventId);
                        }
                        return;
                    }
                }

            }
        }
    }

    public void addConnectionEventListener(int eventId, ConnectionEventListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mConnectionEventListeners) {
            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends ConnectionEventListener> releaseListener = null;
            List<WeakReference<ConnectionEventListener>> list = mConnectionEventListeners.get(eventId);
            if (list != null) {
                while ((releaseListener = mConListenerReferenceQueue.poll()) != null) {
                    list.remove(releaseListener);
                }
            }
            // 注册
            if (list == null) {
                list = new ArrayList<WeakReference<ConnectionEventListener>>();
                mConnectionEventListeners.put(eventId, list);
            }
            for (WeakReference<ConnectionEventListener> weakListener : list) {
                ConnectionEventListener listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            WeakReference<ConnectionEventListener> newListener = new WeakReference<ConnectionEventListener>(listener, mConListenerReferenceQueue);
            list.add(newListener);
        }
    }

    public void removeConnectionEventListener(int eventId, ConnectionEventListener listener) {
        synchronized (mConnectionEventListeners) {
            List<WeakReference<ConnectionEventListener>> list = mConnectionEventListeners.get(eventId);
            if (list != null) {
                for (Iterator<WeakReference<ConnectionEventListener>> i = list.iterator(); i.hasNext();) {
                    WeakReference<ConnectionEventListener> weakListener = i.next();
                    if (weakListener.get() == listener) {
                        list.remove(weakListener);
                    }
                    if (list.isEmpty()) {
                        mConnectionEventListeners.remove(eventId);
                    }
                    return;
                }

            }
        }
    }

    private void handleUIEvent(Message msg) {
        List<WeakReference<UIEventListener>> lt = mUIEventListeners.get(msg.what);
        if (lt != null) {
            List<WeakReference<UIEventListener>> list = new ArrayList<WeakReference<UIEventListener>>(lt);
            if (list != null) {
                for (Iterator<WeakReference<UIEventListener>> i = list.iterator(); i.hasNext();) {
                    UIEventListener listener = i.next().get();
                    if (listener != null) {
                        listener.handleUIEvent(msg);
                    }
                }
            }
        }
    }

    private void handleCacheEvent(Message msg) {
        List<WeakReference<CacheEventListener>> lt = mCacheEventListeners.get(msg.what);
        if (lt != null) {
            List<WeakReference<CacheEventListener>> list = new ArrayList<WeakReference<CacheEventListener>>(lt);
            if (list != null) {
                for (Iterator<WeakReference<CacheEventListener>> i = list.iterator(); i.hasNext();) {
                    CacheEventListener listener = i.next().get();
                    if (listener != null) {
                        listener.handleCacheEvent(msg);
                    }
                }
            }
        }
    }

    private void handleConnectionEvent(Message msg) {
        List<WeakReference<ConnectionEventListener>> lt = mConnectionEventListeners.get(msg.what);
        if (lt != null) {
            List<WeakReference<ConnectionEventListener>> list = new ArrayList<WeakReference<ConnectionEventListener>>(lt);
            if (list != null) {
                for (Iterator<WeakReference<ConnectionEventListener>> i = list.iterator(); i.hasNext();) {
                    ConnectionEventListener listener = i.next().get();
                    if (listener != null) {
                        listener.handleConnectionEvent(msg);
                    }
                }
            }
        }
    }

    @Override
    public void handleEvent(Message msg) {
        if (msg.what > EventDispatcherEnum.UI_EVENT_BEGIN && msg.what < EventDispatcherEnum.UI_EVENT_END) {
            handleUIEvent(msg);
        } else if (msg.what > EventDispatcherEnum.CACHE_EVENT_START && msg.what < EventDispatcherEnum.CACHE_EVENT_END) {
            handleCacheEvent(msg);
        } else if (msg.what > EventDispatcherEnum.CONNECTION_EVENT_START && msg.what < EventDispatcherEnum.CONNECTION_EVENT_END) {
            handleConnectionEvent(msg);
        }
    }

}
