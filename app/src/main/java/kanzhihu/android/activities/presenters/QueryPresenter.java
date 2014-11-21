package kanzhihu.android.activities.presenters;

import android.app.LoaderManager;
import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;

/**
 * Created by Jiahui.wen on 2014/11/20.
 */
public interface QueryPresenter extends LoaderManager.LoaderCallbacks<Cursor> {

    void init();

    void bindEvent();

    void unBindEvent();

    void loadInitData();

    SearchView.OnQueryTextListener getQueryTextListener();

    MenuItemCompat.OnActionExpandListener getActionExpandListener();
}
