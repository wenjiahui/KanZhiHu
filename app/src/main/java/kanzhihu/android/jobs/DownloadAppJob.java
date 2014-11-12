package kanzhihu.android.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import kanzhihu.android.AppConstant;
import kanzhihu.android.events.DownloadedApkEvent;
import kanzhihu.android.managers.HttpClientManager;
import kanzhihu.android.managers.UpdateManager;
import kanzhihu.android.utils.FileUtils;
import kanzhihu.android.utils.IOUtils;

/**
 * Created by Jiahui.wen on 2014/11/11.
 *
 * 下载app更新包
 */
public class DownloadAppJob extends Job {

    public DownloadAppJob() {
        super(new Params(Priority.LOW).requireNetwork().persist());
    }

    @Override public void onAdded() {

    }

    @Override public void onRun() throws Throwable {
        Request request = new Request.Builder().url(UpdateManager.getUpdateUrl()).build();
        Response response = HttpClientManager.request(request);

        String path = FileUtils.getCachePath();
        File apk = new File(path, AppConstant.APK_NAME);
        if (apk.exists()) {
            apk.delete();
        }

        byte[] bytes = new byte[1024];
        int readCount;
        InputStream is = response.body().byteStream();
        FileOutputStream fos = new FileOutputStream(apk);

        try {
            while ((readCount = is.read(bytes)) != -1) {
                fos.write(bytes, 0, readCount);
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
