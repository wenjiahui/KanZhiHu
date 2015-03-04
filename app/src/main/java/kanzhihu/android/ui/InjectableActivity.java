package kanzhihu.android.ui;

import android.os.Bundle;
import dagger.ObjectGraph;
import kanzhihu.android.modules.IInject;
import kanzhihu.android.modules.Injector;

/**
 * Created by Jiahui.wen on 2014/11/28.
 */
public abstract class InjectableActivity extends BaseActivity implements IInject {

    protected ObjectGraph mGraph;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGraph = Injector.plus(this);
        inject(this);
    }

    @Override public void inject(Object target) {
        mGraph.inject(target);
    }

    @Override protected void onDestroy() {
        mGraph = null;
        super.onDestroy();
    }
}
