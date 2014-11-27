package kanzhihu.android.database;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.ActivityChooserModel;
import android.support.v7.internal.widget.ActivityChooserView;
import android.support.v7.internal.widget.TintManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.PopupWindow;
import de.greenrobot.event.EventBus;
import kanzhihu.android.events.ShareMenuDismissEvent;

/**
 * Created by Jiahui.wen on 2014/11/27.
 */
public class ShareActionProvider extends android.support.v7.widget.ShareActionProvider
    implements PopupWindow.OnDismissListener,
    android.support.v7.widget.ShareActionProvider.OnShareTargetSelectedListener {

    private Context mContext;

    private String mShareHistoryFileName = DEFAULT_SHARE_HISTORY_FILE_NAME;

    private ActivityChooserView mChooserView;

    private OnShareTargetSelectedListener mOnShareTargetSelectedListener;

    private ActivityChooserModel.OnChooseActivityListener mOnChooseActivityListener;

    public ShareActionProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override public boolean overridesItemVisibility() {
        return true;
    }

    public void setOnShareTargetSelectedListener(OnShareTargetSelectedListener listener) {
        mOnShareTargetSelectedListener = listener;
        setActivityChooserPolicyIfNeeded();
    }

    public void setShareHistoryFileName(String shareHistoryFile) {
        //需要调用父类方法设置新的shareHistoryFile， 不然显示不了list.
        super.setShareHistoryFileName(shareHistoryFile);
        mShareHistoryFileName = shareHistoryFile;
        setActivityChooserPolicyIfNeeded();

        if (shareHistoryFile == null) {
            //设置默认的OnShareTargetSelected操作
            setOnShareTargetSelectedListener(this);
        }
    }

    @Override
    public View onCreateActionView() {
        // Create the view and set its data model.
        ActivityChooserModel dataModel = ActivityChooserModel.get(mContext, mShareHistoryFileName);
        ActivityChooserView activityChooserView = new ActivityChooserView(mContext);
        activityChooserView.setActivityChooserModel(dataModel);

        // Lookup and set the expand action icon.
        TypedValue outTypedValue = new TypedValue();
        mContext.getTheme()
            .resolveAttribute(android.support.v7.appcompat.R.attr.actionModeShareDrawable, outTypedValue, true);
        Drawable drawable = TintManager.getDrawable(mContext, outTypedValue.resourceId);
        activityChooserView.setExpandActivityOverflowButtonDrawable(drawable);
        activityChooserView.setProvider(this);

        // Set content description.
        activityChooserView.setDefaultActionButtonContentDescription(
            android.support.v7.appcompat.R.string.abc_shareactionprovider_share_with_application);
        activityChooserView.setExpandActivityOverflowButtonContentDescription(
            android.support.v7.appcompat.R.string.abc_shareactionprovider_share_with);

        //设置ondismiss事件
        activityChooserView.setOnDismissListener(this);

        //保留句柄
        this.mChooserView = activityChooserView;

        return activityChooserView;
    }

    @Override public void onDismiss() {
        //发出事件通知菜单隐藏了
        EventBus.getDefault().post(new ShareMenuDismissEvent());
    }

    public void showPopup() {
        if (mChooserView != null && !mChooserView.isShowingPopup()) {
            mChooserView.showPopup();
        }
    }

    /**
     * Set the activity chooser policy of the model backed by the current
     * share history file if needed which is if there is a registered callback.
     */
    private void setActivityChooserPolicyIfNeeded() {
        if (mOnShareTargetSelectedListener == null) {
            return;
        }
        if (mOnChooseActivityListener == null) {
            mOnChooseActivityListener = new ShareActivityChooserModelPolicy();
        }
        ActivityChooserModel dataModel = ActivityChooserModel.get(mContext, mShareHistoryFileName);
        dataModel.setOnChooseActivityListener(mOnChooseActivityListener);
    }

    @Override public boolean onShareTargetSelected(android.support.v7.widget.ShareActionProvider source,
        Intent intent) {
        mContext.startActivity(intent);
        return true;
    }

    /**
     * Policy that delegates to the {@link OnShareTargetSelectedListener}, if such.
     */
    private class ShareActivityChooserModelPolicy implements ActivityChooserModel.OnChooseActivityListener {
        @Override
        public boolean onChooseActivity(ActivityChooserModel host, Intent intent) {
            if (mOnShareTargetSelectedListener != null) {
                //如果外部有相应处理，则直接返回处理的结果
                return mOnShareTargetSelectedListener.onShareTargetSelected(ShareActionProvider.this, intent);
            }
            return false;
        }
    }
}
