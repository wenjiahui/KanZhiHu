package kanzhihu.android.models;

import android.content.ContentValues;
import kanzhihu.android.database.table.ArticleTable;

/**
 * Created by Jiahui.wen on 2014/11/7.
 */
public class Article {

    public String link;
    public String title;
    public String imageLink;
    public String writer;
    public String writerLink;
    public int agreeCount;
    public String summary;
    public long category_id;

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ArticleTable.LINK, link);
        values.put(ArticleTable.TITLE, title);
        values.put(ArticleTable.IMAGE_LINK, imageLink);
        values.put(ArticleTable.WRITER, writer);
        values.put(ArticleTable.WRITER_LINK, writerLink);
        values.put(ArticleTable.AGREE_COUNT, agreeCount);
        values.put(ArticleTable.SUMMARY, summary);
        values.put(ArticleTable.CATEGORY_ID, category_id);
        return values;
    }
}
