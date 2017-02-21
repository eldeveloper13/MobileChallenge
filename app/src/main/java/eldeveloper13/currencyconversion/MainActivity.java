package eldeveloper13.currencyconversion;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import eldeveloper13.currencyconversion.data.RateProvider;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main)
    View mTopview;

    @BindView(R.id.base_currency_spinner)
    Spinner mBaseCurrencySpinner;

    @BindView(R.id.converted_currencies_recycler_view)
    RecyclerView mConvertedCurrenciesRecyclerView;

    @Inject
    RateProvider mRateProvider;

    private ConvertedCurrencyAdapter mConvertedAdapter;
    private ArrayAdapter mCurrencyCodeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((CurrencyConversionApplication)getApplication()).getAppComponent().inject(this);

        mCurrencyCodeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mRateProvider.getRatesSymbol());
        mBaseCurrencySpinner.setAdapter(mCurrencyCodeAdapter);

        mConvertedAdapter = new ConvertedCurrencyAdapter(mRateProvider.getConversionRates(mCurrencyCodeAdapter.getItem(0).toString()));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mConvertedCurrenciesRecyclerView.setAdapter(mConvertedAdapter);
        mConvertedCurrenciesRecyclerView.setLayoutManager(layoutManager);
    }

    @OnTextChanged(value = R.id.base_value_edit, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onBaseValueChanged(CharSequence s) {
        BigDecimal newValue = BigDecimal.ZERO;
        try {
            newValue = new BigDecimal(s.toString());
        } catch (NumberFormatException e) {
            // Ignore format error
        }
        updateBaseValue(newValue);
    }

    @OnItemSelected(R.id.base_currency_spinner)
    void onCurrencySelected(AdapterView<?> parent, View view, int position, long id){
        updateConversionRate(mCurrencyCodeAdapter.getItem(position).toString());
    }

    private void updateConversionRate(String currencyCode) {
        List<ConversionRate> conversionRates = mRateProvider.getConversionRates(currencyCode);
        if (conversionRates == null) {
            Snackbar.make(mTopview, String.format("Cannot find conversion rate for %s", currencyCode), Snackbar.LENGTH_LONG).show();
        } else {
            mConvertedAdapter.updateConversion(conversionRates);
        }
    }
    private void updateBaseValue(BigDecimal value) {
        mConvertedAdapter.updateBaseValue(value);
    }
}
