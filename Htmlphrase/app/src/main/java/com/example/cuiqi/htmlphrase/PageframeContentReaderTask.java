package com.example.cuiqi.htmlphrase;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cuiqi on 16/6/7.
 */
public class PageframeContentReaderTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    private  OnTaskCompleted listener;

    public PageframeContentReaderTask(OnTaskCompleted listener){
        this.listener=listener;
    }

    protected String doInBackground(String... urls) {

        StringBuilder pageHTML = new StringBuilder();
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "MSIE 7.0");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                pageHTML.append(line);
                pageHTML.append("\r\n");
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageHTML.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onTaskCompleted(result);
    }



    public interface OnTaskCompleted{
        void onTaskCompleted(String result);
    }
}
