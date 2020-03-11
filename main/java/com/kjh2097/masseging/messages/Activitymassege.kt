package com.kjh2097.masseging.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kjh2097.masseging.R
import com.kjh2097.masseging.model.Chatmessage
import com.kjh2097.masseging.model.LateastMessagesRow
import com.kjh2097.masseging.model.User
import com.kjh2097.masseging.register.ActivityRegister
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_massege.*
import kotlinx.android.synthetic.main.leatest_messages_row.view.*

class Activitymassege : AppCompatActivity() {

    companion object{
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_massege)

        recyclerview_leatest_messages.adapter= adapter
        recyclerview_leatest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            Log.d("latestmesssages","clickk")
            val intent=Intent(this,ActivityChatLog::class.java)

            val row = item as LateastMessagesRow


            intent.putExtra(newmessageActivity.user_key, row.chatpartneruser)
            startActivity(intent)
        }

        //setupdummyrows()
        listenforlatestmessages()
        fetchcurrentuser()
        verifyifuserloggedin()
    }

    val latestmessagesmap= HashMap<String, Chatmessage>()

    fun refreshrecyclermessages(){
        adapter.clear()
        latestmessagesmap.values.forEach {
            adapter.add(LateastMessagesRow(it))
        }

    }

    private fun listenforlatestmessages(){

        val fromid =FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("/latest_messages/$fromid")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage= p0.getValue(Chatmessage::class.java) ?: return

                latestmessagesmap[p0.key!!]=chatMessage

                refreshrecyclermessages()
               // adapter.add(LateastMessagesRow(chatMessage))

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage= p0.getValue(Chatmessage::class.java) ?: return
                latestmessagesmap[p0.key!!]=chatMessage
                refreshrecyclermessages()
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }
    val adapter= GroupAdapter<ViewHolder>()

//    private fun setupdummyrows(){
//
//        adapter.add(LateastMessagesRow())
//        adapter.add(LateastMessagesRow())
//        adapter.add(LateastMessagesRow())
//
//    }


    private fun fetchcurrentuser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("messages","current username: ${currentUser?.profileimageurl}")
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun verifyifuserloggedin(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent=Intent(this, ActivityRegister::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.new_massege -> {
                val intent=Intent(this, newmessageActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this, ActivityRegister::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
