package kanzhihu.android.activities.presenters.impl;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import kanzhihu.android.AppConstant;
import kanzhihu.android.activities.presenters.ArticlesPresenter;
import kanzhihu.android.activities.views.ArticlesView;
import kanzhihu.android.jobs.LoadArticlesTask;
import kanzhihu.android.models.Article;
import kanzhihu.android.models.Category;
import kanzhihu.android.utils.AssertUtils;

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

    @Override public void onDestory() {
        if (mLoadTask != null && mLoadTask.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadTask.cancel(true);
            mLoadTask = null;
        }
    }
}
