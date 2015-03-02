package kanzhihu.android.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import java.lang.reflect.Field;
import kanzhihu.android.R;

/**
 * Created by Jiahui.wen on 2014/11/28.
 */
public abstract class BaseActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.primary);
        tintManager.setNavigationBarTintResource(R.color.primary);

        forceShowActionBarOverflowMenu();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
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
