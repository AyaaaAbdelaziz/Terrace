package com.amuab.pergola

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.login.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "MyActivity"
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        auth = FirebaseAuth.getInstance()
       // button = findViewById(R.id.logout_btn) as Button
        supportActionBar?.hide()


        lgn_btn.setOnClickListener {
              doLogin()
        }

        regbtn.setOnClickListener {
            startActivity(Intent(this, register::class.java))
            finish()

        }



    }

       private fun doLogin(){
        if (L_email.text.toString().isEmpty()){
            L_email.error="من فضلك ادخل ايميل"
            L_email.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(L_email.text.toString()).matches()){
            L_email.error="invalid email"
            L_email.requestFocus()
            return
        }
        if (L_pass.text.toString().isEmpty()){
            L_pass.error="من فضلك ادخل pass"
            L_pass.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(L_email.text.toString(), L_pass.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user :FirebaseUser?= auth.currentUser
                      updateUI(user)
                } else {
                    updateUI(null)
                }

            }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = auth.currentUser
        updateUI(currentUser)
    }
     fun signOut() {
        auth.signOut()

         updateUI(null)
    }

    private fun updateUI (currentUser: FirebaseUser?) {
        if (currentUser != null) {

            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, Home::class.java))
                finish()
            } else {
                Toast.makeText(
                    baseContext, "Please verfiy ur mail;",
                    Toast.LENGTH_SHORT
                ).show()

        }
           // startActivity(Intent(this, Home::class.java))

        }
    }

}


