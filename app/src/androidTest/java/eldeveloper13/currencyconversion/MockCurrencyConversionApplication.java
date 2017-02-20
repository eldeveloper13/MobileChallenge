package eldeveloper13.currencyconversion;

import eldeveloper13.currencyconversion.dagger2.AppComponent;

public class MockCurrencyConversionApplication extends CurrencyConversionApplication {

    @Override
    protected AppComponent createComponent() {
        return DaggerMainActivityTest_TestComponent.builder()
                .mockAppModule(new MockAppModule(this))
                .build();
    }
}
