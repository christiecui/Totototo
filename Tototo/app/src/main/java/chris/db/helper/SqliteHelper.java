package chris.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.util.Log;

import chris.db.table.IBaseTable;


/**
 * Created by cuiqi on 15/6/11.
 * modified by christiecui on 2016/3/15
 * 各个db数据库的公共父类，提供线程安全的调用
 * 实现这个类就可以创建一个新的.db，在子类中创建表并做版本管理
 * 数据库下面的只有一个张表的版本升级了, 数据库也要升级
 */
public abstract class SqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = SqliteHelper.class.getSimpleName();

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public abstract Class<?>[] getTables();

    public abstract int getDBVersion();

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db = super.getWritableDatabase();
        Log.v(TAG, "currentThread:" + Thread.currentThread().getId() + ", dbLockedByCurrentThread:" + db.isDbLockedByCurrentThread()
                + ", lockedByOtherThread:" + db.isDbLockedByOtherThreads());
        while (db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads()) {
            SystemClock.sleep(10l);
        }
        return db;
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        Log.v("cuiqi", "getWritableDatabase begin");
        final SQLiteDatabase db = super.getReadableDatabase();
        while (db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads()) {
            SystemClock.sleep(10l);
        }
        return db;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        int version = db.getVersion();
        if (version == 0) {
        } else if (version < getDBVersion()) {
            this.onUpgrade(db, version, getDBVersion());
        } else if (version > getDBVersion()) {
            this.onDowngrade(db, version, getDBVersion());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("cuiqi", "createTable begin");
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 一步步往上升级
        for (int i = oldVersion; i < newVersion; i++) {
            for (Class<?> baseTable : getTables()) {
                try {
                    IBaseTable table = (IBaseTable) baseTable.newInstance();
                    table.dataMovement(i, i+1,db);
                    String[] sql = table.getAlterSQL(i, i + 1);
                    if (sql != null ) {
                        for (String aSql : sql) {
                            db.execSQL(aSql);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(db);
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        for (Class<?> baseTable : getTables()) {
            String sql = null;
            try {
                IBaseTable table = (IBaseTable) baseTable.newInstance();
                sql = table.createTableSQL();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (sql != null && sql.length() > 0) {
                db.execSQL(sql);
            }
        }
    }

    private void deleteTable(SQLiteDatabase db) {
        for (Class<?> baseTable : getTables()) {
            try {
                IBaseTable table = (IBaseTable)baseTable.newInstance();
                db.delete(table.tableName(), null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
