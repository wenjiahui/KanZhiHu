package kanzhihu.android.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.database.ZhihuDatabase;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.database.table.CategoryTable;
import kanzhihu.android.models.Article;
import kanzhihu.android.models.Category;

/**
 * Created by Jiahui.wen on 2014/11/11.
 */
public class PersistUtils {

    public static void store(List<Category> categories) {
        if (categories == null || categories.size() == 0) {
            return;
        }

        SQLiteDatabase database = new ZhihuDatabase(App.getAppContext()).getWritableDatabase();
        database.beginTransaction();
        try {
            for (Category category : categories) {
                Cursor cursor =
                    database.rawQuery(buildCategoryQueryUri(AppConstant.CATEGORY_EXIST_SQL, category), null);
                if (cursor == null || cursor.getCount() == 0) {
                    save(category, database);
                }
                close(cursor);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            AppLogger.e(PersistUtils.class.getSimpleName(), e);
        } finally {
            database.endTransaction();
        }
        //App.getAppContext().getContentResolver().notifyChange();
        close(database);
    }

    /**
     * 创建查询Category是否已经存在的sql语句
     */
    private static String buildCategoryQueryUri(String categoryExistSql, Category category) {
        return String.format(categoryExistSql, category.title);
    }

    private static void save(Category category, SQLiteDatabase database) {
        long id = database.insert(CategoryTable.TABLE_NAME, null, category.getContentValues());
        saveArticles(id, category.articles, database);
    }

    private static void saveArticles(long id, List<Article> articles, SQLiteDatabase database) {
        for (Article article : articles) {
            article.category_id = id;
            database.insert(ArticleTable.TABLE_NAME, null, article.getContentValues());
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            closeable = null;
        }
    }
}
