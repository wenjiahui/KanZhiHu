package kanzhihu.android.activities.views;

import android.app.Activity;
import android.app.LoaderManager;
import android.database.Cursor;
import android.view.MenuItem;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/20.
 */
public interface QueryView {

    void onSearchViewClosed(MenuItem menuItem);

    LoaderManager getLoaderManager();

    Activity getContext();

    void swapCursor(Cursor cursor);

    boolean getVisiable();

    void showArticle(int position);

    void onQueryTextChange(String newText);

    void articleChanged(int position);

    Article getArticle(int position);
}
