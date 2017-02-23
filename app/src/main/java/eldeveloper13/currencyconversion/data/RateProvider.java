package eldeveloper13.currencyconversion.data;

import eldeveloper13.currencyconversion.ConversionRates;
import rx.Observable;

public interface RateProvider {

    Observable<ConversionRates> getConversionRates(String baseCurrency);
}
