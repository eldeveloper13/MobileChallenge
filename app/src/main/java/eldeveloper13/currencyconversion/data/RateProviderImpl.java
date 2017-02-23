package eldeveloper13.currencyconversion.data;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import eldeveloper13.currencyconversion.ConversionRates;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RateProviderImpl implements RateProvider {

    private static final long CACHE_DURATION = 15 * 60 * 1000;
    private Map<String, ConversionRates> mConversionRateMap;
    private FixerService mFixerService;

    @Inject
    public RateProviderImpl(FixerService fixerService) {
        mConversionRateMap = new HashMap<>();
        mFixerService = fixerService;
    }

    @Override
    public Observable<ConversionRates> getConversionRates(String baseCurrency) {
        return Observable.concat(
                getConversionRatesFromCache(baseCurrency),
                getConversionRatesFromNetwork(baseCurrency)
        ).first(new Func1<ConversionRates, Boolean>() {
            @Override
            public Boolean call(ConversionRates conversionRates) {
                return conversionRates != null && !isDataExpired(conversionRates);
            }
        });
    }

    private boolean isDataExpired(ConversionRates conversionRates) {
        long now = Calendar.getInstance().getTimeInMillis();
        long expiredTime = conversionRates.getUpdatedTime().getTime() + CACHE_DURATION;
        return now > expiredTime;
    }

    private Observable<ConversionRates> getConversionRatesFromCache(String baseCurrency) {
        return Observable.just(mConversionRateMap.get(baseCurrency));
    }

    private Observable<ConversionRates> getConversionRatesFromNetwork(String baseCurrency) {
        return mFixerService.getConversionRate(baseCurrency)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ConversionRateResponse,ConversionRates>() {
                    @Override
                    public ConversionRates call(ConversionRateResponse conversionRateResponse) {
                        ConversionRates conversionRates = new ConversionRates(conversionRateResponse.getRates(), new Date());
                        mConversionRateMap.put(conversionRateResponse.getBase(), conversionRates);
                        return conversionRates;
                    }
                });
    }
}
