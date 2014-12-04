package kanzhihu.android.database.table;

public interface ArticleTable {
    String TABLE_NAME = "article";

    String _ID = "_id";

    String LINK = "link";
    String TITLE = "title";
    String IMAGE_LINK = "image_link";
    String WRITER = "writer";
    String WRITER_LINK = "writer_link";
    String AGREE_COUNT = "agree_count";
    String SUMMARY = "summary";
    String CATEGORY_ID = "category_id";
    String MARKED = "marked";
    String READ = "read";

    String[] ALL_COLUMNS = new String[] {
        _ID, LINK, TITLE, IMAGE_LINK, WRITER, WRITER_LINK, AGREE_COUNT, SUMMARY, CATEGORY_ID, MARKED, READ
    };

    String SQL_CREATE = "CREATE TABLE article ( _id INTEGER PRIMARY KEY AUTOINCREMENT, link TEXT, title TEXT, "
        + "image_link TEXT, writer TEXT, writer_link TEXT, agree_count INTEGER, "
        + "summary TEXT, category_id INTEGER, marked INTEGER, read INTEGER )";

    String SQL_INSERT = "INSERT INTO article ( link, title, image_link, writer, writer_link,"
        + "agree_count, summary, category_id, marked, read ) "
        + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

    String SQL_DROP = "DROP TABLE IF EXISTS article";

    String WHERE_ID_EQUALS = _ID + "=?";
}