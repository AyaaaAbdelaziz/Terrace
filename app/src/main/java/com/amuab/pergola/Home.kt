package com.amuab.pergola

import android.content.Intent
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.register.*

class Home: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var Uname: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        val button = findViewById<Button>(R.id.btnplant)
        val log_btn = findViewById<Button>(R.id.logout_btn)
alarm_btn.setOnClickListener {
    val intent = Intent(this,Alarm::class.java)
    startActivity(intent)
}

        prof_btn.setOnClickListener {
            val intent = Intent(this,profile::class.java)
            startActivity(intent)
        }
        button?.setOnClickListener()
        {
           val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            }

       // val textView: TextView = findViewById(R.id.wel_name) as TextView

        //  val uid=auth.currentUser?.uid
        log_btn.setOnClickListener {
            signOut()
            startActivity(Intent(this, Login::class.java))

        }
        }


    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        finish()

    }
}