package kanzhihu.android.utils;

import android.support.annotation.StringRes;
import kanzhihu.android.App;

/**
 * Created by Jiahui.wen on 2014/11/25.
 */
public class StringUtils {

    public static String getString(@StringRes int strRes) {
        return App.getAppContext().getString(strRes);
    }

    public static String getString(@StringRes int strRes, Object... formatArgs) {
        return App.getAppContext().getString(strRes, formatArgs);
    }
}
