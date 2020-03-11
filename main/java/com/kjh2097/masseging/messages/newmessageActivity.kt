package com.kjh2097.masseging.messages

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kjh2097.masseging.R
import com.kjh2097.masseging.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_massege.*
import kotlinx.android.synthetic.main.activity_newmessage.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class newmessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newmessage)

        supportActionBar?.title="select user"


      /*  val adapter = GroupAdapter<ViewHolder>()

        adapter.add(Useritem())
        adapter.add(Useritem())
        adapter.add(Useritem())

        recycler_view.adapter=adapter
        */
        fetchuser()

    }
    companion object{
        val user_key = "user_key"
    }
    private fun fetchuser(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{


            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("newmessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null){
                        adapter.add(Useritem(user))

                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as Useritem

                    val intent =Intent(view.context, ActivityChatLog::class.java)
                    //intent.putExtra(user_key, UserItem.user.username)
                    intent.putExtra(user_key, userItem.user)

                    startActivity(intent)

                    finish()
                }
                recycler_view_new_message.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
class Useritem(val user: User):Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_row.text=user.username
        Picasso.get().load(user.profileimageurl).into(viewHolder.itemView.imageView)

    }
    override fun getLayout(): Int{
        return R.layout.user_row_new_message
    }
}
