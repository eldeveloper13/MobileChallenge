package eldeveloper13.currencyconversion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ericl on 2/20/2017.
 */

public class ConvertedCurrencyAdapter extends RecyclerView.Adapter<ConvertedCurrencyAdapter.ConvertedCurrencyViewHolder> {

    private BigDecimal mBaseValue;
    private List<ConversionRate> mConversionRates;

    public ConvertedCurrencyAdapter(List<ConversionRate> conversionRates) {
        mBaseValue = BigDecimal.ZERO;
        mConversionRates = conversionRates;
    }

    @Override
    public ConvertedCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_convert_currency, parent, false);
        return new ConvertedCurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConvertedCurrencyViewHolder holder, int position) {
        ConversionRate unit = mConversionRates.get(position);
        BigDecimal amount = mBaseValue.multiply(unit.getRate());
        holder.mValueText.setText(NumberFormat.getCurrencyInstance().format(amount));
        holder.mCurrencySymbol.setText(unit.getSymbol());
    }

    @Override
    public int getItemCount() {
        return mConversionRates.size();
    }

    public void updateBaseValue(BigDecimal value) {
        mBaseValue = value;
        notifyDataSetChanged();
    }

    public void updateConversion(List<ConversionRate> conversionRates){
        mConversionRates = conversionRates;
        notifyDataSetChanged();
    }

    class ConvertedCurrencyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.currency_symbol_text)
        TextView mCurrencySymbol;

        @BindView(R.id.value_text)
        TextView mValueText;

        public ConvertedCurrencyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
