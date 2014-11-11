package kanzhihu.android;

/**
 * Created by Jiahui.wen on 2014/10/29.
 */
public class AppConstant {

    public static final String RSS_URL = "http://www.kanzhihu.com/feed";

    public static final String PREF_KEY_APP_VERSION = "pref_key_app_version";

    public static final String PREF_KEY_SAVE_DAYS = "pref_saveDays";

    public static final String KEY_PREFERENCE = BuildConfig.APPLICATION_ID + "_preferences";

    public interface ITEM_TAG {
        String ITEM = "item";

        String TITLE = "title";
        String LINK = "link";
        String COMMENTS_LINK = "comments";
        String PUBDATE = "pubDate";
        String CREATOR = "creator";
        String CATEGORY = "category";
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
}
