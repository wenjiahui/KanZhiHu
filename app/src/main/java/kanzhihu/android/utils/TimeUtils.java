package kanzhihu.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import kanzhihu.android.AppConstant;

/**
 * Created by Jiahui.wen on 2014/11/8.
 */
public class TimeUtils {

    private final static SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat(AppConstant.PUBLISH_DATE_PATTERN, Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT-0000"));
    }

    public static long getTimeMillis(String date) {
        long time = 0l;
        try {
            time = formatter.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String formatTime(long timemillis) {

        return formatter.format(new Date(timemillis));
    }
}
