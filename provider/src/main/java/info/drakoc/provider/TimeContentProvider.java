package info.drakoc.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TimeContentProvider extends ContentProvider {
    private static final String AUTHORITY = "info.drakoc.provider.time";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int DATA = 1;
    private ExecutorService executorService;

    static {
        uriMatcher.addURI(AUTHORITY, "data", DATA);
    }

    @Override
    public boolean onCreate() {
        executorService = Executors.newSingleThreadExecutor();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (uriMatcher.match(uri) == DATA) {

            final MatrixCursor cursor = new MatrixCursor(new String[]{"datetime"});
            final CountDownLatch latch = new CountDownLatch(1);

            fetchDateTimeFromApi(dateTime -> {
                cursor.addRow(new Object[]{dateTime});
                latch.countDown();
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private void fetchDateTimeFromApi(DateTimeCallback callback) {
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

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private interface DateTimeCallback {
        void onDateTimeFetched(String dateTime);
    }
}

