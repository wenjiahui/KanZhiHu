package kanzhihu.android.fragments.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Created by Jiahui.wen on 2014/10/9.
 */
public abstract class BaseFragment extends Fragment {

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getViewRec(), container, false);

        ButterKnife.inject(this, rootView);

        return rootView;
    }

    public abstract int getViewRec();
}
