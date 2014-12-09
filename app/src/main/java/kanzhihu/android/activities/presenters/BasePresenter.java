package kanzhihu.android.activities.presenters;

import kanzhihu.android.events.ImageModeChangeEvent;

/**
 * Created by Jiahui.wen on 2014/11/23.
 */
public interface BasePresenter {

    void init();

    void onEventMainThread(ImageModeChangeEvent event);

    void onDestory();
}
