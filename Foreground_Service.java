package com.example.safeguard;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class Foreground_Service extends Service {
    private BroadcastReceiver volumeButtonReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification()); // Start service as foreground to keep it alive

        volumeButtonReceiver = new BroadcastReceiver() {
            private boolean volumeUpPressed = false;
            private boolean volumeDownPressed = false;

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                    int volumeStreamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                    if (volumeStreamType == 3) { // Volume stream type 3 corresponds to media volume
                        int currentVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
                        int previousVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", -1);
                        if (currentVolume > previousVolume) {
                            volumeUpPressed = true;
                        } else if (currentVolume < previousVolume) {
                            volumeDownPressed = true;
                        }

                        // Check if both volume up and volume down buttons are pressed
                        if (volumeUpPressed && volumeDownPressed) {
                            launchActivity();
                            volumeUpPressed = false; // Reset flag
                            volumeDownPressed = false; // Reset flag
                        }
                    }
                }
            }
        };

        registerReceiver(volumeButtonReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(volumeButtonReceiver);
    }

    private void launchActivity() {
        Intent intent = new Intent(this, Login_Activity.class); // Replace Login_Activity with your desired activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
