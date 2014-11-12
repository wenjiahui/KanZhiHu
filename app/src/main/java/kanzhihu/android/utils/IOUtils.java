package kanzhihu.android.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Jiahui.wen on 2014/11/12.
 */
public class IOUtils {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            closeable = null;
        }
    }
}
