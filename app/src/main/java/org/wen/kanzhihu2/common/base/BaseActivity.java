package org.wen.kanzhihu2.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusAppCompatActivity;
import org.wen.kanzhihu2.App;
import org.wen.kanzhihu2.R;
import org.wen.kanzhihu2.common.Navigator;
import org.wen.kanzhihu2.common.di.internal.component.AppComponent;

/**
 * Created by Jiahui.wen on 16-1-21.
 */
public abstract class BaseActivity<PresenterType extends Presenter> extends NucleusAppCompatActivity<PresenterType> {

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolBarView;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setUpToolBar();
    }

    protected void setUpToolBar() {
        if (toolBarView == null) {
            return;
        }

        setSupportActionBar(toolBarView);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && showHomeAsUp()) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolBarView.setNavigationOnClickListener((v) -> onClickNavigationBack());
        }
    }

    protected boolean showHomeAsUp() {
        return true;
    }

    protected void onClickNavigationBack() {
        finish();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    public Navigator getNavigator() {
        return getAppComponent().getNavigator();
    }

    public AppComponent getAppComponent() {
        return App.from(this).getAppComponent();
    }
}
