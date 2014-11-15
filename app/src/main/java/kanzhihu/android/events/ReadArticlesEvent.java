package kanzhihu.android.events;

import kanzhihu.android.models.Category;

/**
 * Created by Jiahui.wen on 2014/11/14.
 */
public class ReadArticlesEvent {
    public Category category;

    public ReadArticlesEvent() {
    }

    public ReadArticlesEvent(Category category) {
        this.category = category;
    }
}
