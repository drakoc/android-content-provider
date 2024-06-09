package info.drakoc.consumer;

import android.app.Application;


public class ConsumerApplication extends Application {

    private static ConsumerApplication instance;

    public ConsumerApplication() {
        instance = this;
    }

    public static ConsumerApplication getInstance() {
        return instance;
    }
}
