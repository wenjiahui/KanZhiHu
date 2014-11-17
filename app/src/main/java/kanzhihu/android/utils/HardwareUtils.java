package kanzhihu.android.utils;

import android.app.Activity;
import android.graphics.Point;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public class HardwareUtils {

    public static Point getScrenSize(Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);

        return point;
    }
}
