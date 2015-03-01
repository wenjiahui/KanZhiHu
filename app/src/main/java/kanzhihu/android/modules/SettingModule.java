package kanzhihu.android.modules;

import dagger.Module;
import kanzhihu.android.ui.SettingActivity;
import kanzhihu.android.ui.fragments.SettingFragment;

/**
 * Created by Jiahui.wen on 15-3-1.
 */
@Module(
    injects = { SettingActivity.class, SettingFragment.class },
    complete = false)
public class SettingModule {
}
