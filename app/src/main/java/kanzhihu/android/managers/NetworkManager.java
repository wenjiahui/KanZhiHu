package kanzhihu.android.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import kanzhihu.android.App;

/**
 * Created by Jiahui.wen on 2014/12/8.
 */
public class NetworkManager {

    public static boolean isConnected() {
        ConnectivityManager connMgr =
            (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnectedOrConnecting();
    }
}
