package kanzhihu.android.jobs;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.BuildConfig;
import kanzhihu.android.managers.HttpClientManager;
import kanzhihu.android.managers.UpdateManager;
import kanzhihu.android.utils.AppLogger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jiahui.wen on 2014/11/11.
 *
 * 检查app的最新版本
 */
public class CheckVersionJob extends Job {

    private static final String TAG = CheckVersionJob.class.getSimpleName();

    public CheckVersionJob() {
        super(new Params(Priority.LOW).requireNetwork());
    }

    @Override public void onAdded() {

    }

    @Override public void onRun() throws Throwable {
        Request request = new Request.Builder().url(AppConstant.APP_INFO_URL).build();
        Response response = HttpClientManager.request(request);
        try {
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                compareVersion(jsonObject);
            }
        } finally {
            response.body().close();
        }
    }

    private void compareVersion(JSONObject jsonObject) {
        int lastestVersion = Integer.parseInt(jsonObject.optString("lastest_version", "0"));
        if (lastestVersion > BuildConfig.VERSION_CODE) {
            //服务器上有新版本
            try {
                UpdateManager.setUpdateUrl(jsonObject.getString("url"));
                UpdateManager.registerUpdateBroadcast();
                LocalBroadcastManager.getInstance(App.getAppContext())
                    .sendBroadcast(new Intent(AppConstant.ACTION_NEW_VERSION_APP));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override protected void onCancel() {

    }

    @Override protected boolean shouldReRunOnThrowable(Throwable throwable) {
        AppLogger.e(TAG, throwable);
        return false;
    }
}
