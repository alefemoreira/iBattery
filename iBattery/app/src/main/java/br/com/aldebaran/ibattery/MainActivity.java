package br.com.aldebaran.ibattery;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

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


        registerReceiver(pcr, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(pcr, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        registerReceiver(bp, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (!iBatteryJson.exists()) {
            try {
                iBatteryJson.createNewFile();
                writeAlarmState(true);
            } catch (IOException io){
                io.printStackTrace();
            }
        }

        alarme.setChecked(verifyAlarmState());

        setPercentageText();
        setConnectedText();

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
    public void onConnectionChanged() { setConnectedText(); }

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
        writeAlarmState(this.isAlarme);
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

    public boolean verifyAlarmState(){
        JSONObject iBattery = readFileJSON(iBatteryJson);
        try {
            Log.i("Script", iBattery.get("isAlarm").toString());
            return Boolean.parseBoolean(iBattery.get("isAlarm").toString());
        } catch (JSONException json) {
            json.printStackTrace();
            return false;
        }
    }

    public void writeAlarmState(boolean state){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(iBatteryJson));
            writer.write("{\"isAlarm\":"+ state +"}");
            writer.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public JSONObject readFileJSON(File file) {
        String contentOfFile = "";

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) contentOfFile += scanner.next();

        } catch (IOException io) {
            io.printStackTrace();
        }

        try {
            return new JSONObject(contentOfFile);
        } catch (JSONException json){
            json.printStackTrace();
            return new JSONObject();
        }
    }
}
