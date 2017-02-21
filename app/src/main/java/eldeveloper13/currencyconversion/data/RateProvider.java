package eldeveloper13.currencyconversion.data;

import android.support.annotation.Nullable;

import java.util.List;

import eldeveloper13.currencyconversion.ConversionRate;

public interface RateProvider {

    @Nullable
    List<ConversionRate> getConversionRates(String baseCurrency);

    List<String> getRatesSymbol();
}
