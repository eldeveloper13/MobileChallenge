package eldeveloper13.currencyconversion;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConversionRate {

    private String symbol;
    private BigDecimal rate;

    public ConversionRate(String symbol, BigDecimal rate) {
        this.symbol = symbol;
        this.rate = rate;
    }
}
