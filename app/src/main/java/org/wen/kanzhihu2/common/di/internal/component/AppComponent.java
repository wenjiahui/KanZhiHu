package org.wen.kanzhihu2.common.di.internal.component;

import dagger.Component;
import javax.inject.Singleton;
import org.wen.kanzhihu2.common.Navigator;
import org.wen.kanzhihu2.common.di.internal.module.AppModule;
import org.wen.kanzhihu2.common.di.internal.module.DataModule;

/**
 * Created by Jiahui.wen on 15-11-4.
 */
@Singleton
@Component(modules = { AppModule.class, DataModule.class })
public interface AppComponent {

    Navigator getNavigator();
}
