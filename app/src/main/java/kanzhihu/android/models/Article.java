package kanzhihu.android.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import kanzhihu.android.database.table.ArticleTable;

/**
 * Created by Jiahui.wen on 2014/11/7.
 */
public class Article implements Parcelable {

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

    //================= parceable =====================

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.link);
        dest.writeString(this.title);
        dest.writeString(this.imageLink);
        dest.writeString(this.writer);
        dest.writeString(this.writerLink);
        dest.writeInt(this.agreeCount);
        dest.writeString(this.summary);
        dest.writeLong(this.category_id);
    }

    public Article() {
    }

    private Article(Parcel in) {
        this.link = in.readString();
        this.title = in.readString();
        this.imageLink = in.readString();
        this.writer = in.readString();
        this.writerLink = in.readString();
        this.agreeCount = in.readInt();
        this.summary = in.readString();
        this.category_id = in.readLong();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public static Article fromCursor(Cursor cursor) {
        Article article = new Article();

        //article.id = cursor.getLong(cursor.getColumnIndex(ArticleTable._ID));
        article.link = cursor.getString(cursor.getColumnIndex(ArticleTable.LINK));
        article.title = cursor.getString(cursor.getColumnIndex(ArticleTable.TITLE));
        article.imageLink = cursor.getString(cursor.getColumnIndex(ArticleTable.IMAGE_LINK));
        article.writer = cursor.getString(cursor.getColumnIndex(ArticleTable.WRITER));
        article.writerLink = cursor.getString(cursor.getColumnIndex(ArticleTable.WRITER_LINK));
        article.agreeCount = cursor.getInt(cursor.getColumnIndex(ArticleTable.AGREE_COUNT));
        article.summary = cursor.getString(cursor.getColumnIndex(ArticleTable.SUMMARY));
        article.category_id = cursor.getInt(cursor.getColumnIndex(ArticleTable.CATEGORY_ID));

        return article;
    }
}
