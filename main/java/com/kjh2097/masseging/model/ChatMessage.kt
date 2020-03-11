package com.kjh2097.masseging.model

import com.kjh2097.masseging.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class Chatmessage(val id: String,val text: String,val fromid: String,val toid: String,val timestamp: Long){
    constructor():this("","","","",-1)
}

class chatfromitem(val text: String,val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text=text

        var uri = user.profileimageurl
        val targetimageview = viewHolder.itemView.imageView2_chat_from_row
        Picasso.get().load(uri).into(targetimageview)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

}
class chattoitem(val text: String,val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text=text

        var uri = user.profileimageurl
        val targetimageview = viewHolder.itemView.imageView_chat_to_row
        Picasso.get().load(uri).into(targetimageview)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}