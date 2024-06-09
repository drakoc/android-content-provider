package info.drakoc.shared;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {

    private static final int NUMBER_OF_THREADS = 4;
    private static ExecutorService instance;

    private Executor() {
    }

    public static ExecutorService getExecutorService() {
        if (instance == null) {
            instance = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        }
        return instance;
    }
}

