package com.distance_tracker.km.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.distance_tracker.km.BuildConfig;
import com.distance_tracker.km.DistanceTrackerService;
import com.distance_tracker.km.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener, ServiceConnection {
    private static final String TAG = MainActivity.class.getSimpleName();

    Button startTrackingButton, stopTrackingButton;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // A reference to the service used to get location updates.
    private DistanceTrackerService mService = null;

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

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
    protected void onStart() {
        super.onStart();
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, DistanceTrackerService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        DistanceTrackerService.LocalBinder binder = (DistanceTrackerService.LocalBinder) service;
        mService = binder.getService();
        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
        mBound = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDistanceTrackerServiceRunning()) {
            showStartedState();
        } else {
            showStoppedState();
        }
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(this);
            mBound = false;
        }
        super.onStop();
    }

    private boolean isDistanceTrackerServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DistanceTrackerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startTracking() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            mService.requestLocationUpdates();
        }
        showStartedState();
    }

    private void stopTracking() {
        mService.removeLocationUpdates();
        showStoppedState();
    }

    private void showStartedState() {
        startTrackingButton.setVisibility(View.GONE);
        stopTrackingButton.setVisibility(View.VISIBLE);
    }

    private void showStoppedState() {
        startTrackingButton.setVisibility(View.VISIBLE);
        stopTrackingButton.setVisibility(View.GONE);
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
                stopTracking();
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
                startTracking();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
        }
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.main_activity_constraint_layout),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                showStoppedState();
                Snackbar.make(
                        findViewById(R.id.main_activity_constraint_layout),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }
}
