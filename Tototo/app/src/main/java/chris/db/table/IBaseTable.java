package chris.db.table;

import android.database.sqlite.SQLiteDatabase;

import chris.db.helper.SqliteHelper;


/**
 * Created by cuiqi on 15/6/11.
 * 数据库基类
 */
public interface IBaseTable {
    /**
     * 表的版本号,在表结构变化时，务必升级该字段
     *
     * @return
     */
    public int tableVersion();

    /**
     * 表名
     * @return
     */
    public String tableName();

    /**
     * 创建表的sql语句
     *
     * @return
     */
    public String createTableSQL();


    /**
     * 升级的alter语句
     * 增量升级, oldVersion 比newVersion 小1的传到相关的表中
     * 如 1-2 2-3 3-4 4-5...
     * @return
     */
    public String[] getAlterSQL(int oldVersion, int newVersion);

    /**
     * 升级的数据迁移
     * 增量升级, oldVersion 比newVersion 小1的传到相关的表中
     * 如 1-2 2-3 3-4 4-5...
     * @return
     */
    public void dataMovement(int oldVersion, int newVersion, SQLiteDatabase db);

    /**
     * 返回数据库管理的Helper, 一个Helper的子类代表一个数据库
     * @return
     */
    public SqliteHelper getHelper();
}
