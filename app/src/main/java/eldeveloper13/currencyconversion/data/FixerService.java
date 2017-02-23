package eldeveloper13.currencyconversion.data;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FixerService {

    @GET("latest")
    Observable<ConversionRateResponse> getConversionRate(@Query("base") String baseCurrency);

}
