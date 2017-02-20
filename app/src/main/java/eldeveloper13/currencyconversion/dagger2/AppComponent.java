package eldeveloper13.currencyconversion.dagger2;

import javax.inject.Singleton;

import dagger.Component;
import eldeveloper13.currencyconversion.MainActivity;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity activity);

}
