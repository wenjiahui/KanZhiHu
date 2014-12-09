package kanzhihu.android.events;

import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/12/5.
 */
public class ArticleReadEvent {

    public Article article;

    public ArticleReadEvent() {
    }

    public ArticleReadEvent(Article article) {
        this.article = article;
    }
}
