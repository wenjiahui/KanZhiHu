package kanzhihu.android.modules;

import dagger.Module;
import kanzhihu.android.ui.MainActivity;
import kanzhihu.android.ui.fragments.ArticlesFragment;
import kanzhihu.android.ui.fragments.CategoryFragment;

/**
 * Created by Jiahui.wen on 15-3-1.
 */
@Module(
    injects = { MainActivity.class, CategoryFragment.class, ArticlesFragment.class },
    complete = false
)
public class MainModule {
}
