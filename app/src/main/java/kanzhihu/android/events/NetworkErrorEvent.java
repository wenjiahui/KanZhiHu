package kanzhihu.android.events;

import android.support.annotation.StringRes;

/**
 * Created by Jiahui.wen on 2014/12/9.
 *
 * 无网络连接的事件
 */
public class NetworkErrorEvent {

    public int messageRes;

    public String message;

    public NetworkErrorEvent() {
    }

    public NetworkErrorEvent(@StringRes int messageRes) {
        this.messageRes = messageRes;
    }

    public NetworkErrorEvent(String message) {
        this.message = message;
    }
}
