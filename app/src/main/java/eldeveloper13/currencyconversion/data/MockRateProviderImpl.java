package eldeveloper13.currencyconversion.data;

import android.content.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import eldeveloper13.currencyconversion.ConversionRate;
import eldeveloper13.currencyconversion.R;

public class MockRateProviderImpl implements RateProvider {

    String[] mCurrencyNames;

    @Inject
    public MockRateProviderImpl(Context context) {
        mCurrencyNames = context.getResources().getStringArray(R.array.currency_names);
    }
    @Override
    //TODO: Return real data
    public List<ConversionRate> getConversionRates() {
        List<ConversionRate> rates = new ArrayList<>();
        for (String symbol : mCurrencyNames) {
            rates.add(new ConversionRate(symbol, BigDecimal.ONE));
        }
        return rates;
    }

    @Override
    public List<String> getRatesSymbol() {
        return Arrays.asList(mCurrencyNames);
    }
}
