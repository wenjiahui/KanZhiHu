package kanzhihu.android.activities.views;

import java.util.ArrayList;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public interface ArticlesView {

    void onLoadArticlesFinished(ArrayList<Article> articles);
}
