package eldeveloper13.currencyconversion;

import android.app.Application;

import eldeveloper13.currencyconversion.dagger2.AppComponent;
import eldeveloper13.currencyconversion.dagger2.AppModule;
import eldeveloper13.currencyconversion.dagger2.DaggerAppComponent;

public class CurrencyConversionApplication extends Application {

    private AppComponent mAppComponent = createComponent();

    protected AppComponent createComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() { return mAppComponent; }
}
