package org.wen.kanzhihu;

import android.app.Application;
import im.fir.sdk.FIR;

/**
 * Created by Jiahui.wen on 15-10-31.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        FIR.init(this);
        super.onCreate();
    }
}
