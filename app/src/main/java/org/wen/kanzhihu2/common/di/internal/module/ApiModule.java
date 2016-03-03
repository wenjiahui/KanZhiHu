package org.wen.kanzhihu2.common.di.internal.module;

import android.app.Application;
import com.squareup.moshi.Moshi;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.wen.kanzhihu2.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Created by Jiahui.wen on 15-11-4.
 */
@Module
public class ApiModule {

    @Singleton
    @Provides
    public HttpLoggingInterceptor provideLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Timber.d(message));
        interceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
        return interceptor;
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(Application context, HttpLoggingInterceptor logger) {
        Cache cache = new Cache(context.getCacheDir(), 20 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().retryOnConnectionFailure(true)
                                                              .connectTimeout(10, TimeUnit.SECONDS)
                                                              .cache(cache)
                                                              .addInterceptor(logger)
                                                              .build();
        return okHttpClient;
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, Moshi moshi) {
        return new Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
                                     .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                     .baseUrl("http://api.kanzhihu.com/")
                                     .client(okHttpClient)
                                     .build();
    }
}
