package kanzhihu.android.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.inject.Inject;
import kanzhihu.android.AppConstant;
import kanzhihu.android.events.DownloadProgressEvent;
import kanzhihu.android.events.DownloadedApkEvent;
import kanzhihu.android.events.DownloadingApkEvent;
import kanzhihu.android.managers.NetworkManager;
import kanzhihu.android.managers.UpdateManager;
import kanzhihu.android.modules.Injector;
import kanzhihu.android.utils.FileUtils;
import kanzhihu.android.utils.IOUtils;

/**
 * Created by Jiahui.wen on 2014/11/11.
 *
 * 下载app更新包
 */
public class DownloadAppJob extends Job {

    @Inject UpdateManager mUpdateManager;
    @Inject OkHttpClient mHttpClient;

    public DownloadAppJob() {
        super(new Params(Priority.LOW).persist());

        Injector.inject(this);
    }

    @Override public void onAdded() {
        EventBus.getDefault().post(new DownloadingApkEvent());
    }

    @Override public void onRun() throws Throwable {
        if (!NetworkManager.isConnected()) {
            return;
        }

        Request request = new Request.Builder().url(mUpdateManager.getUpdateUrl()).build();
        Response response = mHttpClient.newCall(request).execute();

        String path = FileUtils.getCachePath();
        File apk = new File(path, AppConstant.APK_NAME);
        if (apk.exists()) {
            apk.delete();
        }

        InputStream is = response.body().byteStream();
        FileOutputStream fos = new FileOutputStream(apk);
        DownloadProgressEvent event = new DownloadProgressEvent();

        byte[] bytes = new byte[1024 * 8];
        long totalLength = response.body().contentLength();
        int count = 0;
        int readCount;

        try {
            while ((readCount = is.read(bytes)) != -1) {
                fos.write(bytes, 0, readCount);
                count += readCount;
                int progress = (int) ((count * 1.0 / totalLength) * 100);
                if (progress > event.progress) {
                    event.progress = progress;
                    EventBus.getDefault().post(event);
                }
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fos);
            IOUtils.close(is);
        }

        response.body().close();

        EventBus.getDefault().post(new DownloadedApkEvent());
    }

    @Override protected void onCancel() {

    }

    @Override protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
