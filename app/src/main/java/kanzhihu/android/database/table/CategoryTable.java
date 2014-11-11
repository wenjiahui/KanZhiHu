package kanzhihu.android.database.table;

public interface CategoryTable {
    String TABLE_NAME = "category";

    String _ID = "_id";

    String TITLE = "title";
    String LINK = "link";
    String COMMENTS_LINK = "comments_link";
    String PUBLISH_DATE = "publish_date";
    String CREATOR = "creator";
    String GUID = "guid";
    String DESCRIPTION = "description";
    String ENCODED = "encoded";
    String COMMENT_RSS_LINK = "comment_rss_link";
    String COMMENTS = "comments";
    String[] ALL_COLUMNS = new String[] {
        _ID, TITLE, LINK, COMMENTS_LINK, PUBLISH_DATE, CREATOR, GUID, DESCRIPTION, ENCODED, COMMENT_RSS_LINK, COMMENTS
    };

    String SQL_CREATE =
        "CREATE TABLE category ( _id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, link TEXT, comments_link TEXT, publish_date NUMERIC, creator TEXT, guid TEXT, description TEXT, encoded TEXT, comment_rss_link TEXT, comments TEXT )";

    String SQL_INSERT =
        "INSERT INTO category ( title, link, comments_link, publish_date, creator, guid, description, encoded, comment_rss_link, comments ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

    String SQL_DROP = "DROP TABLE IF EXISTS category";

    String WHERE_ID_EQUALS = _ID + "=?";
}