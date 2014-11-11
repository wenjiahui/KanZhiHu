package kanzhihu.android;

import junit.framework.TestCase;
import kanzhihu.android.utils.TimeUtils;

/**
 * Created by Jiahui.wen on 2014/11/8.
 */
public class TimeUtilsTest extends TestCase {

    public void testGetTimeMillis() {
        assertEquals("Fri, 07 Nov 2014 09:00:00 +0000",
            TimeUtils.formatTime(TimeUtils.getTimeMillis("Fri, 07 Nov 2014 09:00:00 +0000")));
    }
}
