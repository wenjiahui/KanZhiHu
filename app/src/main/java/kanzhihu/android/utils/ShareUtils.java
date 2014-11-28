package kanzhihu.android.utils;

import android.content.Intent;
import kanzhihu.android.R;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/25.
 */
public class ShareUtils {

    public static Intent getShareIntent(Article article) {
        AssertUtils.requireNonNull(article);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, buildShareMessage(article));
        shareIntent.setType("text/plain");

        return shareIntent;
    }

    private static String buildShareMessage(Article article) {
        return StringUtils.getString(R.string.share_title, article.title, article.summary, article.link);
    }
}
