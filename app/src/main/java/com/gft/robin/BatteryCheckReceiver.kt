package com.gft.robin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.BatteryManager
import android.util.Log


class BatteryCheckReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context!!.applicationContext.registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }
    private val mBatInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)!!
            val mMediaPlayer = MediaPlayer()
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (level == 100) {
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
                        call(context)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun call(context: Context?){
        context!!.applicationContext.unregisterReceiver(mBatInfoReceiver)
    }
}

class shakeBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val sManager = context!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sManager.registerListener(ShakeEventListener(), sensor, SensorManager.SENSOR_DELAY_NORMAL)

    }

}
class ShakeEventListener : SensorEventListener {

    val threshold = 1000
    var last_y = 0f
    var last_z = 0f
    var last_x = 0f
    var lastUpdate = 0L

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
            handleShake(event)
    }
    fun handleShake(event: SensorEvent){
        val curTime = System.currentTimeMillis()

        if (curTime - lastUpdate > 100) {
            val diffTime = curTime - lastUpdate
            lastUpdate = curTime
            val x = event.values[SensorManager.DATA_X]
            val y = event.values[SensorManager.DATA_Y]
            val z = event.values[SensorManager.DATA_Z]

            val speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000

            if (speed > threshold) {
               Log.d("hey","printed")
            }
            last_x = x
            last_y = y
            last_z = z
        }
        }
 }


