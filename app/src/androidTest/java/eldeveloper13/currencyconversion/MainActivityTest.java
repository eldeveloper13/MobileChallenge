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
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import eldeveloper13.currencyconversion.dagger2.AppComponent;
import eldeveloper13.currencyconversion.data.CurrencyCodeProvider;
import eldeveloper13.currencyconversion.data.RateProvider;
import rx.Observable;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Inject
    RateProvider mRateProvider;

    @Inject
    CurrencyCodeProvider mCurrencyCodeProvider;

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

        ConversionRates mockCADRates = getMockCADRates();
        Mockito.when(mRateProvider.getConversionRates("CAD")).thenReturn(Observable.just(mockCADRates));
        ConversionRates mockUSDRates = getMockUSDRates();
        Mockito.when(mRateProvider.getConversionRates("USD")).thenReturn(Observable.just(mockUSDRates));
        Mockito.when(mCurrencyCodeProvider.getCurrencyCodes()).thenReturn(Arrays.asList("CAD", "USD", "GBP"));
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void onLaunch_shouldDisplayValues() {
        onView(withId(R.id.base_value_edit)).check(matches(withText("0")));
        onView(withId(R.id.base_currency_spinner)).check(matches(withSpinnerText(containsString("CAD"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("CAD"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("USD"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("GBP"))));
    }

    @Test
    public void onEditBaseValue_shouldUpdateDisplayValues() {
        onView(withId(R.id.base_value_edit)).perform(replaceText("100.00"));
        onView(withId(R.id.base_currency_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("CAD"))).perform(click());

        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("CA$100.00"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("$150.00"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("£200.00"))));
    }

    @Test
    public void onEditBaseValue_setToEmpty_shouldDisplayZeroes() {
        onView(withId(R.id.base_value_edit)).perform(replaceText(""));

        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("CA$0.00"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("$0.00"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("£0.00"))));
    }

    @Test
    public void enterBaseValue_thenSwitchCurrency_shouldUpdateDisplayValues() {
        onView(withId(R.id.base_value_edit)).perform(replaceText("100.00"));
        onView(withId(R.id.base_currency_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("USD"))).perform(click());

        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("CA$66.00"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("$100.00"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("£133.00"))));
    }

    @Test
    public void onSwitchCurrency_thenEnterBaseValue_shouldDisplayValues() {
        onView(withId(R.id.base_currency_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("USD"))).perform(click());
        onView(withId(R.id.base_value_edit)).perform(replaceText("100.00"));

        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("CA$66.00"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("$100.00"))));
        onView(withId(R.id.converted_currencies_recycler_view)).check(matches(hasDescendant(withText("£133.00"))));
    }
    @Test
    public void onSwitchCurrencyNotSupported_shouldDisplayError() {
        Mockito.when(mRateProvider.getConversionRates("GBP")).thenReturn(Observable.<ConversionRates>just(null));
        onView(withId(R.id.base_currency_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("GBP"))).perform(click());

        onView(withText("Cannot find conversion rate for GBP")).check(matches(isDisplayed()));
    }

    private ConversionRates getMockCADRates() {
        LinkedHashMap<String, BigDecimal> mockRates = new LinkedHashMap<>();
        mockRates.put("CAD", BigDecimal.valueOf(1.0));
        mockRates.put("USD", BigDecimal.valueOf(1.5));
        mockRates.put("GBP", BigDecimal.valueOf(2.0));
        return new ConversionRates(mockRates, new Date());
    }

    private ConversionRates getMockUSDRates() {
        LinkedHashMap<String, BigDecimal> mockRates = new LinkedHashMap<>();
        mockRates.put("CAD", BigDecimal.valueOf(0.66));
        mockRates.put("USD", BigDecimal.valueOf(1.0));
        mockRates.put("GBP", BigDecimal.valueOf(1.33));
        return new ConversionRates(mockRates, new Date());
    }
}