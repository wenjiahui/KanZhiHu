package kanzhihu.android.jobs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import javax.inject.Inject;
import kanzhihu.android.App;
import kanzhihu.android.database.ZhihuDatabase;
import kanzhihu.android.database.ZhihuProvider;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.database.table.CategoryTable;
import kanzhihu.android.modules.Injector;
import kanzhihu.android.utils.IOUtils;
import kanzhihu.android.utils.Preferences;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public class DeleteOldArticlesJob extends Job {

    @Inject Preferences mPreference;

    public DeleteOldArticlesJob() {
        super(new Params(Priority.LOW).delayInMs(5000));

        Injector.inject(this);
    }

    @Override public void onAdded() {

    }

    @Override public void onRun() throws Throwable {
        SQLiteDatabase database = new ZhihuDatabase(App.getAppContext()).getReadableDatabase();
        Cursor cursor =
            database.query(CategoryTable.TABLE_NAME, new String[] { CategoryTable._ID }, null, null, null, null,
                CategoryTable.PUBLISH_DATE + " ASC");
        database.beginTransaction();
        try {
            if (cursor != null && cursor.getCount() > mPreference.getSaveDays()) {
                cursor.moveToFirst();
                //乘以3是因为每天有3个category
                int days = cursor.getCount() - mPreference.getSaveDays() * 3;
                boolean avaliable = true;
                while (--days > 0 && avaliable) {
                    int index = cursor.getColumnIndex(CategoryTable._ID);
                    int _id = cursor.getInt(index);
                    //delete relative articles
                    database.delete(ArticleTable.TABLE_NAME,
                        ArticleTable.CATEGORY_ID + " = ? and " + ArticleTable.MARKED + " = 0",
                        new String[] { String.valueOf(_id) });
                    //delete category
                    database.delete(CategoryTable.TABLE_NAME, CategoryTable._ID + " = ?",
                        new String[] { String.valueOf(_id) });

                    avaliable = cursor.moveToNext();
                }
            }
            database.setTransactionSuccessful();
            App.getAppContext().getContentResolver().notifyChange(ZhihuProvider.CATEGORY_CONTENT_URI, null);
            App.getAppContext().getContentResolver().notifyChange(ZhihuProvider.ARTICLE_CONTENT_URI, null);
        } finally {
            database.endTransaction();

            IOUtils.close(cursor);
            IOUtils.close(database);
        }
    }

    @Override protected void onCancel() {

    }

    @Override protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
