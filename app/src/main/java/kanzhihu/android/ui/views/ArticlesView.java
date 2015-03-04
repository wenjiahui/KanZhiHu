package kanzhihu.android.ui.views;

import java.util.ArrayList;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public interface ArticlesView extends BaseView {

    Article getArticle(int position);

    void onLoadArticlesFinished(ArrayList<Article> articles);

    void articleChanged(int position);

    void createShareView(Article article);

    void closeShareView();
}
