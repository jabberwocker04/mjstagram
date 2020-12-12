package com.jkh.mjstagram


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class ProfileAdapter (var context: Context, var postList:ArrayList<Post>):RecyclerView.Adapter<ProfileAdapter.viewHolder>(){

        inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageIv: ImageView = itemView.findViewById(R.id.image_iv)
            fun bind(post:Post){
                Glide.with(imageIv).load(post.imageUrl).into(imageIv)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
            var view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false)
            return viewHolder(view)
        }

        override fun onBindViewHolder(holder: viewHolder, position: Int) {
            var post = postList[position]
            holder.bind(post)
        }

        override fun getItemCount(): Int {
            return postList.size
        }
}