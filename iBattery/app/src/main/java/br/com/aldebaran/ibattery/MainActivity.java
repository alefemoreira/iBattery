package br.com.aldebaran.ibattery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnEventChangedListener {
    public Switch alarme;
    public ImageView connectedBackground, moldBattery, batteryFill;
    public TextView test, percentage, batteryText, toAlarm, connected;
    public boolean isAlarme = true;

    private PowerConnectionReceiver pcr = new PowerConnectionReceiver();
    private BatteryPercentageReceiver bp = new BatteryPercentageReceiver();
    private File iBatteryJson;

    Locale locale = Locale.getDefault();

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

        iBatteryJson = new File(getExternalFilesDir(null), "iBattery.json");

        setPercentageText();
        setConnectedText();

        registerReceiver(pcr, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(pcr, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        registerReceiver(bp, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (iBatteryJson.exists()) {
            Log.i("Script", "File Exist");
        } else {
            Log.i("Script", "File Not Exist");
            try {
                Log.i("Script", "Creating " + iBatteryJson.getName());

                iBatteryJson.createNewFile();

                Log.i("Script", "The file \"" + iBatteryJson.getName() +
                        "\" has been created with success");
            } catch (IOException io){
                io.printStackTrace();
                Log.i("Script", "Fail on Creation of \"" + iBatteryJson.getName() + "\"");
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pcr);

        PowerConnectionReceiver.setOnOnEventChangedListener(null);
        BatteryPercentageReceiver.setOnOnEventChangedListener(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(pcr);
        // É legal tirar a referência quando a Activity sair do topo do TaskStack.
        BatteryPercentageReceiver.setOnOnEventChangedListener(null);
        PowerConnectionReceiver.setOnOnEventChangedListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(pcr, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        // Recolocando quando ela vier a ser o topo do TaskStack.
        PowerConnectionReceiver.setOnOnEventChangedListener(this);
        BatteryPercentageReceiver.setOnOnEventChangedListener(this);

        setPercentageText();
        setConnectedText();
    }

    @Override
    public void onConnectionChanged() {
        setConnectedText();
    }

    @Override
    public void onPercentageChanged() { setPercentageText(); }

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

    public float percentalize(float percentage){
        return percentage * 100;
    }

    private int toRemovePoint(float percentalized) {
        return Math.round(percentalized);
    }

    public String toStantardPercentage(float percentage) {
        float percentalized = percentalize(percentage);
        int noPoint = toRemovePoint(percentalized);
        return String.format(this.locale,"%d%%", noPoint);
    }

    public void setPercentageText(){
        float batteryPercentage = getBatteryLevel();
        String percentageStardalized = toStantardPercentage(batteryPercentage);
        this.percentage.setText(percentageStardalized);
    }

    public void setIsAlarme(View view){
        this.isAlarme = this.alarme.isChecked();
    }

    public  void setConnectedText(){
        if (isCharging()){
            this.test.setText(R.string.teste_positive);
            this.connectedBackground.setBackgroundResource(R.drawable.ic_bgconnectedgreen);
        } else {
            this.test.setText(R.string.teste_negative);
            this.connectedBackground.setBackgroundResource(R.drawable.ic_bgconnectedred);
        }
    }

    public boolean isCharging() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL; //Bateria cheia

        return isCharging;
    }

    public void verifyAlarmState(){
        try {
            String str = "\"isAlarm\": true,";
            BufferedWriter writer = new BufferedWriter(new FileWriter(iBatteryJson));
            writer.write(str);

            Log.i("Script", "Written Did " + str);

            writer.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void writeAlarmState(){
        try {
            String str = "\"isAlarm\": true,";
            BufferedWriter writer = new BufferedWriter(new FileWriter(iBatteryJson));
            writer.write(str);

            Log.i("Script", "Written Did " + str);

            writer.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
