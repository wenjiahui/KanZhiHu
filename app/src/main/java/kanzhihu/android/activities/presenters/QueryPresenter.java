package kanzhihu.android.activities.presenters;

import android.app.LoaderManager;
import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/20.
 */
public interface QueryPresenter extends LoaderManager.LoaderCallbacks<Cursor> {

    void init();

    void bindEvent();

    void unBindEvent();

    void loadInitData();

    void markArticleChanged(final int position, final Article article, final boolean isChecked);

    SearchView.OnQueryTextListener getQueryTextListener();

    MenuItemCompat.OnActionExpandListener getActionExpandListener();
}
