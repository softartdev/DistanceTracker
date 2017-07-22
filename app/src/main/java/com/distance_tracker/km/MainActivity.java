package com.distance_tracker.km;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    Button startTrackingButton, stopTrackingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTrackingButton = (Button) findViewById(R.id.start_tracking_button);
        startTrackingButton.setOnClickListener(this);

        stopTrackingButton = (Button) findViewById(R.id.stop_tracking_button);
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
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.location_tracked_message)
                        .setPositiveButton(android.R.string.ok, this)
                        .setNegativeButton(android.R.string.cancel, this)
                        .create();
                alertDialog.show();
                break;
            case R.id.stop_tracking_button:
                startTrackingButton.setVisibility(View.VISIBLE);
                stopTrackingButton.setVisibility(View.GONE);
                break;
            case R.id.show_distance_button:
                startActivity(new Intent(this, DistanceActivity.class));
                break;
            case R.id.show_location_button:
                startActivity(new Intent(this, LocationActivity.class));
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                startTrackingButton.setVisibility(View.GONE);
                stopTrackingButton.setVisibility(View.VISIBLE);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
        }
    }
}
