package kanzhihu.android.modules;

import dagger.ObjectGraph;

/**
 * Created by Jiahui.wen on 15-1-25.
 */
public class Injector {

    private static ObjectGraph graph;

    public static <T> void inject(T target) {
        graph.inject(target);
    }

    public static ObjectGraph plus(IInject injector) {
        return graph.plus(injector.listModules().toArray());
    }

    public static void setGraph(ObjectGraph objectGraph) {
        graph = objectGraph;
    }
}
