package eldeveloper13.currencyconversion.data;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import eldeveloper13.currencyconversion.R;

public class CurrencyCodeProvider {

    List<String> mCurrencyCodes;

    @Inject
    public CurrencyCodeProvider(Context context) {
        mCurrencyCodes = Arrays.asList(context.getResources().getStringArray(R.array.currency_code));
    }

    public List<String> getCurrencyCodes() {
        return mCurrencyCodes;
    }
}
