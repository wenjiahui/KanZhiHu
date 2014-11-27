package kanzhihu.android.activities.views;

import android.app.Activity;
import java.util.ArrayList;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public interface ArticlesView {

    Activity getContext();

    void onLoadArticlesFinished(ArrayList<Article> articles);

    void articleChanged(int position);

    boolean getVisiable();

    void createShareView(Article article);

    void closeShareView();
}
