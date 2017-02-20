package eldeveloper13.currencyconversion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eldeveloper13.currencyconversion.data.RateProvider;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.base_currency_spinner)
    Spinner mBaseCurrencySpinner;

    @BindView(R.id.converted_currencies_recycler_view)
    RecyclerView mConvertedCurrenciesRecyclerView;

    @Inject
    RateProvider mRateProvider;

    ConvertedCurrencyAdapter mConvertedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((CurrencyConversionApplication)getApplication()).getAppComponent().inject(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mRateProvider.getRatesSymbol());
        mBaseCurrencySpinner.setAdapter(arrayAdapter);

        mConvertedAdapter = new ConvertedCurrencyAdapter(mRateProvider.getConversionRates());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mConvertedCurrenciesRecyclerView.setAdapter(mConvertedAdapter);
        mConvertedCurrenciesRecyclerView.setLayoutManager(layoutManager);
    }
}
