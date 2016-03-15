package chris.engine;

import android.util.SparseIntArray;

import org.json.JSONObject;

import chris.listener.ProtocolListener;
import chris.protocol.ProtocolClient;
import chris.protocol.ResultCode;
import chris.utils.CommonUtil;


/**
 * Created by cuiqi on 16/1/14.
 * modified by christiecui on 2016/3/15
 */
public abstract class BaseEngine implements ProtocolListener {
    //seq和 request映射关系
    private SparseIntArray seqMap = new SparseIntArray();

    protected  int send(int seq, String request){
        int requestId = ProtocolClient.getInstance().sendRequest(request,this);
        if(seq <= 0){
            seq = getUniqueId();
        }
        seqMap.put(requestId,seq);
        return requestId;
    }

    protected boolean cancel(int seq){
        int index = seqMap.indexOfValue(seq);
        if(index >= 0){
            int requestId = seqMap.keyAt(index);
            ProtocolClient.getInstance().cancelRequest(requestId);
            seqMap.delete(requestId);
            return true;
        }else{
            return false;
        }
    }

    protected int getUniqueId(){
        return CommonUtil.getUniqueId();
    }

    @Override
    public void onProtocolRequestFinish(int requestId, int errorCode, String request, JSONObject response) {

        final int seq = seqMap.get(requestId, -1);
        if(seq == -1){
            return;
        }
        seqMap.delete(requestId);

        // 一个正常的网络回包
        if (errorCode == ResultCode.Code_OK && response != null && request != null) {
            onRequestSuccessed(seq, request, response);
        } else {
            onRequestFailed(seq, errorCode, request, response);
        }
    }

    /**
     * 请求成功
     * @param seq			请求seq
     * @param request		请求包
     * @param response		回应包
     */
    protected abstract void onRequestSuccessed(int seq, String request, JSONObject response);

    /**
     * 请求失败
     * @param seq			请求seq
     * @param errorCode		错误码
     * @param request		请求包
     * @param response		回应包
     */
    protected abstract void onRequestFailed(int seq, int errorCode, String request, JSONObject response);
}
