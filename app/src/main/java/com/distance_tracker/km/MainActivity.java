package com.distance_tracker.km;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startTrackingButton = (Button) findViewById(R.id.start_tracking_button);
        startTrackingButton.setOnClickListener(this);

        Button stopTrackingButton = (Button) findViewById(R.id.stop_tracking_button);
        stopTrackingButton.setOnClickListener(this);

        Button showDistanceButton = (Button) findViewById(R.id.show_distance_button);
        showDistanceButton.setOnClickListener(this);

        Button showLocationButton = (Button) findViewById(R.id.show_location_button);
        showLocationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_tracking_button:
                break;
            case R.id.stop_tracking_button:
                break;
            case R.id.show_distance_button:
                startActivity(new Intent(this, DistanceActivity.class));
                break;
            case R.id.show_location_button:
                break;
        }
    }
}
