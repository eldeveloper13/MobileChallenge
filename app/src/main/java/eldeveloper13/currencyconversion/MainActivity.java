package eldeveloper13.currencyconversion;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import eldeveloper13.currencyconversion.data.CurrencyCodeProvider;
import eldeveloper13.currencyconversion.data.RateProvider;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main)
    View mTopview;

    @BindView(R.id.loading_spinner)
    ProgressBar mLoadingSpinner;

    @BindView(R.id.base_value_edit)
    EditText mBaseValueEdit;

    @BindView(R.id.timestamp_textview)
    TextView mTimeStampTextView;

    @BindView(R.id.base_currency_spinner)
    Spinner mBaseCurrencySpinner;

    @BindView(R.id.converted_currencies_recycler_view)
    RecyclerView mConvertedCurrenciesRecyclerView;

    @Inject
    RateProvider mRateProvider;

    @Inject
    CurrencyCodeProvider mCurrencyCodeProvider;

    private ConvertedCurrencyAdapter mConvertedAdapter;
    private ArrayAdapter mCurrencyCodeAdapter;
    private BigDecimal mBaseValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((CurrencyConversionApplication) getApplication()).getAppComponent().inject(this);

        mCurrencyCodeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mCurrencyCodeProvider.getCurrencyCodes());
        mBaseCurrencySpinner.setAdapter(mCurrencyCodeAdapter);

        if (savedInstanceState != null) {
            mBaseValueEdit.setText(savedInstanceState.getString(Extra.BASE_VALUE));
            int currencySelectedPosition = savedInstanceState.getInt(Extra.CURRENCY);
            mBaseCurrencySpinner.setSelection(currencySelectedPosition);
        } else {
            mBaseValue = BigDecimal.ZERO;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        mConvertedCurrenciesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Extra.BASE_VALUE, mBaseValueEdit.getText().toString());
        outState.putInt(Extra.CURRENCY, mBaseCurrencySpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @OnTextChanged(value = R.id.base_value_edit, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onBaseValueChanged(CharSequence s) {
        try {
            mBaseValue = new BigDecimal(s.toString());
        } catch (NumberFormatException e) {
            // Ignore format error
        }
        updateBaseValue(mBaseValue);
    }

    @OnItemSelected(R.id.base_currency_spinner)
    void onCurrencySelected(AdapterView<?> parent, View view, int position, long id) {
        updateConversionRate(mCurrencyCodeAdapter.getItem(position).toString());
    }

    private void updateConversionRate(final String currencyCode) {
        mLoadingSpinner.setVisibility(View.VISIBLE);
        mRateProvider.getConversionRates(currencyCode).subscribe(new Subscriber<ConversionRates>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mLoadingSpinner.setVisibility(View.GONE);
                Snackbar.make(mTopview, String.format("Error getting conversion rate for %s", currencyCode), Snackbar.LENGTH_LONG).show();
                Log.d(this.getClass().getName(), e.getMessage());
            }

            @Override
            public void onNext(ConversionRates conversionRates) {
                mLoadingSpinner.setVisibility(View.GONE);
                if (conversionRates == null) {
                    Snackbar.make(mTopview, String.format("Cannot find conversion rate for %s", currencyCode), Snackbar.LENGTH_LONG).show();
                } else {
                    if (mConvertedAdapter == null) {
                        mConvertedAdapter = new ConvertedCurrencyAdapter(conversionRates, mBaseValue);
                        mConvertedCurrenciesRecyclerView.setAdapter(mConvertedAdapter);
                    } else {
                        mConvertedAdapter.updateConversion(conversionRates);
                    }
                    mTimeStampTextView.setText(String.format("Last Updated: %1$tD %1$tr", conversionRates.getUpdatedTime()));
                }
            }
        });
    }

    private void updateBaseValue(BigDecimal value) {
        if (mConvertedAdapter != null) {
            mConvertedAdapter.updateBaseValue(value);
        }
    }

    private static final class Extra {
        private static final String BASE_VALUE = "base_value";
        private static final String CURRENCY = "currency";
    }
}
