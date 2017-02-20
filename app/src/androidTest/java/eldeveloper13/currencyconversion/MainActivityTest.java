package eldeveloper13.currencyconversion;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import eldeveloper13.currencyconversion.dagger2.AppComponent;
import eldeveloper13.currencyconversion.data.RateProvider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Inject
    RateProvider mRateProvider;

    @Singleton
    @Component(modules = MockAppModule.class)
    public interface TestComponent extends AppComponent {
        void inject(MainActivityTest mainActivityTest);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,
            false);

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        CurrencyConversionApplication app =
                (CurrencyConversionApplication) instrumentation.getTargetContext().getApplicationContext();
        TestComponent component = (TestComponent) app.getAppComponent();
        component.inject(this);
    }

    @Test
    public void onLaunch_shouldDisplayValues() {
        List<ConversionRate> mockRates = getMockRates();
        Mockito.when(mRateProvider.getConversionRates()).thenReturn(mockRates);
        Mockito.when(mRateProvider.getRatesSymbol()).thenReturn(Arrays.asList("CAD", "USD", "GBP"));
        activityRule.launchActivity(new Intent());

        onView(withId(R.id.base_value_edit)).check(matches(withText("0")));
        onView(withId(R.id.base_currency_spinner)).check(matches(withSpinnerText(containsString("CAD"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText(containsString("CAD")))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText(containsString("USD")))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText(containsString("GBP")))));
    }

    private List<ConversionRate> getMockRates() {
        List<ConversionRate> mockRates = new ArrayList<ConversionRate>();
        mockRates.add(new ConversionRate("CAD", BigDecimal.valueOf(1.0)));
        mockRates.add(new ConversionRate("USD", BigDecimal.valueOf(1.5)));
        mockRates.add(new ConversionRate("GBP", BigDecimal.valueOf(2.0)));
        return mockRates;
    }
}