package com.gft.robin;

import android.app.LauncherActivity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Uri notification;
    Ringtone r;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }
        });
    }

    BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            if(level == 100) {
               try {
                   notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                   r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
                   r.play();
                   PackageManager pm = context.getPackageManager();
                   Intent launchIntent = pm.getLaunchIntentForPackage("com.gft.robin");
                   context.startActivity(launchIntent);
                   context.getApplicationContext().unregisterReceiver(mBatInfoReceiver);
                } catch (Exception e ){
                    e.printStackTrace();
                }

            }
        }

    };

}
