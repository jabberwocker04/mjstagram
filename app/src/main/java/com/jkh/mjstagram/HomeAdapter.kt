package com.jkh.mjstagram

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

import java.util.ArrayList;

class HomeAdapter(var context: Context, var postList:ArrayList<Post>): RecyclerView.Adapter<HomeAdapter.viewHolder>() {

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileIv: CircleImageView = itemView.findViewById(R.id.profile_iv)
        var nameTv: TextView = itemView.findViewById(R.id.name_tv)
        var textTv: TextView =itemView.findViewById(R.id.text_tv)
        var imageIv:ImageView=itemView.findViewById(R.id.image_tv)

        fun bind(post:Post){
            textTv.text=post.text
            Glide.with(imageIv).load(post.imageUrl).into(imageIv)
            var firestore=FirebaseFirestore.getInstance()
            firestore.collection("User")
                    .document(post.userId)
                    .get()
                    .addOnSuccessListener {
                        var user=it.toObject(User::class.java)
                        nameTv.text=user?.name
                        Glide.with(profileIv).load(user?.profileUrl).into(profileIv)

                    }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder{
        var view=LayoutInflater.from(context).inflate(R.layout.item_home,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var post=postList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

}
