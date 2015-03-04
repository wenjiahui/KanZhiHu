package kanzhihu.android.ui.presenters;

import kanzhihu.android.events.ArticleReadEvent;
import kanzhihu.android.events.ShareArticleEvent;
import kanzhihu.android.events.ViewAuthorEvent;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public interface ArticlesPresenter extends BasePresenter {

    void loadArticles();

    void onEventMainThread(ShareArticleEvent event);

    void onEventMainThread(ArticleReadEvent event);

    void onEventMainThread(ViewAuthorEvent event);

    void markArticleChanged(int position, Article article, boolean isChecked);

    void onShareArticle(int position);

    void readArticle(Article article);
}
