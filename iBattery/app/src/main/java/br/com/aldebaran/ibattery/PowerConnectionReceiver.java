package br.com.aldebaran.ibattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerConnectionReceiver extends BroadcastReceiver {

    static OnEventChangedListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        int connected = intent.getIntExtra(intent.ACTION_POWER_CONNECTED, 1);
        int disconnected = intent.getIntExtra(intent.ACTION_POWER_DISCONNECTED, 1);

        if(mListener != null) {
            mListener.onConnectionChanged();
        }
    }

    public static void setOnOnEventChangedListener(OnEventChangedListener listener) {
        mListener = listener;
    }
}