package kanzhihu.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.database.table.CategoryTable;

public class ZhihuDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "zhihu_database.db";
    private static final int DATABASE_VERSION = 2;

    public ZhihuDatabase(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        db.execSQL(ArticleTable.SQL_CREATE);

        db.execSQL(CategoryTable.SQL_CREATE);
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        upgrade(db, oldVersion, newVersion);
    }

    private final void dropTablesAndCreate(final SQLiteDatabase db) {
        db.execSQL(ArticleTable.SQL_DROP);

        db.execSQL(CategoryTable.SQL_DROP);

        onCreate(db);
    }

    private void upgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        dropTablesAndCreate(db);
    }
}