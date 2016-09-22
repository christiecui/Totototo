package com.example.cuiqi.htmlphrase.upload;


import android.util.Log;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cuiqi on 16/8/30.
 */
public class Upload {

    public void upload(){
        new Thread(new AppBrandNetworkUploadWorker()).start();
    }



    public class AppBrandNetworkUploadWorker implements Runnable{


        private static final String TAG = "asdasd";
        private String BOUNDARY =  UUID.randomUUID().toString();
        private String PREFIX = "--" ;
        private String LINE_END = "\r\n";
        private String CONTENT_TYPE = "multipart/form-data";

        @Override
        public void run() {
            uploadFile();

        }

        private void uploadFile()
        {

            String requestURL = "https://stream.weixin.qq.com/weapp/UploadFile";
            String filepath = "/storage/emulated/0/tencent/MicroMsg/wxafiles/wx56924b489f2c837d/99deb32f0332e2ae1735f7af13171e66bd0de8e9dc2b0dc302841885df0ac4657c2794d604aa75800e010bf2643c9506d00f3916cb863469.tmp";
            String name = "lala";
            String mimeType = "asd";
            String filename = "wx-file." + mimeType;
            Map<String, String> formData = new HashMap<>();
            Map<String, String> header = new HashMap<>();

            HttpURLConnection httpURLConnection = null;
            DataOutputStream outputStream = null;
            InputStream inputStream = null;


            String twoHyphens = "--";
            String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
            String lineEnd = "\r\n";

            String result = "";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            try {
                File file = new File(filepath);
                FileInputStream fileInputStream = new FileInputStream(file);

                URL url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }

                outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: " + mimeType + lineEnd);
                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

                outputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0 ) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(lineEnd);

                // Upload POST Data
                Iterator<String> keys = formData.keySet().iterator();
                while (keys.hasNext() ) {
                    String key = keys.next();
                    String value = formData.get(key);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(value);
                    outputStream.writeBytes(lineEnd);
                }

                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                if (200 != httpURLConnection.getResponseCode()) {
                    return;
                }

                inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();

                fileInputStream.close();
                inputStream.close();
                outputStream.flush();
                outputStream.close();

                Log.i(TAG, "success");
            } catch (Exception e) {
                Log.e(TAG, "exception : %s ,url is %s filepath %s ");
            } finally {
                Log.i(TAG, "finally : url is " + requestURL + ", filepath" + filepath);
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "exception : %s ,url is %s filepath %s ");
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "exception : %s ,url is %s filepath %s ");
                    }
                }
            }

        }

    }
}
