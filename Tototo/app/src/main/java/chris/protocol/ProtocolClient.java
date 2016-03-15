package chris.protocol;

import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import chris.application.ChrisApp;
import chris.listener.ProtocolListener;
import chris.utils.CommonUtil;

/**
 * 协议的解包
 * 因为不会写后台，所以协议没有确定，因此requestId,错误码什么的都没有定
 * 所以暂时封装到逻辑,用json实现
 * Created by cuiqi on 16/1/14.
 */
public class ProtocolClient {
    private  static ProtocolClient mInstance = null;

    private RequestQueue mRequestQueue = null;

    //缓存id和request关系
    private ConcurrentHashMap<Integer,ProtocolListener> requestMap ;

    //请求url
    private String url ="http://vp4themarket.applinzi.com";

    //requestId
    private int mQuestId = 0;

    private ProtocolClient(){
        requestMap = new ConcurrentHashMap<Integer,ProtocolListener>();
    }

    /*获取单例*/
    public  static ProtocolClient getInstance() {
        if (null == mInstance) {
                if (null == mInstance) {
                    mInstance = new ProtocolClient();
                }
        }
        return mInstance;
    }

    public int sendRequest(String request,ProtocolListener listener){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(ChrisApp.self());
        }

        mQuestId = CommonUtil.getUniqueId();

        requestMap.put(mQuestId, listener);

        JsonRequest<JSONObject> jsonRequest = packRequest(request);

        mRequestQueue.add(jsonRequest);

        return 0;
    }

    private JsonRequest<JSONObject> packRequest(final String request) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",request);
            jsonObject.put("number","13991275572");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> resultRequest = new JsonObjectRequest(Request.Method.POST,url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cuiqi", "response -> " + response.toString());

                        // TODO: 16/1/20 理论上服务器应该给我回requestId，目前不会写动态服务器，因此只处理0
                        ProtocolListener listner = requestMap.remove(0);
                        if(listner == null){
                            return;
                        }

                        // TODO: 16/1/20 错误码请求码都没有，所以都填0
                        listner.onProtocolRequestFinish(0,0,"",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cuiqi", error.getMessage(), error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        };

        return resultRequest;

    }

    public void cancelRequest(int requestId) {
        // TODO: 16/1/20 还没处理好id，listener和request实体 后续封装成一个类
//        mRequestQueue.cancelAll(this);
    }


}
