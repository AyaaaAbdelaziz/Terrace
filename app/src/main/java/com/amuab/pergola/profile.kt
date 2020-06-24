package com.amuab.pergola


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile.*
import android.widget.ListView
import androidx.core.app.NotificationManagerCompat
import com.amuab.pergola.notify.NotificationHelper

@SuppressLint("ByteOrderMark")
class profile  : AppCompatActivity() {
    private lateinit var mDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var Uname: String = ""
    private lateinit var PDatabase: DatabaseReference
    lateinit var plantslist: MutableList<Plants>
    lateinit var watertime: MutableList<Plants>
    lateinit var listview: ListView
lateinit var context :Context
    lateinit var alarmManager: AlarmManager
  lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
lateinit var builder:Notification.Builder
    private  val channelId="com.amuab.pergola"
    private val description ="Water ur plant"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

      //  val relativeLayout: RelativeLayout by bindView(R.id.activity_main)
        var notificationId = 0
       // val mUserName = findViewById<TextView>(R.id.wel_name)
       // val addbutn = findViewById<Button>(R.id.add_btn)
        listview = findViewById(R.id.listView)
        plantslist = mutableListOf()

        //watertime = mutableListOf()
//////////////Alarm////////////////////////////////////

//////////////////////Noticiation///////////////////////
add_new_plant.setOnClickListener {
    val intent = Intent(this,Addplant::class.java)
    startActivity(intent)
}
       NotificationHelper.createNotificationChannel(this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")

        NotificationHelper.createSampleDataNotification(this@profile,
            "pergola",
            "water ur plant",
            "ur plant 3tshanaaaa", false)




        //scheduleNotification(2, "hi", "a5eraaaaaaaaaaan")
        auth = FirebaseAuth.getInstance()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid)
        PDatabase = FirebaseDatabase.getInstance().getReference("plants")

       /* mDatabase.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(data: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val name = data?.child("username").value!!.toString().trim()
                mUserName.setText("Welcome ${name}")

            }

            override fun onCancelled(dataError: DatabaseError) {
                Toast.makeText(
                    baseContext, "Welcome ${dataError.toException()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })*/

        ///////////////////////read plants//////////////////////////////
         PDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if (data!!.exists()) {
                    plantslist.clear()
                        for (h in data.children) {

                            val pdata = h.getValue(Plants::class.java)
if(pdata?.userId==auth.uid) {
    plantslist.add(pdata!!)
    //val water=pdata?.pwater

}


                        }
                 //   scheduleNotification(pdata.pwater, "برجولا", "2s2y el ${pdata.pname}")

                        Toast.makeText(
                            baseContext, " ${plantslist.last()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val adapter =
                            PlantAdapter(applicationContext, R.layout.plants, plantslist)
                        listview.adapter = adapter

                }

                //   getPlantData.setText("plant name:${plantslist[1]}")
            }

            override fun onCancelled(dataError: DatabaseError) {
                Log.d("plant data", "failed", dataError.toException())
                Toast.makeText(
                    baseContext, " ${dataError.toException()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })}




}


