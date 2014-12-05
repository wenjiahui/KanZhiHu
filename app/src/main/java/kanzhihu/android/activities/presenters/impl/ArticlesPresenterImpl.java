package kanzhihu.android.activities.presenters.impl;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.BrowseActivity;
import kanzhihu.android.activities.presenters.ArticlesPresenter;
import kanzhihu.android.activities.views.ArticlesView;
import kanzhihu.android.database.ZhihuProvider;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.events.ArticleReadEvent;
import kanzhihu.android.events.BrowseMarkChangedEvent;
import kanzhihu.android.events.MarkChangeEvent;
import kanzhihu.android.events.ShareArticleEvent;
import kanzhihu.android.events.ShareMenuDismissEvent;
import kanzhihu.android.events.ViewAuthorEvent;
import kanzhihu.android.jobs.LoadArticlesTask;
import kanzhihu.android.jobs.SetArticleReadTask;
import kanzhihu.android.jobs.SimpleBackgroundTask;
import kanzhihu.android.models.Article;
import kanzhihu.android.models.Category;
import kanzhihu.android.utils.AssertUtils;
import kanzhihu.android.utils.Cache;
import kanzhihu.android.utils.PreferenceUtils;
import kanzhihu.android.utils.ToastUtils;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public class ArticlesPresenterImpl implements ArticlesPresenter, Handler.Callback {

    private ArticlesView mView;

    private Handler mHandler;

    private ArrayList<Article> articles = new ArrayList<Article>();

    private LoadArticlesTask mLoadTask;

    private Category mCategory;

    public ArticlesPresenterImpl(ArticlesView articlesView, Category category) {
        this.mView = AssertUtils.requireNonNull(articlesView, "ArticlesView must not null");
        mHandler = new Handler(this);
        mCategory = category;

        EventBus.getDefault().register(this);
    }

    @Override public void loadArticles() {
        AssertUtils.requireNonNull(mCategory, "category must not null");
        mLoadTask = new LoadArticlesTask(mHandler);
        mLoadTask.execute(mCategory._id);
    }

    @Override public boolean handleMessage(Message msg) {
        if (msg.what == AppConstant.LOAD_ARTICLES_OK) {
            articles = msg.getData().getParcelableArrayList(AppConstant.KEY_ARTICLES);
            mView.onLoadArticlesFinished(articles);
            return true;
        }
        return false;
    }

    public void onEventMainThread(MarkChangeEvent event) {
        if (!mView.getVisiable()) {
            return;
        }
        Article article = mView.getArticle(event.position);
        this.markArticleChanged(event.position, article, event.isChecked);
    }

    public void onEventMainThread(BrowseMarkChangedEvent event) {
        int position = articles.indexOf(event.article);
        if (position != -1) {
            articles.set(position, event.article);
            mView.articleChanged(position);
        }
    }

    public void onEventMainThread(ShareMenuDismissEvent event) {
        if (!mView.getVisiable()) {
            return;
        }
        mView.closeShareView();
    }

    public void onEventMainThread(ShareArticleEvent event) {
        if (!mView.getVisiable()) {
            return;
        }
        this.onShareArticle(event.position);
    }

    public void onEventMainThread(ArticleReadEvent event) {
        int position = articles.indexOf(event.article);
        if (position != -1) {
            mView.articleChanged(position);
        }
    }

    public void onEventMainThread(ViewAuthorEvent event) {
        if (!mView.getVisiable()) {
            return;
        }
        Article article = mView.getArticle(event.position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.writerLink));
        mView.getContext().startActivity(intent);
    }

    @Override public void markArticleChanged(final int position, final Article article, final boolean isChecked) {
        new SimpleBackgroundTask<Boolean>(mView.getContext()) {
            @Override protected Boolean onRun() {
                ContentValues values = new ContentValues(1);
                values.put(ArticleTable.MARKED, isChecked ? 1 : 0);
                int count = App.getAppContext()
                    .getContentResolver()
                    .update(Uri.parse(ZhihuProvider.ARTICLE_CONTENT_URI + "/" + article.id), values, null, null);
                return count > 0;
            }

            @Override protected void onSuccess(Boolean result) {
                if (result) {
                    Cache.remove(article);
                    article.marked = isChecked ? 1 : 0;
                    mView.articleChanged(position);
                } else {
                    ToastUtils.showShort(R.string.mark_fail);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override public void onShareArticle(int position) {
        mView.createShareView(mView.getArticle(position));
    }

    @Override public void onDestory() {
        if (mLoadTask != null && mLoadTask.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadTask.cancel(true);
            mLoadTask = null;
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override public void readArticle(final Article article) {
        if (PreferenceUtils.external_open()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.link));
            mView.getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(mView.getContext(), BrowseActivity.class);
            intent.putExtra(AppConstant.KEY_ARTICLE, article);
            mView.getContext().startActivity(intent);
        }
        new SetArticleReadTask(mView.getContext(), article).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
