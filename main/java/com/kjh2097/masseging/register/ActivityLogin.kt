package com.kjh2097.masseging.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kjh2097.masseging.messages.Activitymassege
import com.kjh2097.masseging.R
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        btn_login.setOnClickListener {

            val email=email_login.text.toString().trim()
            val password=password_login.text.toString()

            Log.d("login", "the email is: "+ email)
            Log.d("login", "the password is: "+ password)

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    val intent=Intent(this, Activitymassege::class.java)
                    startActivity(intent)

                    if (!it.isSuccessful) return@addOnCompleteListener

                    Log.d("login", "successful signing in by id: ${it.result!!.user.uid}")
                    Toast.makeText(this, "singing in is successful", Toast.LENGTH_LONG).show()



                }

                .addOnFailureListener {
                    Log.d("login", "filed at singing in:" + it.message)
                    Toast.makeText(this, "either the email or the password is wrong try again", Toast.LENGTH_LONG).show()
                }
        }

        I_dont_have_an_account.setOnClickListener {
            Log.d("login", "try to go back to the registration page")
            finish()

           // val intent=Intent(this, MainActivity::class.java)
            //startActivity(intent)
        }


    }

}