package chris.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import chris.application.ChrisApp;


public class CommonUtil
{
	private static final String KEY_GEN_CONNECTOR = "_  _"; // 生成主键的连接符, 用于包名与版本号和成主键的场景
	protected static int mUniqueId = 0;

	private static int screenDensity = -1;

	private static String uuid;

	public static final String LOG_FILE_MC = "mingchun.txt";
	
	/**
	 * 低分辨率
	 */
	public static final int SCREEN_DENSITY_LOW = 120;

	/**
	 * 普通分辨率
	 */
	public static final int SCREEN_DENSITY_MEDIUM = 160;

	/**
	 * 高分辨率
	 */
	public static final int SCREEN_DENSITY_HIGH = 240;

	public static int currentDeviceWidth = -1;
	public static int currentDeviceHeight = -1;
	public static float currentDensity;

	public synchronized static int getUniqueId()
	{
		return mUniqueId++;
	}

	/**
	 * 通过反射获取屏幕的density
	 * 
	 * @return 屏幕的density
	 */
	public static int getScreenDensity()
	{
		if (screenDensity == -1)
		{
			DisplayMetrics displayMetrics = ChrisApp.self().getResources().getDisplayMetrics();
			try
			{
				screenDensity = DisplayMetrics.class.getField("densityDpi").getInt(displayMetrics);
			}
			catch (Exception e)
			{
				// 没有这个field说明这是1.5的api，目前已知的1.5只有普通分辨率的机器
				screenDensity = SCREEN_DENSITY_MEDIUM;
			}
		}
		return screenDensity;
	}

	/**
	 * int类型转换成byte[]
	 * 
	 * @param num
	 *            int数
	 * @return byte[]
	 */
	public static byte[] intToBytes(int num)
	{

		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++)
		{
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	/**
	 * byte[]转换成int数
	 * 
	 * @param data
	 *            包括int的byte[]
	 * @param offset
	 *            偏移量
	 * @return int数
	 */
	public static int bytesToInt(byte[] data, int offset)
	{
		int num = 0;
		for (int i = offset; i < offset + 4; i++)
		{
			num <<= 8;
			num |= (data[i] & 0xff);
		}
		return num;
	}

	/**
	 * 将字符串类型ip地址转换成int
	 * 
	 * @param ipAddress
	 * @return
	 */
	public static int stringIpToInt(String ipAddress)
	{
		if (ipAddress == null)
		{
			return 0;
		}
		try{
			long[] ip = new long[4];  
			// 先找到IP地址字符串中.的位置  
			int position1 = ipAddress.indexOf(".");  
			int position2 = ipAddress.indexOf(".", position1 + 1);  
			int position3 = ipAddress.indexOf(".", position2 + 1);  
			// 将每个.之间的字符串转换成整型  
			ip[0] = Long.parseLong(ipAddress.substring(0, position1));  
			ip[1] = Long.parseLong(ipAddress.substring(position1 + 1, position2));  
			ip[2] = Long.parseLong(ipAddress.substring(position2 + 1, position3));  
			ip[3] = Long.parseLong(ipAddress.substring(position3 + 1));  
			return (int)((ip[3] << 24) + (ip[2] << 16) + (ip[1] << 8) + ip[0]);
		}catch(Throwable e){
		}
		return 0;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return
	 */
	public static int getScreenWidth()
	{
		if (currentDeviceWidth == -1)
		{
			DisplayMetrics displayMetrics = ChrisApp.self().getResources().getDisplayMetrics();
			currentDeviceWidth = displayMetrics.widthPixels;
		}
		return currentDeviceWidth;
	}
	//create by zxf
	public static float getScreenWidth2() {
		try{
			DisplayMetrics dm = new DisplayMetrics();
			ChrisApp.getCurActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			if(dm!=null){
				return dm.widthPixels;
			}
			WindowManager wm = (WindowManager) ChrisApp.self().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			if (android.os.Build.VERSION.SDK_INT >= 13){
				Point size = new Point();
				display.getSize(size);
				return size.x;
			}else{
				return display.getWidth();
			}
		}catch (NullPointerException e){
			return getScreenWidth();
		}
	}
	/**
	 * 获取屏幕高度
	 * 
	 * @return
	 */
	public static int getScreenHeight()
	{
		if (currentDeviceHeight == -1)
		{
			currentDeviceHeight = ChrisApp.self().getResources().getDisplayMetrics().heightPixels;
		}
		return currentDeviceHeight;
	}

	/**
	 * 获取sdk version信息
	 * 
	 * @return int 版本号
	 */
	public static int getAndroidSDKVersion()
	{
		int version = 0;
		try
		{
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		}
		catch (NumberFormatException e)
		{
		}
		return version;
	}

	/**
	 * memory信息
	 * 
	 * @return String
	 */
	public static String printMemoryInfo()
	{
		StringBuffer sb = new StringBuffer();
		Runtime rt = Runtime.getRuntime();
		sb.append("Max:").append(rt.maxMemory());
		sb.append("|Free:").append(rt.freeMemory());
		sb.append("|Total:").append(rt.totalMemory());
		return sb.toString();
	}

	/**
	 * 获取机器唯一标志码，与sim卡无关
	 * 
	 * @param context
	 * @return
	 */
	public static String getUUID(Context context)
	{
		if (TextUtils.isEmpty(uuid))
		{
			uuid = getUUID(false, context);
		}
		return uuid;
	}

	/**
	 * 获取机器唯一标志码
	 * 
	 * @param context
	 * @param sim
	 *            是否跟sim卡相关
	 * @return
	 */
	public static String getUUID(boolean sim, Context context)
	{
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + (sim ? tm.getSimSerialNumber() : "sim");
		androidId = "" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}

	/**
	 * 处理版本号
	 * 
	 * @param version
	 *            版本信息
	 * @return String
	 */
	public static String dealVersion(int version)
	{
		String value = "0000";
		value = version + "";
		switch (value.length())
		{
		case 1:
			value = "000" + value;
			break;
		case 2:
			value = "00" + value;
			break;
		case 3:
			value = "0" + value;
			break;
		default:

			break;
		}
		return value;
	}

	/**
	 * 将字符串用base64编码
	 * 
	 * @param str
	 * @return
	 */
	public static final String uriEncode(String str)
	{
		return Uri.encode(str);
	}

	/**
	 * 将base64字符串解码
	 * 
	 * @param str
	 * @return
	 */
	public static final String uriDecode(String str)
	{
		return Uri.decode(str);
	}

	/***
	 * 当前machine是否支持多点触摸
	 * 
	 * @return
	 */
	public static boolean isSupportMultiTouch()
	{
		boolean condition1 = false;
		boolean condition2 = false;
		// Not checking for getX(int), getY(int) etc 'cause I'm lazy
		Method methods[] = MotionEvent.class.getDeclaredMethods();
		for (Method m : methods)
		{
			if (m.getName().equals("getPointerCount"))
				condition1 = true;
			if (m.getName().equals("getPointerId"))
				condition2 = true;
		}
		if (getAndroidSDKVersion() >= 7 || (condition1 && condition2))
			return true; // 支持多点触摸
		else
			return false;
	}

	/**
	 * 获取中图图片地址 高分辨率拉460的，普通分辨率拉320的
	 * 
	 * @param url
	 *            图片地址(不包含/460,/320...)
	 * @return
	 */
	public static String getMiddleImage(String url)
	{
		if (TextUtils.isEmpty(url))
		{
			return "";
		}
		if (getScreenDensity() >= SCREEN_DENSITY_HIGH)
		{
			return url + "/460";
		}
		else
		{
			return url + "/320";
		}
	}

	public static String getOrginalImageUrl(String url)
	{
		return url + "/2000";
	}

	// 策略调整， 中图都为460
	public static String getMiddleImageSize()
	{
		return "/460";
	}

	public static String getOrginalImageSize()
	{
		return "/2000";
	}

	public static String getSmallPic120(String url)
	{
		return url + "/120";
	}

	public static String getMiddeleImgeUrl(String orginaUrl)
	{
		return orginaUrl + getMiddleImageSize();
	}

	/**
	 * 解析字符串为整形数, 转换出错默认返回0;
	 * 
	 * @param str
	 * @return
	 */
	public static int parseInt(String str)
	{
		return parseInt(str, 0);
	}

	/**
	 * 解析字符串为整数, 转换出错返回指定默认值
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(String str, int defaultValue)
	{
		int value = defaultValue;
		try
		{
			value = Integer.parseInt(str);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 显示软键盘
	 * 
	 * @param context
	 * @param editText
	 */
	public static void showSoftKeyBroad(Context context, EditText editText)
	{
		InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// only will trigger it if no physical keyboard is open
		mgr.showSoftInput(editText, mgr.SHOW_IMPLICIT);
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 * @param editText
	 */
	public static void hideSoftKeyBroad(Context context, EditText editText)
	{
		InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * @deprecated use ShellUtils instead
	 * @param args
	 * @return
	 */
	public static String exec(String[] args)
	{
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1)
			{
				baos.write(read);
			}
			baos.write('n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1)
			{
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (errIs != null)
				{
					errIs.close();
				}
				if (inIs != null)
				{
					inIs.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (process != null)
			{
				process.destroy();
			}
		}
		return result;
	}
	
	/**
	 * 根据包名与版本号算一个key
	 * 
	 * @param packageName
	 * @param versionCode
	 * @return
	 */
	public static String getKey(String packageName, int versionCode) {
		StringBuilder builder = new StringBuilder();
		builder.append(packageName);
		builder.append(KEY_GEN_CONNECTOR);
		builder.append(Integer.toString(versionCode));
		return builder.toString();
	}
	
	/**
	 * 打开一个app
	 * @param packageName
	 * @param data
	 * @return
	 */
	public static boolean lanuchApp(String packageName, Bundle data){
		try{
			Intent resolveIntent = ChrisApp.self().getPackageManager().getLaunchIntentForPackage(packageName);
			if(resolveIntent == null) {
			    PackageInfo p = ChrisApp.self().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
                if(p != null){
                    resolveIntent = new Intent(packageName);
                }
			}
			if(resolveIntent != null){
				if(data != null){
					resolveIntent.putExtras(data);
				}
				resolveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ChrisApp.self().startActivity(resolveIntent);
				return true;
			}
		}catch(Exception e){
		}
		return false;
	}

	/**
	 * 打开应用内activity
	 *
	 * @param activityClassName
	 * @param data
	 * @return
	 */
	public static boolean startActivity(String activityClassName, Bundle data) {
		try {
			if (TextUtils.isEmpty(activityClassName)) {
				return false;
			}
			Intent resolveIntent = new Intent(ChrisApp.self(), Class.forName(activityClassName));

			if (data != null) {
				resolveIntent.putExtras(data);
			}
			resolveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ChrisApp.self().startActivity(resolveIntent);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 判断一个int数字的第n位是否置位了
	 * @param src int数字
	 * @param bitNum 第几位
	 * @return
	 */
	public static boolean isBitSetted(int src, int bitNum) {
		return ((src >> bitNum)&1) != 0;
	}



}
