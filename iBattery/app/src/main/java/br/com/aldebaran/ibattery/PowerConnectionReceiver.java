package br.com.aldebaran.ibattery;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class PowerConnectionReceiver extends BroadcastReceiver {

    static OnEventChangedListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        int connected = intent.getIntExtra(intent.ACTION_POWER_CONNECTED, 1);
        int disconnected = intent.getIntExtra(intent.ACTION_POWER_DISCONNECTED, 1);

        if (connected == 1)
            Log.i("Script", "PowerConnectionReceiver: (connected)" + connected);
        else
            Log.i("Script", "PowerConnectionReceiver: (disconnected)" + disconnected);

        if(mListener != null) {
            mListener.onEventChanged();
        }
    }

    public static void setOnOnEventChangedListener(OnEventChangedListener listener) {
        mListener = listener;
    }
}
// <!--"@string/teste_negative"-->