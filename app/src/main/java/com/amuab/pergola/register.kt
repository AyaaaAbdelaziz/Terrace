package com.amuab.pergola

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.register.*

class register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "MyActivity"
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.register)
        auth = FirebaseAuth.getInstance()
        sign_btn.setOnClickListener {

            signUp()
        }
    }
private fun signUp(){
        if (name.text.toString().isEmpty()){
            name.error="من فضلك ادخل اسمك"
            name.requestFocus()
            return
        }
        if (email.text.toString().isEmpty()){
            email.error="من فضلك ادخل ايميل"
            email.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error="valid email"
            email.requestFocus()
            return
        }
        if (pass.text.toString().isEmpty()){
            pass.error="من فضلك ادخل pass"
            pass.requestFocus()
            return
        }
    auth.createUserWithEmailAndPassword(email.text.toString(), pass.text.toString())
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user :FirebaseUser?=auth.currentUser
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this,Login::class.java))
                            finish()
                        }
                    }
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
            saveUserToFirebase()
        }
}
    private fun saveUserToFirebase(){

        val uid=FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val username=name.text.toString()
            val user = Cuser(uid,username)

        ref.setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(baseContext, "Doone.",
                        Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {   Toast.makeText(baseContext, " Not stored.",
                    Toast.LENGTH_SHORT).show() }
        }


    }



class Cuser(
    val uid:String?="",
    var username: String? = ""
)