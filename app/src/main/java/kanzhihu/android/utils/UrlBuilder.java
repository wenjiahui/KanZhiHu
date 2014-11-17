package kanzhihu.android.utils;

import android.text.TextUtils;
import java.util.Calendar;
import kanzhihu.android.AppConstant;
import kanzhihu.android.models.Category;

/**
 * Created by Jiahui.wen on 2014/11/17.
 *
 * 2014年11月15日 历史精华
 * http://www.kanzhihu.com/wp-content/uploads/2014/11/wpid-archive-2014-11-15-720x340.jpg
 *
 * 2014年11月15日 近日热门
 * http://www.kanzhihu.com/wp-content/uploads/2014/11/wpid-recent-2014-11-15-520x245.jpg
 *
 * 2014年11月15日 昨日最新
 * http://www.kanzhihu.com/wp-content/uploads/2014/11/wpid-yesterday-2014-11-15-520x245.jpg
 */
public class UrlBuilder {

    public static String getScreenShotUrl(Category category, boolean small) {
        String url = "";

        String type = "";
        if (AppConstant.CATEGOTY.ARCHIVE_NAME.equals(category.categoryName)) {
            type = AppConstant.CATEGOTY.ARCHIVE;
        } else if (AppConstant.CATEGOTY.RECENT_NAME.equals(category.categoryName)) {
            type = AppConstant.CATEGOTY.RECENT;
        } else if (AppConstant.CATEGOTY.YESTERDAY_NAME.equals(category.categoryName)) {
            type = AppConstant.CATEGOTY.YESTERDAY;
        }

        if (!TextUtils.isEmpty(type)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(category.pubDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String size = small ? AppConstant.CATEGOTY.SMALL_SIZE : AppConstant.CATEGOTY.LARGE_SIZE;
            url = String.format(AppConstant.CATEGOTY.BASE_URL, year, month, type, year, month, day, size);
        }

        return url;
    }
}
