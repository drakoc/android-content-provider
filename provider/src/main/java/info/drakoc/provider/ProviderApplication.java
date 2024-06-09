package info.drakoc.provider;

import android.app.Application;


public class ProviderApplication extends Application {

    private static ProviderApplication instance;

    public ProviderApplication() {
        instance = this;
    }

    public static ProviderApplication getInstance() {
        return instance;
    }
}
