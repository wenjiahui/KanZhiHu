package kanzhihu.android;

import kanzhihu.android.database.table.CategoryTable;

/**
 * Created by Jiahui.wen on 2014/10/29.
 */
public class AppConstant {

    public static final String APK_NAME = "kanzhihu_lastest.apk";

    public static final String RSS_URL = "http://www.kanzhihu.com/feed";

    public static final String PREF_KEY_APP_VERSION = "pref_key_app_version";

    public static final String PREF_KEY_SAVE_DAYS = "pref_saveDays";

    public static final String PREF_KEY_AUTO_REFRESH = "pref_auto_refresh";

    public static final String PREF_KEY_AUTO_UPDATE = "pref_auto_update";

    public static final String PREF_KEY_BROWSER = "pref_browser_select";

    public static final String KEY_PREFERENCE = BuildConfig.APPLICATION_ID + "_preferences";

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

    public static int ID_CATEGORY_LOADER = 0X123;
    public static int ID_ARTICLE_LOADER = 0X125;
}
