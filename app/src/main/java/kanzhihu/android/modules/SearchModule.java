package kanzhihu.android.modules;

import dagger.Module;
import kanzhihu.android.ui.SearchActivity;
import kanzhihu.android.ui.fragments.SearchFragment;

/**
 * Created by Jiahui.wen on 15-3-1.
 */
@Module(
    complete = false,
    injects = { SearchActivity.class, SearchFragment.class }
)
public class SearchModule {
}
