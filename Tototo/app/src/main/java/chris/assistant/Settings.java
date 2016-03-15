package chris.assistant;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import chris.application.ChrisApp;
import chris.db.table.SettingTable;


/**
 * 软件配置。基础模块，请勿随便修改
 * Created by christiecui on 2016/3/15.
 *
 * 使用
 *  Settings.get().set("xxx", "xxx");
 */
public class Settings {
    private SettingTable setting;

    private static Settings _instance;

    private ConcurrentHashMap<String, String> mData = new ConcurrentHashMap<String, String>();

    private static HashMap<String, String> mSpecialSetting = new HashMap<String, String>();

    private Settings() {
        setting = new SettingTable(ChrisApp.self());
        init();
    }

    public static Settings get() {
        if (_instance == null) {
            _instance = new Settings();
        }
        return _instance;
    }

    private String get(String key, Object defValue) {
        if (mData.containsKey(key)) {
            return mData.get(key);
        } else {
            return String.valueOf(defValue);
        }
    }

    public void init() {
        setting.getAll(mData);
    }

    public int getInt(String uin, String key, int defValue) {
        return Integer.valueOf(get(uin + "_" + key, defValue));
    }

    public int getInt(String key, int defValue) {
        return getInt("", key, defValue);
    }

    public boolean getBoolean(String uin, String key, boolean defValue) {
        return Boolean.parseBoolean(get(uin + "_" + key, defValue));
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getBoolean("", key, defValue);
    }

    public byte getByte(String uin, String key, byte defValue) {
        return Byte.valueOf(get(uin + "_" + key, defValue));
    }

    public byte getByte(String key, byte defValue) {
        return getByte("", key, defValue);
    }

    public long getLong(String uin, String key, long defValue) {
        return Long.valueOf(get(uin + "_" + key, defValue));
    }

    public long getLong(String key, long defValue) {
        return getLong("", key, defValue);
    }

    public String getString(String uin, String key, String defValue) {
        return get(uin+"_"+key, defValue);
    }

    public String getString(String key, String defValue) {
        return getString("", key, defValue);
    }


    public boolean set(String uin, String key, Object value) {
        mData.put(uin + "_" + key, String.valueOf(value));
        boolean updated = setting.update(uin, key, String.valueOf(value));
        if (!updated) {
            return setting.add(uin, key, String.valueOf(value));
        }
        return false;
    }

    public boolean set(String key, Object value) {
        return set("", key, value);
    }
}
