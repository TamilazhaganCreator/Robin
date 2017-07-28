package com.gft.robin

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.BatteryManager
import android.os.IBinder
import android.widget.Toast


class BatteryService : Service() {
    val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)!!
    val mMediaPlayer = MediaPlayer()
    override fun onCreate() {
        super.onCreate()
        registerReceiver(mBatInfoReceiver,IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        Toast.makeText(this,"creating",Toast.LENGTH_SHORT).show()
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private val mBatInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if ( level == 100) {
                try {
                   mMediaPlayer.setDataSource(context, alert)
                    if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING)
                        mMediaPlayer.isLooping = true
                        mMediaPlayer.prepare()
                        mMediaPlayer.start()
                        val pm = context.packageManager
                        val launchIntent = pm.getLaunchIntentForPackage("com.gft.robin")
                        context.startActivity(launchIntent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBatInfoReceiver)
        mMediaPlayer.stop()
    }
}