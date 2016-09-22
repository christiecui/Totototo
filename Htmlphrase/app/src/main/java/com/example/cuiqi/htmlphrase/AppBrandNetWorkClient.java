package com.example.cuiqi.htmlphrase;

/**
 * Created by cuiqi on 16/7/6.
 */

import android.util.Log;

import com.example.cuiqi.htmlphrase.utils.AppBrandNetWorkDataEncryptLogic;
import com.example.cuiqi.htmlphrase.utils.SHA1;
import com.example.cuiqi.htmlphrase.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cuiqi on 16/7/6.
 * AppBrand NetWork Client
 */
public class AppBrandNetWorkClient {

    private static final String TAG = "MicroMsg.AppBrandNetWorkClient";
    private static final int SUCCESS = 0;
    private static final int ERROR = 1;

    private AppBrandNetWorkClient(){}

    private static class AppBrandNetWorkSingletonHolder{
        private static AppBrandNetWorkClient instance = new AppBrandNetWorkClient();
    }

    public static AppBrandNetWorkClient getInstance(){
        return AppBrandNetWorkSingletonHolder.instance;
    }

    /**
     * request by post
     * @param openid
     * @param signature
     * @param paras
     * @param callback
     */
    public void request(String openid , String signature , Map<String, String> paras, AppBrandNetWorkCallback callback){

        String url = (String)paras.get("url");
        String data = (String)paras.get("data");
        String contentType = (String)paras.get("contentType");
        //可能会有问题,看具体参数了
        boolean withCredentials = getInt((String) paras.get("withCredentials"), 1) == 1;

        if(Util.isNullOrNil(signature) &&Util.isNullOrNil(openid) && Util.isNullOrNil(url)  && data == null){
            return;
        }

        //调试代码
        openid = "oqLVYt7umywjyqjubRI5lMPx7aAA";
        signature = "aaaaaaaa";
        byte[] postData = data.getBytes(Charset.forName("UTF-8"));
        sendHttpsRequest(openid, signature, url, postData, withCredentials, contentType,callback);
    }

    /**
     * config httpURLConnection
     * @param openid
     * @param signature
     * @param postData
     * @param withCredentials
     * @param contentType
     * @throws ProtocolException
     */
    private void sendHttpsRequest(String openid, String signature, String url, byte[] postData, Boolean withCredentials, String contentType, AppBrandNetWorkCallback callback) {
        HttpURLConnection httpsURLConnection = null;
        InputStream inputStream;
        DataOutputStream outputStream;

        try {
            URL extractUrl = new URL(url);
            httpsURLConnection = (HttpURLConnection) extractUrl.openConnection();
            if (httpsURLConnection != null) {
                configHttpConnection(openid, signature, postData, withCredentials,contentType, httpsURLConnection);

                outputStream = new DataOutputStream(httpsURLConnection.getOutputStream ());
                outputStream.write (postData);
                outputStream.flush ();
                outputStream.close ();

                inputStream = httpsURLConnection.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                if (inputStream != null) {
                    int readBytes;
                    byte[] rawBuffer = new byte[1024];
                    while ((readBytes = inputStream.read(rawBuffer)) != -1) {
                        buffer.write(rawBuffer, 0, readBytes);
                    }

                    buffer.flush();
                    inputStream.close();
                }

                try{
                    callback.onResult(SUCCESS,buffer.toString());
                } catch(Exception e){
                    callback.onException(httpsURLConnection, e);
                }
            }

        } catch (Exception e) {
            callback.onException(httpsURLConnection, e);
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
    }


    /**
     * config httpURLConnection
     * @param openid
     * @param signature
     * @param postData
     * @param withCredentials
     * @param contentType
     * @param httpURLConnection  @throws ProtocolException
     */
    private void configHttpConnection(String openid, String signature, byte[] postData, Boolean withCredentials, String contentType, HttpURLConnection httpURLConnection) throws ProtocolException {
        //todo 参数
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        httpURLConnection.setRequestProperty("User-Agent", "AppBrandNetWorkClient");
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("charset", "utf-8");
        httpURLConnection.setUseCaches( false );

        httpURLConnection.setRequestProperty("Content-Length", Integer.toString( postData.length ));

        httpURLConnection.setRequestProperty("contentType", contentType);
        //wx-network head
        if(withCredentials == true) {
            httpURLConnection.setRequestProperty("wx-openid", openid);
            httpURLConnection.setRequestProperty("wx-signature", signature);
        }
    }

    /**
     * AppBrandNetWorkCallback
     */
    public interface AppBrandNetWorkCallback{
        public void onResult(int errMsg , String data);
        public void onException(HttpURLConnection httpURLConnection, Exception e);
    }

    public static int getInt(final String string, final int def) {
        try {
            return (string == null || string.length() <= 0) ? def : Integer.parseInt(string);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return def;
    }
}
