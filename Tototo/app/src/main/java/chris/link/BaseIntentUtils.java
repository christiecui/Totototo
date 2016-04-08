package chris.link;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 跳转能力功能集合类
 * Created by cuiqi on 15/7/7.
 */
public class BaseIntentUtils {

    public static final String SCHEME_MAST = "tmast";
    public static final String SCHEME_MARKET = "market";
    public static final String SCHEME_TPMAST = "tpmast";// 内部使用的scheme

    // ------------------------ google market山寨协议常量定义开始 --------------

    public static final String PAGE_LIST = "list"; // 下载列表
    public static final String PAGE_IMAGE = "image"; //图片实验页
    public static final String PAGE_MAIN = "main"; //图片实验页
    // ------------------------- google market山寨协议常量定义结束 --------------

    // ------------------------ 程序内的山寨协议常量定义开始 --------------

    public static final String PAGE_APP_DETAIL = "appdetails"; // 软件详情页

//    public static final String SPECIAL_ENCRYPT = "encrypt";// 加密协议
//    public static final String UPDATE_DO、WNLOAD = "updatedownload";//更新下载任务信息
    public static final String EXT_KEY = "ex、t";
    public static final String PAGE_SETTING = "setting";
    public static final String SELFUPATE_CHECK = "selfupdatecheck";
    public static final String UNKNOWN = "unknown";
    public static final String KEY_ACTION_URL = "com.tencent.assistant.ACTION_URL";

    public static final String SELF_LINK = "&"+ ActionKey.KEY_SELF_LINK + "=1";
    public static final String SELF_LINK_F = "?"+ ActionKey.KEY_SELF_LINK + "=1";

    // ------------------------ 程序内的山寨协议常量定义结束 --------------

    private static final String TAG = "BaseIntentUtils";

    protected static boolean onMast(Context context, Uri uri, Bundle extras) {
        try {
            Log.v(TAG,uri.getHost());
            String extStr = uri.getQueryParameter(EXT_KEY);
            if(!TextUtils.isEmpty(extStr)) {
                IntentUtils.forward(context, extStr);
                return true;
            }

            String host = uri.getHost();
            if (host.equals(PAGE_LIST)) {
//                onPageList(context, uri, extras);
                return true;
            } else if(host.equals(PAGE_IMAGE)) {
//                OnPageImageTest(context, uri);
                return true;
            }else if(host.equals(PAGE_MAIN)){
//                onMainActivity(context);
                return true;
            } else {
                return IntentUtils.onMastExtra(context, uri, extras);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    protected static Uri buildUri(String scheme, String host, HashMap<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append(scheme);
        sb.append("://");
        sb.append(host);
        if (map != null && map.size() > 0) {
            sb.append("?");
            Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append("&");
            }
        }
        if (sb.toString().endsWith("&")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        Log.v(TAG, "uri:" + sb.toString());
        return Uri.parse(sb.toString());
    }



//    private static void OnPageImageTest(Context context,Uri uri) {
//        Intent intent = new Intent(context, TestActivity.class);
//
//        if(!(context instanceof Activity)) {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//
//        context.startActivity(intent);
//    }

//    private static void onPageList(Context context,Uri uri,Bundle extras) {
//        Intent intent = new Intent(context, ListActivity.class);
//
//        Log.v(TAG,"uri = "+ uri + "bundle = " + extras);
//        if(!(context instanceof Activity)) {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//
//        context.startActivity(intent);
//    }

//    private static void onMainActivity(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        if(!(context instanceof Activity)) {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//
//        context.startActivity(intent);
//    }
    /**
     * Channel : 当只有一个参数时调用
     * @param scheme
     * @param host
     * @param paramKey
     * @param paramValue
     * @return
     */
    protected static Uri buildUri(String scheme, String host, String paramKey, String paramValue) {
        StringBuffer sb = new StringBuffer();
        sb.append(scheme).append("://").append(host);
        if (!TextUtils.isEmpty(paramKey) && !TextUtils.isEmpty(paramValue)) {
            sb.append("?").append(paramKey).append("=").append(paramValue);
        }

        Log.v(TAG, "uri:" + sb.toString());
        return Uri.parse(sb.toString());
    }

    protected static boolean onHttp(Context context, Uri uri) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", uri.toString());
//        return onWebView(context, buildUri(SCHEME_MAST, PAGE_WEBVIEW, map), null);
        //todo 跳转到webView
        return false;
    }

    protected static boolean onMarket(Context context, Uri uri) {
            return false;
    }

    protected static boolean onTpMast(Context context, Uri uri, Bundle extras) {
        boolean flag = onMast(context, uri, extras);
        if(!flag){
            String host = uri.getHost();
        }
        return flag;
    }

    /**
     * 是否有activity可以处理
     *
     * @param context
     * @param intent
     * @return
     */
    protected static boolean hasAbility(Context context, Intent intent) {
	    /* 下方这个逻辑用来判断从外部跳转时，tmast下的host是否能解析成功 */
        Uri uri = intent.getData();
        if(uri != null){
            String scheme = uri.getScheme();
            if(scheme != null && scheme.equals(SCHEME_MAST)){
                String host = uri.getHost();
                return (isHostAvailable(host));
            }
        }

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
        return list != null && list.size() > 0;
    }

    /**
     * host是否是版本里能正常解析成Activity的判断
     * @param host
     * @return
     */
    protected static boolean isHostAvailable(String host){
        if (host.equals(PAGE_APP_DETAIL) || host.equals(PAGE_SETTING) || host.equals(SELFUPATE_CHECK) ){
            return true;
        }
        return false;
    }

    /**
     * 从url中获取version code
     *
     * @param url
     * @return
     */
    protected static int getVersionCodeFromUrl(String url) {
        int versionCode = -1;
        if (TextUtils.isEmpty(url)) {
            return -1;
        }

        Uri uri = Uri.parse(url);
        String version = uri.getQueryParameter("versioncode");
        if (null != version) {
            try {
                versionCode = Integer.parseInt(version);

            } catch (NumberFormatException e) {
            }
        }
        return versionCode;
    }

    protected static String getTaskIdFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            Uri uri = Uri.parse(url);
            String taskid = uri.getQueryParameter("taskid");
            return taskid;
        } catch (Exception e) {
            return null;
        }
    }

    protected static String getActionTypeFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            Uri uri = Uri.parse(url);
            String actiontype = uri.getQueryParameter("actiontype");
            return actiontype;
        } catch (Exception e) {
            return null;
        }
    }

    protected static String getHostPackageNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            Uri uri = Uri.parse(url);
            String hostpname = uri.getQueryParameter("hostpname");
            return hostpname;
        } catch (Exception e) {
            return null;
        }
    }

    protected static String getHostVersionCodeFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            Uri uri = Uri.parse(url);
            String hostversioncode = uri.getQueryParameter("hostversioncode");
            return hostversioncode;
        } catch (Exception e) {
            return null;
        }
    }

    protected static String getViaFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            Uri uri = Uri.parse(url);
            String via = uri.getQueryParameter("via");
            return via;
        } catch (Exception e) {
            return null;
        }
    }

    protected static String getSngAppIdFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            Uri uri = Uri.parse(url);
            String sngAppId = uri.getQueryParameter("sngappid");
            return sngAppId;
        } catch (Exception e) {
            return null;
        }
    }

}

