package info.drakoc.provider;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import info.drakoc.shared.Executor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Fetch {

    public Fetch() {
    }

    public static void fetchDateTime(DateTimeCallback callback) {
        ExecutorService executorService = Executor.getExecutorService();
        executorService.execute(() -> {
            String API_URL = ProviderApplication.getInstance().getResources().getString(R.string.time_api_url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(API_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    String dateTime = jsonObject.get("datetime").getAsString();
                    callback.onDateTimeFetched(dateTime);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public interface DateTimeCallback {
        void onDateTimeFetched(String dateTime);
    }
}
