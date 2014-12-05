package kanzhihu.android.activities.presenters.impl;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import de.greenrobot.event.EventBus;
import kanzhihu.android.App;
import kanzhihu.android.R;
import kanzhihu.android.activities.presenters.BrowsePresenter;
import kanzhihu.android.activities.views.BrowseView;
import kanzhihu.android.database.ZhihuProvider;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.events.BrowseMarkChangedEvent;
import kanzhihu.android.events.ImageModeChangeEvent;
import kanzhihu.android.jobs.SimpleBackgroundTask;
import kanzhihu.android.models.Article;
import kanzhihu.android.utils.AssertUtils;
import kanzhihu.android.utils.Cache;
import kanzhihu.android.utils.ToastUtils;

/**
 * Created by Jiahui.wen on 2014/11/23.
 */
public class BrowsePresenterImpl implements BrowsePresenter {

    private Article mArticle;
    private BrowseView mView;

    public BrowsePresenterImpl(BrowseView view, Article article) {
        mView = AssertUtils.requireNonNull(view);
        this.mArticle = article;
    }

    @Override public void init() {

    }

    @Override public void onEventMainThread(ImageModeChangeEvent event) {

    }

    @Override public void browseArticle(Article article) {
        mArticle = article;
        mView.setMarkStatue(mArticle.marked > 0);
    }

    @Override public void onMarkChanged() {
        new SimpleBackgroundTask<Boolean>(mView.getContext()) {
            @Override protected Boolean onRun() {
                ContentValues values = new ContentValues(1);
                values.put(ArticleTable.MARKED, mArticle.marked == 0 ? 1 : 0);
                int count = App.getAppContext()
                    .getContentResolver()
                    .update(Uri.parse(ZhihuProvider.ARTICLE_CONTENT_URI + "/" + mArticle.id), values, null, null);
                return count > 0;
            }

            @Override protected void onSuccess(Boolean result) {
                if (result) {
                    Cache.remove(mArticle);
                    mArticle.marked = mArticle.marked == 0 ? 1 : 0;
                    mView.setMarkStatue(mArticle.marked > 0);

                    EventBus.getDefault().post(new BrowseMarkChangedEvent(mArticle));
                } else {
                    ToastUtils.showShort(R.string.mark_fail);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override public void onDestory() {

    }
}
