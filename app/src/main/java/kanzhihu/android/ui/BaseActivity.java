package kanzhihu.android.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewConfiguration;
import butterknife.ButterKnife;
import java.lang.reflect.Field;

/**
 * Created by Jiahui.wen on 2014/11/28.
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.inject(this);

        forceShowActionBarOverflowMenu();
    }

    public abstract @LayoutRes int getLayoutRes();

    /**
     * 在4.2系统以下的版本中，菜单是需要按下菜单键才触发的
     */
    private void forceShowActionBarOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {

        }
    }

    //todo not work
    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
    //    if(event.getAction() == KeyEvent.ACTION_DOWN){
    //        switch (keyCode) {
    //            case KeyEvent.KEYCODE_MENU:
    //                try {
    //                    WindowDecorActionBar actionBar = (WindowDecorActionBar) getSupportActionBar();
    //                    Field contextViewField = WindowDecorActionBar.class.getDeclaredField("mContextView");
    //                    contextViewField.setAccessible(true);
    //                    ActionBarContextView contextView = (ActionBarContextView) contextViewField.get(actionBar);
    //                    if (!contextView.isOverflowMenuShowing()) {
    //                        contextView.showOverflowMenu();
    //                    }
    //                } catch (Exception ignored) {
    //
    //                }
    //                return true;
    //        }
    //    }
    //
    //    return super.onKeyDown(keyCode, event);
    //}
}
