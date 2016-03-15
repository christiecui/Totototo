package chris.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import chris.db.table.SettingTable;


/**
 * Created by christiecui on 15/6/24.
 * modified by christiecui on 2016/3/15
 */
public class ChrisDbHelper extends SqliteHelper {
    public ChrisDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, version);
    }

    public static synchronized SqliteHelper get(Context context) {
        if (instance == null) {
            instance = new ChrisDbHelper(context, DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    /**
     * ver:1
     */
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mobile_ast.db";

    private static SqliteHelper instance;

    /** 后续有表的增加或减少，这里进行维护 */
    private static final Class<?>[] TABLESS ={
            SettingTable.class,
    };

    @Override
    public Class<?>[] getTables() {
        return TABLESS;
    }

    @Override
    public int getDBVersion() {
        return 0;
    }


}
