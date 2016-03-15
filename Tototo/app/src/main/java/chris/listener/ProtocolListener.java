package chris.listener;

import org.json.JSONObject;

/**
 * Created by cuiqi on 16/1/14.
 * modified by christiecui on 2016/3/15
 */
public interface ProtocolListener {
    /**
     * 协议请求返回回调
     * @param resultCode 请求吗
     * @param errorCode  错误码
     * @param request 请求
     * @param response 响应
     */
    public void onProtocolRequestFinish(int resultCode, int errorCode, String request, JSONObject response);
}
