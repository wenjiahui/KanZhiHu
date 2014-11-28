package kanzhihu.android.managers;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import de.greenrobot.event.EventBus;
import kanzhihu.android.App;
import kanzhihu.android.R;
import kanzhihu.android.database.ZhihuProvider;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.events.MarkChangeEvent;
import kanzhihu.android.models.Article;
import kanzhihu.android.utils.Cache;
import kanzhihu.android.utils.ToastUtils;

/**
 * Created by Jiahui.wen on 2014/11/25.
 */
public class MarkManager {

    public static void handle(Article article) {
        new MarkTask(article).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    static class MarkTask extends AsyncTask<Void, Void, Boolean> {

        private Article article;

        MarkTask(Article article) {
            this.article = article;
        }

        @Override protected Boolean doInBackground(Void... params) {
            ContentValues values = new ContentValues(1);
            values.put(ArticleTable.MARKED, article.marked > 0 ? 0 : 1);
            int count = App.getAppContext()
                .getContentResolver()
                .update(Uri.parse(ZhihuProvider.ARTICLE_CONTENT_URI + "/" + article.id), values, null, null);
            return count > 0;
        }

        @Override protected void onPostExecute(Boolean result) {
            if (result) {
                Cache.remove(article);
                article.marked = article.marked > 0 ? 0 : 1;
                EventBus.getDefault().post(new MarkChangeEvent());
            } else {
                ToastUtils.showShort(R.string.mark_fail);
            }
        }
    }
}
