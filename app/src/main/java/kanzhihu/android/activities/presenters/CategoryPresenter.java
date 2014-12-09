package kanzhihu.android.activities.presenters;

import android.app.LoaderManager;
import android.database.Cursor;
import kanzhihu.android.events.FetchedRssEvent;
import kanzhihu.android.events.ListitemClickEvent;

/**
 * Created by Jiahui.wen on 2014/11/13.
 */
public interface CategoryPresenter extends BasePresenter {

    void onEventMainThread(FetchedRssEvent event);

    void onEventMainThread(ListitemClickEvent event);

    void fetchRss();

    void loadDataFromDB(LoaderManager.LoaderCallbacks callbacks);

    void loadDataFromDBComplete(Cursor cursor);

    void unloadDataFromDb();
}
