package kanzhihu.android.activities.presenters.impl;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import com.cocosw.undobar.UndoBarController;
import de.greenrobot.event.EventBus;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.BrowseActivity;
import kanzhihu.android.activities.presenters.QueryPresenter;
import kanzhihu.android.activities.views.QueryView;
import kanzhihu.android.database.ZhihuProvider;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.events.ImageModeChangeEvent;
import kanzhihu.android.events.ListitemClickEvent;
import kanzhihu.android.events.MarkChangeEvent;
import kanzhihu.android.events.ShareArticleEvent;
import kanzhihu.android.events.ShareMenuDismissEvent;
import kanzhihu.android.events.ViewAuthorEvent;
import kanzhihu.android.jobs.SetArticleReadTask;
import kanzhihu.android.jobs.SimpleBackgroundTask;
import kanzhihu.android.models.Article;
import kanzhihu.android.utils.AssertUtils;
import kanzhihu.android.utils.Cache;
import kanzhihu.android.utils.PreferenceUtils;
import kanzhihu.android.utils.ToastUtils;

/**
 * Created by Jiahui.wen on 2014/11/20.
 */
public class QueryPresenterImpl implements QueryPresenter {

    private QueryView mView;

    String mCurFilter;

    private boolean backPressed = false;

    private SearchView.OnQueryTextListener mQueryTextListener;

    private MenuItemCompat.OnActionExpandListener mActionExpandListener;

    private boolean bMarkView;

    public QueryPresenterImpl(QueryView mView, boolean bMarkView) {
        this.mView = AssertUtils.requireNonNull(mView, QueryView.class.getSimpleName() + " must not null");
        this.bMarkView = bMarkView;

        init();
    }

    @Override public void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override public void onEventMainThread(ImageModeChangeEvent event) {

    }

    @Override public void onEventMainThread(ListitemClickEvent event) {
        if (mView.getVisiable()) {
            Article article = mView.getArticle(event.position);
            if (article == null) {
                return;
            }
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

    @Override public void onEventMainThread(MarkChangeEvent event) {
        if (!mView.getVisiable()) {
            return;
        }
        Article article = mView.getArticle(event.position);
        if (article != null) {
            markArticleChanged(event.position, article, event.isChecked);
        }
    }

    @Override public void onEventMainThread(ShareMenuDismissEvent event) {
        if (!mView.getVisiable()) {
            return;
        }
        mView.closeShareView();
    }

    @Override public void onEventMainThread(ShareArticleEvent event) {
        if (!mView.getVisiable()) {
            return;
        }
        this.onShareArticle(event.position);
    }

    @Override public void onEventMainThread(ViewAuthorEvent event) {
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
                    if (bMarkView) {
                        //如果是收藏界面，需要显示撤销模式
                        mView.showUndo(article);
                    }
                } else {
                    ToastUtils.showShort(R.string.mark_fail);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override public void loadInitData() {
        mView.getLoaderManager().initLoader(AppConstant.SEARCH_LOADER_ID, null, QueryPresenterImpl.this);
    }

    @Override public SearchView.OnQueryTextListener getQueryTextListener() {
        if (mQueryTextListener == null) {
            mQueryTextListener = new SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextChange(String newText) {
                    String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
                    if (mCurFilter == null && newFilter == null) {
                        return true;
                    }
                    if (mCurFilter != null && mCurFilter.equals(newFilter)) {
                        return true;
                    }
                    if (newFilter != null) {
                        newFilter = newFilter.trim();
                    }
                    mCurFilter = newFilter;
                    //通知adapter，查询关键字已改变
                    mView.onQueryTextChange(mCurFilter);
                    mView.getLoaderManager().restartLoader(AppConstant.SEARCH_LOADER_ID, null, QueryPresenterImpl.this);
                    return true;
                }

                @Override public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            };
        }
        return mQueryTextListener;
    }

    @Override public MenuItemCompat.OnActionExpandListener getActionExpandListener() {
        if (mActionExpandListener == null) {
            mActionExpandListener = new MenuItemCompat.OnActionExpandListener() {
                @Override public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    return true;
                }

                @Override public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    if (!backPressed) {
                        backPressed = true;
                        mView.onSearchViewClosed(menuItem);
                    }
                    return true;
                }
            };
        }
        return mActionExpandListener;
    }

    @Override public UndoBarController.UndoListener getUndoListener() {
        return this;
    }

    @Override public void onShareArticle(int position) {
        Article article = mView.getArticle(position);
        mView.createShareView(article);
    }

    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;
        if (mCurFilter != null && !mCurFilter.trim().equals("")) {
            if (bMarkView) {
                selection = AppConstant.SEARCH_SQL_SELECTION_FOR_MARK;
            } else {
                selection = AppConstant.SEARCH_SQL_SELECTION;
            }
            String selectionArg = "%" + mCurFilter + "%";
            selectionArgs = new String[] { selectionArg, selectionArg };
        } else {
            if (bMarkView) {
                selection = AppConstant.SEARCH_SQL_MARK_ONLY;
            }
        }
        String sortOrder = ArticleTable.CATEGORY_ID + " desc, " + ArticleTable.AGREE_COUNT + " desc";

        return new CursorLoader(mView.getContext(), ZhihuProvider.ARTICLE_CONTENT_URI, null, selection, selectionArgs,
            sortOrder);
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mView.swapCursor(data);
    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {
        mView.swapCursor(null);
    }

    @Override public void onUndo(@Nullable Parcelable parcelable) {
        final Article article = (Article) parcelable;
        new SimpleBackgroundTask<Boolean>(mView.getContext()) {
            @Override protected Boolean onRun() {
                ContentValues values = new ContentValues(1);
                values.put(ArticleTable.MARKED, 1);
                int count = App.getAppContext()
                    .getContentResolver()
                    .update(Uri.parse(ZhihuProvider.ARTICLE_CONTENT_URI + "/" + article.id), values, null, null);
                return count > 0;
            }

            @Override protected void onSuccess(Boolean result) {
                if (!result) {
                    ToastUtils.showShort(R.string.mark_revert_fail);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override public void onDestory() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
