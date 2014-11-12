package kanzhihu.android.managers;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.fragments.UpdateDialogFragment;
import kanzhihu.android.jobs.CheckVersionJob;
import kanzhihu.android.jobs.DownloadAppJob;
import kanzhihu.android.listeners.DialogSelectListener;

/**
 * Created by Jiahui.wen on 2014/11/11.
 */
public class UpdateManager {

    private static String app_url = "";

    public static void CheckVersion() {
        BackThreadManager.getJobManager().addJob(new CheckVersionJob());
    }

    public static void registerUpdateBroadcast() {
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

    private static void showDialog() {
        Activity activity = App.getInstance().topActivity();
        final UpdateDialogFragment dialog = new UpdateDialogFragment();
        dialog.setListener(new DialogSelectListener() {
            @Override public void onConfirm() {
                dialog.dismiss();
                //确定下载新版本
                BackThreadManager.getJobManager().addJob(new DownloadAppJob());
            }

            @Override public void onCancel() {
                dialog.dismiss();
            }
        });
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "update");
    }

    public static void setUpdateUrl(String url) {
        app_url = url;
    }

    public static String getUpdateUrl() {
        return app_url;
    }
}
