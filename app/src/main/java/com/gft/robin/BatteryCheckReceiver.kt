package com.gft.robin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.BatteryManager
import android.widget.Toast


class BatteryCheckReceiver: BroadcastReceiver() {
    val main = MainActivity()
    override fun onReceive(context: Context?, intent: Intent?) {
        context!!.applicationContext.registerReceiver(main.mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private val mBatInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            if (level == 100) {
                try {
                    Toast.makeText(context, "HEY", Toast.LENGTH_SHORT).show()
                    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    val r = RingtoneManager.getRingtone(context.applicationContext, notification)
                    r.play()
                    //val pm = context.packageManager
                    //val launchIntent = pm.getLaunchIntentForPackage("com.gft.robin")
                    // context.startActivity(launchIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
