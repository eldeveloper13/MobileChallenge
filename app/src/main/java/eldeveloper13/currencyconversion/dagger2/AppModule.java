package eldeveloper13.currencyconversion.dagger2;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eldeveloper13.currencyconversion.data.MockRateProviderImpl;
import eldeveloper13.currencyconversion.data.RateProvider;

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
    RateProvider providesRateProvider(MockRateProviderImpl rateProvider) {
        return rateProvider;
    }
}
