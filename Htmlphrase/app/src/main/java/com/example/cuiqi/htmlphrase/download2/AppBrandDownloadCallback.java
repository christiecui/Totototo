package com.example.cuiqi.htmlphrase.download2;

/**
 * Created by cuiqi on 16/8/21.
 */
public interface AppBrandDownloadCallback {
    public void onFinish(String filename);
    public void onUpdate(String filename, int percent);
    public void onError(String filename , String errorMessage);
    public void onStart(String filename, String url);
}
