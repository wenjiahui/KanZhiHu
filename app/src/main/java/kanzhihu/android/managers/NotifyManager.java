package kanzhihu.android.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import de.greenrobot.event.EventBus;
import java.io.File;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.events.DownloadProgressEvent;
import kanzhihu.android.events.DownloadingApkEvent;
import kanzhihu.android.utils.FileUtils;

/**
 * Created by Jiahui.wen on 2014/11/12.
 */
public class NotifyManager {

    public static NotifyManager manager;

    private NotificationManager mNotificationManager;
    private int mDownloadNotifyID = 1;
    private Notification mDownloadNotification;
    private NotificationCompat.Builder mNotifyBuilder;

    public static NotifyManager getManager() {
        if (manager == null) {
            manager = new NotifyManager();
        }
        return manager;
    }

    public void registerEventbus() {
        EventBus.getDefault().register(this);
    }

    public void unregisterEventbus() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 开始下载apk
     */
    public void onEventMainThread(DownloadingApkEvent event) {
        mNotificationManager = (NotificationManager) App.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(App.getAppContext()).setContentTitle(
            App.getAppContext().getResources().getString(R.string.downloading_kanzhi))
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setAutoCancel(false)
            .setOngoing(true)
            .setProgress(100, 0, true);
        mDownloadNotification = mNotifyBuilder.build();
        mNotificationManager.notify(mDownloadNotifyID, mDownloadNotification);
    }

    /**
     * 下载apk的进度
     */
    public void onEventMainThread(DownloadProgressEvent event) {
        if (mNotificationManager == null || mNotifyBuilder == null) {
            return;
        }

        if (event.progress >= 100) {
            mNotificationManager.cancel(mDownloadNotifyID);
            notifyComplete(mNotificationManager);
        } else {
            mNotifyBuilder.setProgress(100, event.progress, false);
            mNotificationManager.notify(mDownloadNotifyID, mNotifyBuilder.build());
        }
    }

    private void notifyComplete(NotificationManager mNotificationManager) {
        File apkfile = new File(FileUtils.getCachePath(), AppConstant.APK_NAME);
        if (apkfile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            PendingIntent notifyIntent =
                PendingIntent.getActivity(App.getAppContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotifyBuilder = new NotificationCompat.Builder(App.getAppContext()).setContentTitle(
                App.getAppContext().getResources().getString(R.string.downloaded_kanzhi))
                .setContentText(App.getAppContext().getResources().getString(R.string.downloaded_kanzhi_details))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(notifyIntent)
                .setAutoCancel(false);
            mDownloadNotification = mNotifyBuilder.build();
            mNotificationManager.notify(mDownloadNotifyID, mDownloadNotification);
        }
    }
}
