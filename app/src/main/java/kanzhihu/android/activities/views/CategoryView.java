package kanzhihu.android.activities.views;

import android.app.LoaderManager;

/**
 * Created by Jiahui.wen on 2014/11/13.
 */
public interface CategoryView {

    void showArticles(String categoryId);

    void showFetchRssUI();

    void hideFetchRssUI();

    LoaderManager getLoaderManager();
}
