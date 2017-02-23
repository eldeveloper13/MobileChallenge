package eldeveloper13.currencyconversion.data;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import lombok.Data;

@Data
public class ConversionRateResponse {

    private String base;
    private LinkedHashMap<String, BigDecimal> rates;
}