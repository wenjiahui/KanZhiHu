package kanzhihu.android.ui.presenters;

import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/23.
 */
public interface BrowsePresenter extends BasePresenter {

    void browseArticle(Article article);

    void onMarkChanged();
}
