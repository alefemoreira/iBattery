package br.com.aldebaran.ibattery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
//import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public Switch alarme;
    public ImageView connectedBackground, moldBattery, batteryFill;
    public TextView test, percentage, batteryText, toAlarm, conneted;
    public boolean isAlarme = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.alarme = findViewById(R.id.alarme);
        this.connectedBackground = findViewById(R.id.bgConnected);
        this.moldBattery = findViewById(R.id.batteryMold);
        this.batteryFill = findViewById(R.id.fillBattery);
        this.test = findViewById(R.id.teste);
        this.percentage = findViewById(R.id.percentage);
        alarme.setChecked(isAlarme);

    }

    public float getBatteryLevel(){
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            return level / (float) scale;
        } catch (Exception e){
            System.err.println("Erro ao pegar a % da bateria:" + e);
            return 0F;
        }
    }

    public void setIsAlarme(View view){
        this.isAlarme = this.alarme.isChecked();
        if (isAlarme) {
            this.test.setText(R.string.teste_positive);
            this.connectedBackground.setBackgroundResource(R.drawable.ic_bgconnectedgreen);
        } else {
            this.test.setText(R.string.teste_negative);
            this.connectedBackground.setBackgroundResource(R.drawable.ic_bgconnectedred);
            float bPercentage = getBatteryLevel() * 100;
            Locale locale = Locale.getDefault();
            this.percentage.setText(String.format(locale,"%d %%", Math.round(bPercentage)));

        }
    }
}
