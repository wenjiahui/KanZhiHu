package kanzhihu.android.utils;

/**
 * Created by Jiahui.wen on 2014/11/13.
 */
public class AssertUtils {

    public static <T> T requireNonNull(T o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
        return o;
    }

    public static <T> T requireNonNull(T o) {
        if (o == null) {
            throw new NullPointerException(o.getClass().getSimpleName() + " must not null");
        }
        return o;
    }
}
