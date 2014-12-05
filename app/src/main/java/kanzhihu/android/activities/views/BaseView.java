package kanzhihu.android.activities.views;

import android.app.Activity;

/**
 * Created by Jiahui.wen on 2014/11/23.
 */
public interface BaseView {

    Activity getContext();

    boolean getVisiable();

    void onImageModeChange(boolean imageVisiable);
}
