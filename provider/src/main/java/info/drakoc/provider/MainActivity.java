package info.drakoc.provider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import info.drakoc.shared.Time;


public class MainActivity extends AppCompatActivity {

    private TextView vResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vResult = findViewById(R.id.result);
        Button btnRefresh = findViewById(R.id.btnGetTime);

        btnRefresh.setOnClickListener(view -> fetchTimeFromAPI());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchTimeFromAPI();
    }

    private void setTime(String time) {
        vResult.setText(time);
    }

    private void fetchTimeFromAPI(){
        Fetch.fetchDateTime(dateTime -> {
            String localTime = Time.convertToLocalTime(dateTime);
            runOnUiThread(() -> setTime(localTime));
        });
    }
}