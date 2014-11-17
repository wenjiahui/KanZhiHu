package kanzhihu.android.managers;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

/**
 * Created by Jiahui.wen on 2014/11/7.
 */
public class HttpClientManager {

    private static OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }

    public static Response request(Request request) throws IOException {
        return getClient().newCall(request).execute();
    }
}
