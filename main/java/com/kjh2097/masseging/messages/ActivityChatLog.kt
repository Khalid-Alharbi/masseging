package com.kjh2097.masseging.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.kjh2097.masseging.R
import com.kjh2097.masseging.model.Chatmessage
import com.kjh2097.masseging.model.User
import com.kjh2097.masseging.model.chatfromitem
import com.kjh2097.masseging.model.chattoitem
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ActivityChatLog : AppCompatActivity() {

    var toUser: User? =null

    val adapter=GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recycleview_chat_log.adapter=adapter


        //val username = intent.getStringExtra(newmessageActivity.user_key)
        //supportActionBar?.title=

        toUser = intent.getParcelableExtra<User>(newmessageActivity.user_key)
        supportActionBar?.title= toUser?.username
        //supportActionBar?.title=user.username


        //demodata()
        listenformessages()

        send_btn_chat_log.setOnClickListener {
            Log.d("chatlog","trying to send")
            preformsendmessage()
        }
    }

    private fun listenformessages(){
        val Fromid = FirebaseAuth.getInstance().uid
        val toid = toUser?.uid
        val refe=FirebaseDatabase.getInstance().getReference("/user-messages/$Fromid/$toid")
        refe.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(Chatmessage::class.java)

                if(chatMessage !=null){
                    Log.d("chatlog",chatMessage.text)
                    if (chatMessage.fromid==FirebaseAuth.getInstance().uid){
                        val currentUser= Activitymassege.currentUser ?:return
                        adapter.add(chatfromitem(chatMessage.text, currentUser))
                    } else{
                        adapter.add(chattoitem(chatMessage.text, toUser!!))

                    }

                }

            recycleview_chat_log.scrollToPosition(adapter.itemCount -1)
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    private fun preformsendmessage(){
        val text=enter_message_chat_log.text.toString()
        //val ref = FirebaseDatabase.getInstance().getReference("/messages").push()

        val Fromid = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(newmessageActivity.user_key)
        val toid = user.uid
        if (Fromid==null) return
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$Fromid/$toid").push()
        val toref =FirebaseDatabase.getInstance().getReference("/user-messages/$toid/$Fromid").push()

        val chatmessage= Chatmessage(ref.key!!, text, Fromid, toid,
            System.currentTimeMillis() / 1000)

        ref.setValue(chatmessage)
            .addOnSuccessListener {
                Log.d("chatlog","saved our chat message: ${ref.key}")

                enter_message_chat_log.text.clear()
                recycleview_chat_log.scrollToPosition(adapter.itemCount -1)
            }
        toref.setValue(chatmessage)
        val latestref= FirebaseDatabase.getInstance().getReference("/latest_messages/$Fromid/$toid")
        latestref.setValue(chatmessage)
        val latesttoref= FirebaseDatabase.getInstance().getReference("/latest_messages/$toid/$Fromid")
        latesttoref.setValue(chatmessage)
    }
}


   /* private fun demodata(){

        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(chatfromitem("fu"))
        adapter.add(chattoitem("you to "))
        adapter.add(chatfromitem("fu"))
        adapter.add(chattoitem("you to "))
        adapter.add(chatfromitem("fu"))
        adapter.add(chattoitem("you to "))
        adapter.add(chatfromitem("fu"))
        adapter.add(chattoitem("you to "))

        recycleview_chat_log.adapter = adapter
    }*/


