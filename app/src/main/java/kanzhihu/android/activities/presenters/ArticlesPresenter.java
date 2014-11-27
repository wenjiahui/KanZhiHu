package kanzhihu.android.activities.presenters;

import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public interface ArticlesPresenter {

    void loadArticles();

    void markArticleChanged(int position, Article article, boolean isChecked);

    void onShareArticle(int position);

    void onDestory();
}
