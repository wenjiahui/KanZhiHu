package kanzhihu.android.managers;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import kanzhihu.android.AppConstant;

/**
 * Created by Jiahui.wen on 2014/11/7.
 */
public class HttpClientManager {

    private static OkHttpClient client;

    static {
        client = new OkHttpClient();
        client.setConnectTimeout(AppConstant.DEFAULT_CONNECT_TIME_OUT, TimeUnit.SECONDS);
        client.setReadTimeout(AppConstant.DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static Response request(Request request) throws IOException {
        return getClient().newCall(request).execute();
    }

    public static void cancelRequest(Object tag) {
        client.cancel(tag);
    }
}
