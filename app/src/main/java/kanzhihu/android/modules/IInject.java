package kanzhihu.android.modules;

import java.util.List;

/**
 * Created by Jiahui.wen on 15-3-1.
 */
public interface IInject {

    void inject(Object target);

    List<Object> listModules();
}
