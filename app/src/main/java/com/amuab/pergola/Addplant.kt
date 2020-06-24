package com.amuab.pergola

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.add_plant.*
import java.util.*

class Addplant:AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
   lateinit var alarmManager: AlarmManager
    var notificationId = 0
    var alarmId=0
    lateinit var watertime: MutableList<Plants>

    lateinit var context :Context
    lateinit var Pnme:EditText
    lateinit var daysText:EditText
    lateinit var hourText:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_plant)
        auth = FirebaseAuth.getInstance()

        Pnme= findViewById(R.id.PName)
        daysText= findViewById(R.id.days)
       hourText= findViewById(R.id.hourText)

        val addbutn = findViewById<Button>(R.id.add_btn)
      //  listview = findViewById(R.id.listView)
    //    plantslist = mutableListOf()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        watertime = mutableListOf()
        PImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

        addbutn.setOnClickListener { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
            upladImagetofirestorage()
            if (Pnme.text.isBlank()) {
                Toast.makeText(applicationContext, "Title is Required!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
           setAlarm(daysText.getText().toString().toInt(),hourText.getText().toString().toInt())

        }
}

    var selectedphoto: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedphoto = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedphoto)
            imgView.setImageBitmap(bitmap)
            PImg.alpha = 0f
            //  val bitmapDrawable=BitmapDrawable(bitmap)
            // PImg.setBackgroundDrawable(bitmapDrawable)
        }}
        private fun upladImagetofirestorage() {
            if (selectedphoto == null) return
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(selectedphoto!!)
                .addOnSuccessListener {
                    Log.d("add", "succ ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Toast.makeText(
                            baseContext, "saved",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("add", "File loc$it")
                        SavePlantsToFirebase(it.toString())

                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        baseContext, "not saved",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
// add plant

        private fun SavePlantsToFirebase(Pimage: String) {


            val ref = FirebaseDatabase.getInstance().getReference("/plants")
            val Pname = PName.text.toString()
            val Pwater: Int = days.text.toString().toInt()
            //  val plants = Plants(Pname,Pwater,Pimage)
            val plantId = UUID.randomUUID().toString()

            val values = HashMap<String, Any>()

            values.put("userId", auth.uid.toString())
            values.put("pname", Pname)
            values.put("pwater", Pwater)
            values.put("pimg", Pimage)

            ref.child(plantId).setValue(values)
                .addOnSuccessListener {
                    Toast.makeText(
                        baseContext, "Doone.",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        baseContext, " Not stored.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }


        private fun add() {
            if (PName.toString().isEmpty()) {
                PName.error = "من فضلك ادخل اسمك"
                PName.requestFocus()
                return
            }
            if (days.text.toString().isEmpty()) {
                days.error = "من فضلك ادخل ايميل"
                days.requestFocus()
                return
            }
            upladImagetofirestorage()

        }
    fun setAlarm( value:Int,hour:Int){

        //  val alarmId = editText.text.toString().toInt()
        val myDate = Calendar.getInstance() // set this up however you need it.
        val dow = myDate.get(Calendar.DAY_OF_WEEK)
        val tom= dow +value

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK,tom)
                set(Calendar.HOUR_OF_DAY,hour)
                set(Calendar.MINUTE, 0)

                set(Calendar.SECOND, 0)
            }.timeInMillis,
            24*60*60*1000,
            PendingIntent.getBroadcast(
                applicationContext,
                ++alarmId,
                Intent(applicationContext, AlarmReceiver::class.java).apply {
                    putExtra("notificationId", ++notificationId)
                    putExtra("reminder", Pnme.text)
                },
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        )
        Toast.makeText(applicationContext, "SET!! ${tom}", Toast.LENGTH_SHORT)
            .show()

    }

}



