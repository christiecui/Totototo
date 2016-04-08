package chris.link;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;

import chris.ui.TTFragmentActivity;

/**
 * 跳转链接支持能力。这个类基本上只是针对BaseIntentUtils的代理功能。如果有涉及到跳转能力的支持的修改，请修改BaseIntentUtils类。
 * 这点很重要
 * Created by cuiqi on 15/7/7.
 */
public class IntentUtils {

    private static final String TAG = "IntentUtils";

    public static final void forward(Context context, String url, Bundle extra) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        forward(context, uri, extra);
    }

    public static final void forward(Context context, String url) {
        forward(context, url, null);
    }

    public static final void forward(Context context, Uri uri, Bundle extra) {
        if(uri==null)
        {
            return;
        }
        if (context != null && context instanceof TTFragmentActivity && extra != null )
        {
            //todo 也许后续会加上报
//            extra.putInt(BaseActivity.PARAMS_PRE_ACTIVITY_TAG_NAME, ((BaseActivity)context).getActivityPageId());
        }
        String scheme = uri.getScheme();
        if(scheme == null){
            return;
        }
        if (scheme.equals(BaseIntentUtils.SCHEME_MAST)) {
            onMast(context, uri, extra);
        } else if (scheme.equals(BaseIntentUtils.SCHEME_TPMAST)) {
            onTpMast(context, uri, extra);
        } else if (scheme.equals(BaseIntentUtils.SCHEME_MARKET)) {
            onMarket(context, uri);
        } else {
            onHttp(context, uri);
        }
    }

    public static final void forward(Context context, Uri uri) {
        if (uri == null) {
            return;
        }

        forward(context, uri, null);
    }

    public static final void innerForward(Context context, String url, Bundle extra) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if(!url.contains("?")) {
            url = url + BaseIntentUtils.SELF_LINK_F;
        }else {
            url = url + BaseIntentUtils.SELF_LINK;
        }
        Uri uri = Uri.parse(url);
        forward(context, uri, extra);
    }

    public static final void innerForward(Context context, String url) {
        innerForward(context, url, null);
    }

    protected static boolean onMastExtra(Context context, Uri uri, Bundle extras){
        return false;
    }

    private static boolean onMast(Context context, Uri uri, Bundle extras) {
        return BaseIntentUtils.onMast(context, uri, extras);
    }

    private static boolean onHttp(Context context, Uri uri) {
        return BaseIntentUtils.onHttp(context, uri);
    }

    private static boolean onMarket(Context context, Uri uri) {
        return BaseIntentUtils.onMarket(context, uri);
    }

    private static boolean onTpMast(Context context, Uri uri, Bundle extras) {
        return BaseIntentUtils.onTpMast(context, uri, extras);
    }

    public static Uri buildUri(String scheme, String host, HashMap<String, String> map) {
        return BaseIntentUtils.buildUri(scheme, host, map);
    }

    /**
     * Channel : 当只有一个参数时调用
     * @param scheme
     * @param host
     * @param paramKey
     * @param paramValue
     * @return
     */
    public static Uri buildUri(String scheme, String host, String paramKey, String paramValue) {
        return BaseIntentUtils.buildUri(scheme, host, paramKey, paramValue);
    }

    /**
     * 是否有activity可以处理
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean hasAbility(Context context, Intent intent) {
        return BaseIntentUtils.hasAbility(context, intent);
    }

    /**
     * 从url中获取version code
     *
     * @param url
     * @return
     */
    public static int getVersionCodeFromUrl(String url) {
        return BaseIntentUtils.getVersionCodeFromUrl(url);
    }

    public static String getTaskIdFromUrl(String url) {
        return BaseIntentUtils.getTaskIdFromUrl(url);
    }

    public static String getActionTypeFromUrl(String url) {
        return BaseIntentUtils.getActionTypeFromUrl(url);
    }

    public static String getHostPackageNameFromUrl(String url) {
        return BaseIntentUtils.getHostPackageNameFromUrl(url);
    }

    public static String getHostVersionCodeFromUrl(String url) {
        return BaseIntentUtils.getHostVersionCodeFromUrl(url);
    }

    public static String getViaFromUrl(String url) {
        return BaseIntentUtils.getViaFromUrl(url);
    }

    public static String getSngAppIdFromUrl(String url) {
        return BaseIntentUtils.getSngAppIdFromUrl(url);
    }

}
