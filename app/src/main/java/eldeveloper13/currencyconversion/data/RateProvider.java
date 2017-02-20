package eldeveloper13.currencyconversion.data;

import java.util.List;

import eldeveloper13.currencyconversion.ConversionRate;

public interface RateProvider {

    List<ConversionRate> getConversionRates();

    List<String> getRatesSymbol();
}
