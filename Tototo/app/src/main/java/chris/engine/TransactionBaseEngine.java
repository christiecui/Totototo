package chris.engine;


import chris.engine.callback.ActionCallback;
import chris.engine.callback.CallbackHelper;
import chris.utils.HandlerUtils;

/**
 * 业务基类，继承这个类可以有回调方法同志有UI
 * Created by cuiqi on 16/1/15.
 */
public abstract class TransactionBaseEngine<T extends ActionCallback> extends BaseEngine{

    private CallbackHelper<T> mCallbacks = new CallbackHelper<T>();

    public TransactionBaseEngine() {
    }

    public void register(T cb){
        mCallbacks.register(cb);
    }

    public void unregister(T cb){
        mCallbacks.unregister(cb);
    }

    /**
     * 通知UI数据改变了。在主线程通知
     * @param caller
     */
    protected void notifyDataChangedInMainThread(final CallbackHelper.Caller<T> caller) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataChanged(caller);
            }
        });
    }

    /**
     * 通知UI数据改变了。这个方法跑在哪个线程取决于调用方。如果需要通知UI，而没有在主线程调用，那么请使用notifyDataChangedInMainThread
     * @param caller
     */
    protected void notifyDataChanged(CallbackHelper.Caller<T> caller){
        mCallbacks.broadcast(caller);
    }

    /**
     * 在主线程执行一些事情。UI数据变更跟通知一定要在同一个runnable里执行完毕。通知的话，一定要使用notifyDataChanged，而不是notifyDataChangedInMainThread。保证数据变更跟通知是一个原子操作
     * @param action
     */
    protected void runOnUiThread(Runnable action){
        HandlerUtils.getMainHandler().post(action);
    }
}
