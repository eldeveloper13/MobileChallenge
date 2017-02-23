package eldeveloper13.currencyconversion.dagger2;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eldeveloper13.currencyconversion.data.FixerService;
import eldeveloper13.currencyconversion.data.RateProviderImpl;
import eldeveloper13.currencyconversion.data.RateProvider;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application){
        mApplication = application;
    }

    @Provides
    Context provideContext() {
        return mApplication;
    }

    @Singleton
    @Provides
    RateProvider providesRateProvider(RateProviderImpl rateProvider) {
        return rateProvider;
    }

    @Singleton
    @Provides
    FixerService providesFixerService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fixer.io")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FixerService.class);
    }
}
