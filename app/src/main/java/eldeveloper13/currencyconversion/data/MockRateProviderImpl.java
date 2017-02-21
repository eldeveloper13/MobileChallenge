package eldeveloper13.currencyconversion.data;

import android.content.Context;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import eldeveloper13.currencyconversion.ConversionRate;
import eldeveloper13.currencyconversion.R;

public class MockRateProviderImpl implements RateProvider {

    Map<String, List<ConversionRate>> mConversionRateMap;
    List<String> mCurrencyCode;

    @Inject
    public MockRateProviderImpl(Context context) {
        mConversionRateMap = new HashMap<>();
        mCurrencyCode = Arrays.asList(context.getResources().getStringArray(R.array.currency_code));
        for (String currencyCode : mCurrencyCode) {
            mConversionRateMap.put(currencyCode, generateFakeConversionRates());
        }
    }
    @Override
    //TODO: Return real data
    @Nullable
    public List<ConversionRate> getConversionRates(String baseCurrency) {
        return mConversionRateMap.get(baseCurrency);
    }

    @Override
    public List<String> getRatesSymbol() {
        return mCurrencyCode;
    }

    private List<ConversionRate> generateFakeConversionRates() {
        List<ConversionRate> rates = new ArrayList<>();
        for (int i = 0; i < mCurrencyCode.size(); i++){
            String symbol = mCurrencyCode.get(i);
            rates.add(new ConversionRate(symbol, BigDecimal.valueOf(i)));
        }
        return rates;
    }
}
