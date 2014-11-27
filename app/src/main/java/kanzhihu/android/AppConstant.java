package kanzhihu.android;

import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.database.table.CategoryTable;

/**
 * Created by Jiahui.wen on 2014/10/29.
 */
public class AppConstant {

    public static final String APK_NAME = "kanzhihu_lastest.apk";

    public static final String RSS_URL = "http://www.kanzhihu.com/feed";

    public static final String IMAGE_LINK = "http://www.kanzhihu.com/%s";

    public static final String KANZHIHU_RESOURCE = "http://www.kanzhihu.com/wp-content/uploads/";

    public static final String PREF_KEY_APP_VERSION = "pref_key_app_version";

    public static final String PREF_KEY_SAVE_DAYS = "pref_saveDays";

    public static final String PREF_KEY_AUTO_REFRESH = "pref_auto_refresh";

    public static final String PREF_KEY_AUTO_UPDATE = "pref_auto_update";

    public static final String PREF_KEY_BROWSER = "pref_browser_select";

    public static final String KEY_PREFERENCE = BuildConfig.APPLICATION_ID + "_preferences";

    public static final long APP_EXIT_TIME_INTERVAL = 2000;

    public interface ITEM_TAG {
        String ITEM = "item";

        String TITLE = "title";
        String LINK = "link";
        String COMMENTS_LINK = "comments";
        String PUBDATE = "pubDate";
        String CREATOR = "creator";
        String GUID = "guid";
        String DESCRIPTION = "description";
        String ENCODED = "encoded";
        String COMMENTS_RSS_LINK = "commentRss";
        String COMMENTS = "comments";
        String CATEGORY = "category";
    }

    /**
     * e.g: Fri, 07 Nov 2014 09:00:00 +0000
     */
    public static String PUBLISH_DATE_PATTERN = "EE, dd MMM yyyy HH:mm:ss Z";

    public static final String CATEGORY_EXIST_SQL =
        "SELECT * FROM " + CategoryTable.TABLE_NAME + " WHERE " + CategoryTable.TITLE + " = '%s'";

    //update app
    public static final String APP_INFO_URL = "http://kanzhihu-android.qiniudn.com/app_info.txt?download=app_info.txt";
    public static final String ACTION_NEW_VERSION_APP = "action.new.version.app";
    public static final String KEY_NEW_VERSION = "new_version";
    public static final String KEY_IGNORE_VERSION = "ignore_version";

    public static int ID_CATEGORY_LOADER = 0X123;

    public static int LOAD_ARTICLES_OK = 1;

    public static String KEY_ARTICLES = "articles";
    public static String KEY_ARTICLE = "article";

    public interface CATEGOTY {
        String ARCHIVE_NAME = "历史精华";
        String RECENT_NAME = "近日热门";
        String YESTERDAY_NAME = "昨日最新";

        String ARCHIVE = "archive";
        String RECENT = "recent";
        String YESTERDAY = "yesterday";

        String LARGE_SIZE = "720x340";
        String SMALL_SIZE = "520x245";

        String BASE_URL = KANZHIHU_RESOURCE + "%d/%d/wpid-%s-%d-%d-%d-%s.jpg";
    }

    //Search
    public static int SEARCH_LOADER_ID = 0X256;
    public static final String SEARCH_SQL_SELECTION =
        ArticleTable.TITLE + " like ? or " + ArticleTable.SUMMARY + " like ?";
    public static final String SEARCH_SQL_SELECTION_FOR_MARK =
        ArticleTable.MARKED + " = 1 and (" + ArticleTable.TITLE + " like ? or " + ArticleTable.SUMMARY + " like ?)";
    public static String SEARCH_SQL_MARK_ONLY = ArticleTable.MARKED + " = 1";
    public static String ACTION_MODE_MARK_VIEW = "action_mode_mark_view";
    public static long UNDO_BAR_DURATION = 2500;

    public static final String KEY_SHARE_ARTICLE = "key_share_article";
}
