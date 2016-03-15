package chris.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import chris.application.ChrisApp;
import chris.db.helper.ChrisDbHelper;
import chris.db.helper.SqliteHelper;


/**
 * Created by cuiqi on 15/6/11.
 */
public class SettingTable implements IBaseTable {
    public static final String TABLE_NAME = "setting";

    public static final String SQL_CREATE = "CREATE TABLE if not exists setting ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "uin TEXT," + "key TEXT,"
            + "value TEXT"
            + ");";

    public SettingTable() {

    }
    public SettingTable(ChrisApp self) {
    }

    public boolean update(String uin, String key, String value) {
        final SQLiteDatabase db = getHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("value", value);
        return db.update("setting", values, "uin=? and key=?", new String[] { uin, key }) > 0;

    }

    public boolean update(String key, String value) {
        return update("", key, value);
    }

    public boolean add(String uin, String key, String value) {
        final SQLiteDatabase db = getHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uin", uin);
        values.put("key", key);
        values.put("value", value);
        return db.insert("setting", null, values) > 0;
    }

    public boolean add(String key, String value) {
        return add("", key, value);
    }

    public String get(String uin, String key) {
        final SQLiteDatabase db = getHelper().getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("setting", new String[] { "value" }, "uin=? and key=?", new String[] { uin, key }, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    public String get(String key) {
        return get("", key);
    }

    public void getAll(Map<String, String> map) {
        final SQLiteDatabase db = getHelper().getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("setting", new String[] { "uin, key, value" }, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String uin = cursor.getString(0);
                    String key = cursor.getString(1);
                    String value = cursor.getString(2);
                    map.put(uin + "_" + key, value);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public int tableVersion() {
        return 1;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String createTableSQL() {
        return SQL_CREATE;
    }

    @Override
    public String[] getAlterSQL(int oldVersion, int newVersion) {
        return null;
    }

    @Override
    public void dataMovement(int oldVersion, int newVersion, SQLiteDatabase db) {

    }


    @Override
    public SqliteHelper getHelper() {
        return ChrisDbHelper.get(ChrisApp.self());
    }
}
