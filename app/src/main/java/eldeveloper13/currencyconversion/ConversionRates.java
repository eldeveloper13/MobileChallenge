package eldeveloper13.currencyconversion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ConversionRates {

    private Map<String, BigDecimal> conversionRates = new LinkedHashMap<>();
    private Date updatedTime = new Date();

    public ConversionRates(LinkedHashMap<String, BigDecimal> rates, Date updatedTime) {
        this.conversionRates = rates;
        this.updatedTime = updatedTime;
    }

    public void addConversionRate(String code, BigDecimal rate) {
        conversionRates.put(code, rate);
    }
}
