package kanzhihu.android.events;

import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/25.
 *
 * 自带浏览器浏览文章时收藏操作事件。
 */
public class BrowseMarkChangedEvent {

    public Article article;

    public BrowseMarkChangedEvent() {
    }

    public BrowseMarkChangedEvent(Article article) {
        this.article = article;
    }
}
