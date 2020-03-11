package com.kjh2097.masseging.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kjh2097.masseging.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.leatest_messages_row.view.*

class LateastMessagesRow(val chatMassage:Chatmessage): Item<ViewHolder>(){
    var chatpartneruser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_lateast_message_row.text = chatMassage.text

        val chatpartner: String
        if (chatMassage.fromid == FirebaseAuth.getInstance().uid) {
            chatpartner=chatMassage.toid
        }else{
            chatpartner=chatMassage.fromid
        }

        val ref= FirebaseDatabase.getInstance().getReference("/users/$chatpartner")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatpartneruser = p0.getValue(User::class.java) ?: return
                viewHolder.itemView.username_lateast_row.text = chatpartneruser?.username

                val latestmessageimage= viewHolder.itemView.imageView2_leatest_row
                Picasso.get().load(chatpartneruser?.profileimageurl).into(latestmessageimage)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })



    }
    override fun getLayout(): Int {
        return R.layout.leatest_messages_row
    }

}