package br.com.aldebaran.ibattery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        alarme.setChecked(isAlarme);

//        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = context.registerReceiver(null, ifilter);
//        System.out.println(batteryStatus);
    }
    public void setIsAlarme(View view){
        this.isAlarme = this.alarme.isChecked();
        final Resources.Theme theme = getResources().newTheme();

        if (isAlarme == false) {
            this.test.setText("No");
            this.connectedBackground.setBackgroundResource(R.drawable.ic_bgconnectedred);

        } else{
            this.test.setText("Yes");
            this.connectedBackground.setBackgroundResource(R.drawable.ic_bgconnectedgreen);
        }
    }
}
