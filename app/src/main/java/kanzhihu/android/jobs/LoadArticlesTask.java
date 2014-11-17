package kanzhihu.android.jobs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.database.ZhihuDatabase;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.models.Article;
import kanzhihu.android.utils.IOUtils;

/**
 * Created by Jiahui.wen on 2014/11/15.
 */
public class LoadArticlesTask extends AsyncTask<Integer, Void, ArrayList<Article>> {

    private WeakReference<Handler> mHandlerRef;

    public LoadArticlesTask(Handler handler) {
        mHandlerRef = new WeakReference<Handler>(handler);
    }

    @Override protected ArrayList<Article> doInBackground(Integer... params) {
        ArrayList<Article> articles = new ArrayList<Article>();

        SQLiteDatabase database = new ZhihuDatabase(App.getAppContext()).getReadableDatabase();
        Cursor cursor =
            database.query(ArticleTable.TABLE_NAME, ArticleTable.ALL_COLUMNS, ArticleTable.CATEGORY_ID + " = ?",
                new String[] { String.valueOf(params[0]) }, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    articles.add(Article.fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            IOUtils.close(cursor);
            IOUtils.close(database);
        }

        return articles;
    }

    @Override protected void onPostExecute(ArrayList<Article> articles) {
        Handler handler = mHandlerRef.get();
        if (handler != null) {
            Message message = Message.obtain(handler, AppConstant.LOAD_ARTICLES_OK);
            Bundle data = new Bundle();
            data.putParcelableArrayList(AppConstant.KEY_ARTICLES, articles);
            message.setData(data);
            message.sendToTarget();
        }
    }
}
