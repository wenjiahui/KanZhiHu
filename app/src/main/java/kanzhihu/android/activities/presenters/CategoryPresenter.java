package kanzhihu.android.activities.presenters;

import android.app.LoaderManager;
import android.database.Cursor;

/**
 * Created by Jiahui.wen on 2014/11/13.
 */
public interface CategoryPresenter {

    void init();

    void bindEvent();

    void unBindEvent();

    void fetchRss();

    void loadDataFromDB(LoaderManager.LoaderCallbacks callbacks);

    void loadDataFromDBComplete(Cursor cursor);

    void unloadDataFromDb();
}
