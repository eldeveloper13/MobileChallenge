package eldeveloper13.currencyconversion.data;

import com.google.gson.Gson;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import eldeveloper13.currencyconversion.ConversionRates;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.assertj.core.api.Assertions.assertThat;


public class RateProviderImplTest {

    RateProvider mSubject;

    @Mock
    FixerService mFixerService;

    @BeforeClass
    public static void setUpClass() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @AfterClass
    public static void tearDownClass() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mSubject = new RateProviderImpl(mFixerService);
    }

    @Test
    public void getConversionRates_shouldReturnCorrectData() throws FileNotFoundException {
        ConversionRateResponse response = getMockResponse();
        Mockito.when(mFixerService.getConversionRate("USD")).thenReturn(Observable.just(response));

        TestSubscriber<ConversionRates> testSubscriber = new TestSubscriber<>();
        mSubject.getConversionRates("USD").subscribe(testSubscriber);

        List<ConversionRates> conversionRatesList = testSubscriber.getOnNextEvents();
        ConversionRates conversionRates = conversionRatesList.get(0);
        assertThat(conversionRates.getConversionRates().size()).isEqualTo(31);
        assertThat(conversionRates.getConversionRates().get("CAD").doubleValue()).isEqualTo(1.311);
    }

    private ConversionRateResponse getMockResponse() throws FileNotFoundException {
        File file = new File(getClass().getResource("/USDRates.json").getPath());
        FileReader reader = new FileReader(file);
        ConversionRateResponse response = new Gson().fromJson(reader, ConversionRateResponse.class);
        return response;
    }
}