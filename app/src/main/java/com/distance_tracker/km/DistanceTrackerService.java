package com.distance_tracker.km;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class DistanceTrackerService extends Service {
    private final String TAG = this.getClass().getName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "created");
        Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "starting");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "done");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
