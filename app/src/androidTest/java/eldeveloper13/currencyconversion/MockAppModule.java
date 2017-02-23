package eldeveloper13.currencyconversion;

import android.app.Application;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eldeveloper13.currencyconversion.data.CurrencyCodeProvider;
import eldeveloper13.currencyconversion.data.RateProvider;

@Module
public class MockAppModule {

    Application mApplication;

    public MockAppModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    RateProvider providesRateProvider() {
        return Mockito.mock(RateProvider.class);
    }

    @Provides
    @Singleton
    CurrencyCodeProvider providesCurrencCodeProvider() {
        return Mockito.mock(CurrencyCodeProvider.class);
    }
}
