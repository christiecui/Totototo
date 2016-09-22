package com.example.cuiqi.htmlphrase.download2;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by cuiqi on 16/8/21.
 */
public class AppBrandDownloadWorker implements Runnable {

    private static final String TAG = "AppBrandDownloadWorker";
    private final AppBrandDownloadCallback callback;
    private boolean isRunning =false;
    private String uri = "http://dlied5.myapp.com/myapp/363/hlddz/10000145_fy_com.qqgame.hlddz_166.apk";
    private String filename = Environment.getExternalStorageDirectory().getAbsolutePath()+"/cuiqiqiqi"+"01.apk";

    public AppBrandDownloadWorker(String uri, String filename, AppBrandDownloadCallback callback) {
        this.uri = uri;
        this.filename = filename;
        this.callback = callback;
    }
    @Override
    public void run() {
        URL url;
        isRunning = true;
        FileOutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            url = new URL(uri);
            Log.e("url", uri);
            URLConnection connection;
            connection = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) connection;
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            float totalsize = connection.getContentLength();
            float downloadsize = 0;
            if (conn.getResponseCode()==200) {
                 inputStream = conn.getInputStream();

                File filePath = new File(filename);

                outputStream = new FileOutputStream(filePath);
                byte [] by = new byte[1024];
                int len=0;
                while ((len = inputStream.read(by))!=-1 && isRunning) {
                    outputStream.write(by,0,len);
                    downloadsize = downloadsize + len;
                    Log.i(TAG,"downloadsize = "+ downloadsize + "total size" + totalsize);
                    if(totalsize > 0){
                        int percent = (int) ((downloadsize/totalsize) * 100);
                        Log.i(TAG,"percent =" + percent);
                    }
                }
                outputStream.flush();
                inputStream.close();
                conn.disconnect();
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(TAG,e.toString());
//            Log.i(TAG,)
        }finally {
            isRunning = false;
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
