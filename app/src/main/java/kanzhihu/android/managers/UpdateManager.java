package kanzhihu.android.managers;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.path.android.jobqueue.JobManager;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.jobs.CheckVersionJob;
import kanzhihu.android.jobs.DownloadAppJob;
import kanzhihu.android.listeners.DialogSelectListener;
import kanzhihu.android.ui.fragments.UpdateDialogFragment;

/**
 * Created by Jiahui.wen on 2014/11/11.
 */
public class UpdateManager {

    private static String app_url = "";

    JobManager mJobManager;

    NotifyManager mNotifyManager;

    public UpdateManager(JobManager manager, NotifyManager notifyManager) {
        mJobManager = manager;
    }

    public void CheckVersion() {
        mJobManager.addJob(new CheckVersionJob());
    }

    public void registerUpdateBroadcast() {
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                LocalBroadcastManager.getInstance(App.getAppContext()).unregisterReceiver(this);
                showDialog();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.ACTION_NEW_VERSION_APP);
        //注册本地Broadcast
        LocalBroadcastManager.getInstance(App.getAppContext()).registerReceiver(receiver, filter);
    }

    private void showDialog() {
        Activity activity = App.getInstance().topActivity();
        final UpdateDialogFragment dialog = new UpdateDialogFragment();
        dialog.setListener(new DialogSelectListener() {
            @Override public void onConfirm() {
                dialog.dismiss();
                //确定下载新版本
                mNotifyManager.registerEventbus();
                mJobManager.addJob(new DownloadAppJob());
            }

            @Override public void onCancel() {
                dialog.dismiss();
            }
        });
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "update");
    }

    public void setUpdateUrl(String url) {
        app_url = url;
    }

    public String getUpdateUrl() {
        return app_url;
    }
}
