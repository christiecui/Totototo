package com.example.cuiqi.htmlphrase;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cuiqi on 16/6/3.
 */
public class HtmlParseUtil {
    public static String getHtmlString(InputStream is) {
        InputStreamReader read = null;
        try {
            read = new InputStreamReader(is,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(read);
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return replaceBlank(sb.toString());
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String replaceScript(String str){
        String dest = "";
        if(str!= null) {
            Pattern p = Pattern.compile("(?is)<script[^>]*?>.*?<\\/script>");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

//    navigation-bar-title="Web+ Demo"
    public static String getTitle(String str){
        String result= "";
        if(str != null) {
            Pattern p = Pattern.compile("navigation-bar-title=\".*?\"");
            Matcher m = p.matcher(str);
            result = m.replaceAll("");
        }
        return result;
    }

    public static String extractScript(String str){
        String array[] = str.split("<script>");
        String content[] = array[1].split("</script>");
        return content[0];
    }

    public static String extractPage(String str){
//        String array[] = str.split("<page");
//        String content[] = array[1].split("</page>");
//        return content[0];
        return "<div id=\"container\"></div>";
    }

    public static String replaceWxScript(String src , String content){
        String dest = "";

        if(content != null) {
            String pattern = "<wx-import src=\"http://203.195.235.76:8888/jssdk.js\" />";
            content = "<script type=\"text/javascript\">"+"\n"+content+"\n"+"</script>";
            dest = src.replace(pattern,content);
        }
        return dest;
    }

    public static String replaceWxCss(String src , String content){
        String dest = "";

        if(content != null) {
            String pattern = "<wx-import src=\"https://res.wx.qq.com/open/libs/weui/0.3.0/weui.css\" />";
            content = "<style type=\"text/css\">"+content+"</style>";
            dest = src.replace(pattern,content);
        }
        return dest;
    }



    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    public static String getSdCardPath() {
        boolean exist = isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+"/htmlparse";
            Log.i("cuiqi",sdpath);
        } else {
            sdpath = "不适用";
        }
        return sdpath;

    }



    public static boolean writeFile(File file , String content){

        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(content);
            bw.flush();
            Log.i("cuiqi","success write");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readFile(File file) {

        InputStreamReader read = null;
        try {
            read = new InputStreamReader(new FileInputStream(file),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(read);
        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return sb.toString();
    }


    /**
     * 不要在主线程调用
     * @param pageURL
     * @param encoding
     * @return
     */
    public static String getHTML(String pageURL, String encoding) {
        StringBuilder pageHTML = new StringBuilder();
        try {
            URL url = new URL(pageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "MSIE 7.0");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
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
}
