package br.com.aldebaran.ibattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BatteryPercentage extends BroadcastReceiver {

    static OnEventChangedListener mListener;

    @Override
    public void onReceive (Context context, Intent intent) {
        int level = intent.getIntExtra("level", 0);
        Log.i("Script", "Bateria: " + level + "%");

        if(mListener != null) {
            mListener.onPercentageChanged();
        }
    }

    public static void setOnOnEventChangedListener(OnEventChangedListener listener) {
        mListener = listener;
    }
}
