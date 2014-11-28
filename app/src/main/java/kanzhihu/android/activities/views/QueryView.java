package kanzhihu.android.activities.views;

import android.app.LoaderManager;
import android.database.Cursor;
import android.view.MenuItem;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/20.
 */
public interface QueryView extends BaseView {

    void onSearchViewClosed(MenuItem menuItem);

    LoaderManager getLoaderManager();

    void swapCursor(Cursor cursor);

    void showArticle(int position);

    void onQueryTextChange(String newText);

    void articleChanged(int position);

    Article getArticle(int position);

    void showUndo(Article article);

    void createShareView(Article article);

    void closeShareView();
}
