package chris.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cuiqi on 16/5/31.
 */
public class HtmlParseUtil {

    public static String getHtmlString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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


    public static String extractScript(String str){
        String array[] = str.split("<script>");
        String content[] = array[1].split("</script>");
        return content[0];
    }

    public static String extractPage(String str){
        String array[] = str.split("<page");
        String content[] = array[1].split("</page>");
        return content[0];
    }

    public static String replaceWxScript(String src , String content){
        String dest = "";

        String pattern = "<wx-import src=\"https://res.wx.qq.com/open/libs/weui/0.3.0/weui.css\">";
        dest = src.replace(pattern,content);
        return dest;
    }

    public static String replaceWxCss(String src , String content){
        String dest = "";
        String pattern = "<wx-import src=\"http://203.195.235.76:8888/jssdk.js\">";
        dest = src.replace(pattern,content);
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
                    .getAbsolutePath();
            Log.i("cuiqi",sdpath);
        } else {
            sdpath = "不适用";
        }
        return sdpath;

    }



    public static boolean writeFile(File file , String content){

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.flush();
            Log.i("cuiqi","success write");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readFile(File file) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
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
}
