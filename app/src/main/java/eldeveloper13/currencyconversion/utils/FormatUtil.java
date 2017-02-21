package eldeveloper13.currencyconversion.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Currency;

public class FormatUtil {

    public Format getCurrencyFormat(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        if (format instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) format;
            decimalFormat.setCurrency(currency);
            DecimalFormatSymbols dfs = decimalFormat.getDecimalFormatSymbols();
            dfs.setCurrency(currency);
            decimalFormat.setDecimalFormatSymbols(dfs);
        }
        return format;
    }
}
