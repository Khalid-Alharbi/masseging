package com.kjh2097.masseging.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kjh2097.masseging.messages.Activitymassege
import com.kjh2097.masseging.R
import com.kjh2097.masseging.model.User

import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class ActivityRegister : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        to_login.setOnClickListener {
            Log.d("reg", "try to show another activity")

            val intent1 = Intent(this, ActivityLogin::class.java)
            startActivity(intent1)
        }
        btn_reg.setOnClickListener {
            preformreg()
        }

        ph_btn_reg.setOnClickListener {
            Log.d("reg", "try to show the photo")

            val intent=Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)

        }
    }
    var selectedphotouri: Uri? = null

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            Log.d("reg", "the photo is selected")

            selectedphotouri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedphotouri)


            //val bitmapDrawable = BitmapDrawable(bitmap)
            //ph_btn_reg.setBackgroundDrawable(bitmapDrawable)

            circle_ph.setImageBitmap(bitmap)
            ph_btn_reg.alpha = 0F
        }
    }
    fun preformreg(){
        val email = email_reg.text.toString().trim()
        val password = password_reg.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "please enter an email/pw ", Toast.LENGTH_LONG).show()
            return
        }
        Log.d("reg", "this is the email: " + email)
        Log.d("reg", "this is the password: " + password)

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else
                Log.d("reg", "successful created user by the id of : ${it.result!!.user.uid}")
                Toast.makeText(this, "user created", Toast.LENGTH_LONG).show()

                uploadimagetofirebasestorage()
                savedatatodatabase(it.toString())
            }
            .addOnFailureListener {
                Log.d("reg", "filed at creating a user:" + it.message)
                Toast.makeText(this, "the emails format is wrong", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadimagetofirebasestorage(){
        if (selectedphotouri == null) return

        val filename= UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedphotouri!!)
            .addOnSuccessListener {
                Log.d("reg","the photo is uploaded successfully : ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("reg", "the url to the image is : $it")

                    savedatatodatabase(it.toString())
                }
            }
            .addOnFailureListener(){
                //do some loggig here to tale me it filed
            }
    }
    private fun savedatatodatabase(profileimageurl: String){
        val uid= FirebaseAuth.getInstance().uid ?: ""
        val ref= FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user= User(uid, usrname_reg.text.toString(), profileimageurl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("reg","the data is saved in the Firebase")
                val intent=Intent(this, Activitymassege::class.java)
                startActivity(intent)
            }
            .addOnFailureListener(){
                //do some loggig here to tale me it filed
                Log.d("reg","sorry!! the data didnt save in Firebase")
            }
    }
}
