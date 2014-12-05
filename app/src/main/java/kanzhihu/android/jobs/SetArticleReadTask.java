package kanzhihu.android.jobs;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import de.greenrobot.event.EventBus;
import kanzhihu.android.App;
import kanzhihu.android.database.ZhihuProvider;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.events.ArticleReadEvent;
import kanzhihu.android.models.Article;
import kanzhihu.android.utils.Cache;

/**
 * Created by Jiahui.wen on 2014/12/4.
 */
public class SetArticleReadTask extends SimpleBackgroundTask<Boolean> {

    private Article mArticle;

    public SetArticleReadTask(Activity activity, Article article) {
        super(activity);
        mArticle = article;
    }

    @Override protected Boolean onRun() {
        ContentValues values = new ContentValues(1);
        values.put(ArticleTable.READ, 1);
        int count = App.getAppContext()
            .getContentResolver()
            .update(Uri.parse(ZhihuProvider.ARTICLE_CONTENT_URI + "/" + mArticle.id), values, null, null);
        return count > 0;
    }

    @Override protected void onSuccess(Boolean result) {
        if (result) {
            Cache.remove(mArticle);
            mArticle.read = 1;
            EventBus.getDefault().post(new ArticleReadEvent(mArticle));
        }
    }
}
