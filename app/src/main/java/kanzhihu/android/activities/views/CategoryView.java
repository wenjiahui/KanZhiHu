package kanzhihu.android.activities.views;

import android.app.LoaderManager;

/**
 * Created by Jiahui.wen on 2014/11/13.
 */
public interface CategoryView extends BaseView {

    void showArticles(int position);

    void showFetchRssUI();

    void hideFetchRssUI();

    LoaderManager getLoaderManager();
}
