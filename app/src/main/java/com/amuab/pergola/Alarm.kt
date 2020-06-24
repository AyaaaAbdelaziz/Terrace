package com.amuab.pergola

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.alarm.*
import java.util.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class Alarm: AppCompatActivity() {
    lateinit var buttonCancel:Button
    lateinit var editText:TextView
    lateinit var buttonSet:Button
lateinit var alarmManager: AlarmManager
    var notificationId = 0
    var alarmId=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm)

        editText= findViewById(R.id.edit_text)
        // timePicker= findViewById(R.id.time_picker)
         buttonSet= findViewById(R.id.button_set)
         buttonCancel= findViewById(R.id.button_cancel)


         alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        buttonSet.setOnClickListener {
             // reset()
            if (editText.text.isBlank()) {
                Toast.makeText(applicationContext, "Title is Required!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setAlarm()
                  }

        buttonCancel.setOnClickListener {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    applicationContext, 0, Intent(applicationContext, AlarmReceiver::class.java), 0))
            Toast.makeText(applicationContext, "CANCEL!!", Toast.LENGTH_SHORT).show()
        }
    }

  /*  override fun onTouchEvent(event: MotionEvent?): Boolean {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        .requestFocus()
        return super.onTouchEvent(event)
    }*/
 fun setAlarm(){

      //  val alarmId = editText.text.toString().toInt()
      val myDate = Calendar.getInstance() // set this up however you need it.
      val dow = myDate.get(Calendar.DAY_OF_WEEK)
      val tom= dow +1

      alarmManager.setRepeating(
          AlarmManager.RTC_WAKEUP,
          Calendar.getInstance().apply {
             // set(Calendar.DAY_OF_WEEK,tom)
              set(Calendar.HOUR_OF_DAY,3)
              set(Calendar.MINUTE, 10)

              set(Calendar.SECOND, 0)
          }.timeInMillis,
          24*60*60*1000,
          PendingIntent.getBroadcast(
              applicationContext,
              ++alarmId,
              Intent(applicationContext, AlarmReceiver::class.java).apply {
                  putExtra("notificationId", ++notificationId)
                  putExtra("reminder", editText.text)
              },
              PendingIntent.FLAG_CANCEL_CURRENT
          )
      )
      Toast.makeText(applicationContext, "SET!! ${tom}", Toast.LENGTH_SHORT)
          .show()

  }
  /*  override fun onResume() {
        super.onResume()
        reset()
    }*/

  /*  private fun reset() {
        timePicker.apply {
            val now = Calendar.getInstance()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = now.get(Calendar.HOUR_OF_DAY)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                minute = now.get(Calendar.MINUTE)
            }
        }
        editText.setText("")
    }*/
    }
