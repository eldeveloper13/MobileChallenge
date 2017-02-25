package eldeveloper13.currencyconversion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eldeveloper13.currencyconversion.utils.FormatUtil;

public class ConvertedCurrencyAdapter extends RecyclerView.Adapter<ConvertedCurrencyAdapter.ConvertedCurrencyViewHolder> {

    private BigDecimal mBaseValue;
    private ConversionRates mConversionRates;
    private FormatUtil mFormatUtil = new FormatUtil();

    public ConvertedCurrencyAdapter(ConversionRates conversionRates, BigDecimal baseValue) {
        mBaseValue = baseValue;
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
        List<String> keys = new ArrayList<>(mConversionRates.getConversionRates().keySet());
        String symbol = keys.get(position);
        BigDecimal unit = mConversionRates.getConversionRates().get(symbol);
        BigDecimal amount = mBaseValue.multiply(unit);
        holder.mValueText.setText(mFormatUtil.getCurrencyFormat(symbol).format(amount));
        holder.mCurrencySymbol.setText(symbol);
    }

    @Override
    public int getItemCount() {
        return mConversionRates.getConversionRates().size();
    }

    public void updateBaseValue(BigDecimal value) {
        mBaseValue = value;
        notifyDataSetChanged();
    }

    public void updateConversion(ConversionRates conversionRates){
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
