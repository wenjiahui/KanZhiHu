package kanzhihu.android.models;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.List;
import kanzhihu.android.database.table.CategoryTable;
import kanzhihu.android.utils.Cache;

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
public class Category {
    public int _id;
    public String title;
    public String link;
    public String commentsLink;
    public long pubDate;
    public String creator;
    public String guid;
    public String description;
    public String encoded;
    public String commentRssLink;
    public String comments;

    public List<Article> articles;

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(CategoryTable.TITLE, title);
        values.put(CategoryTable.LINK, link);
        values.put(CategoryTable.COMMENTS_LINK, commentsLink);
        values.put(CategoryTable.PUBLISH_DATE, pubDate);
        values.put(CategoryTable.CREATOR, creator);
        values.put(CategoryTable.GUID, guid);
        values.put(CategoryTable.DESCRIPTION, description);
        values.put(CategoryTable.ENCODED, encoded);
        values.put(CategoryTable.COMMENT_RSS_LINK, commentRssLink);
        values.put(CategoryTable.COMMENTS, comments);

        return values;
    }

    public static Category fromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(CategoryTable._ID));
        Category category = Cache.getCategory(id);
        if (category == null) {
            category = new Category();
            category._id = id;
            category.title = cursor.getString(cursor.getColumnIndex(CategoryTable.TITLE));
            category.link = cursor.getString(cursor.getColumnIndex(CategoryTable.LINK));
            category.commentsLink = cursor.getString(cursor.getColumnIndex(CategoryTable.COMMENTS_LINK));
            category.pubDate = cursor.getLong(cursor.getColumnIndex(CategoryTable.PUBLISH_DATE));
            category.creator = cursor.getString(cursor.getColumnIndex(CategoryTable.CREATOR));
            category.guid = cursor.getString(cursor.getColumnIndex(CategoryTable.GUID));
            category.description = cursor.getString(cursor.getColumnIndex(CategoryTable.DESCRIPTION));
            //encoded字段暂时不需要
            //category.encoded = cursor.getString(cursor.getColumnIndex(CategoryTable.ENCODED));
            category.commentRssLink = cursor.getString(cursor.getColumnIndex(CategoryTable.COMMENT_RSS_LINK));
            category.comments = cursor.getString(cursor.getColumnIndex(CategoryTable.COMMENTS));
            Cache.cache(category);
        }
        return category;
    }
}
