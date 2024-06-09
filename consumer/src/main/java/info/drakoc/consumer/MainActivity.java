package info.drakoc.consumer;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import info.drakoc.shared.Time;


public class MainActivity extends AppCompatActivity {

    private static String CONTENT_URI;
    private TextView vResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vResult = findViewById(R.id.result);
        Button btnRefresh = findViewById(R.id.refresh);
        CONTENT_URI = getString(R.string.content_uri);
        btnRefresh.setOnClickListener(view -> getTimeFromProviderApp());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTimeFromProviderApp();
    }

    private void getTimeFromProviderApp() {
        Uri uri = Uri.parse(CONTENT_URI);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String columnIndex = "datetime";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int datetimeIndex = cursor.getColumnIndex(columnIndex);
                if (datetimeIndex != -1) {
                    String datetime = cursor.getString(datetimeIndex);
                    String time = Time.convertToLocalTime(datetime);
                    vResult.setText(time);
                } else {
                    String column_not_found = getString(R.string.column_not_found_in_cursor, columnIndex);
                    vResult.setText(column_not_found);
                }
            }
            cursor.close();
        } else {
            vResult.setText(R.string.cursor_null_or_empty);
        }
    }
}
