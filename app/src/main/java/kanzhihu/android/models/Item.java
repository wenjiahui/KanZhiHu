package kanzhihu.android.models;

import java.util.List;

/**
 * Created by Jiahui.wen on 2014/11/7.
 *
 * 每天的三篇推荐代表三个item。 <br/>
 *
 * see: www.kanzhihu.com/faq<br/>
 *
 * 1. 2014年11月7日 历史精华 <br/>
 * 2. 2014年11月7日 近日热门 <br/>
 * 3. 2014年11月7日 昨日最新 <br/>
 */
public class Item {
    public long _id;
    public String title;
    public String link;
    public String commentsLink;
    public long pubDate;
    public String creator;
    public String category;
    public String guid;
    public String description;
    public String encoded;
    public String commentRssLink;
    public String comments;

    public List<Article> articles;
}
