package com.jkh.mjstagram

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.hdodenhof.circleimageview.CircleImageView

class profileFragment: Fragment() {

    lateinit var profileIv:CircleImageView
    lateinit var nameTv: TextView
    lateinit var photoListrv:RecyclerView
    lateinit var chaneProfilebtn:Button
    lateinit var loadingPb:ProgressBar
    lateinit var Logoutbtn:Button

    val REQ_CHANGE_PROFILE=2000

    lateinit var postList:ArrayList<Post>
    lateinit var profileAdapter:ProfileAdapter

    lateinit var auth:FirebaseAuth
    lateinit var firestore:FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_profile,container,false)
        profileIv=view.findViewById(R.id.profile_iv)
        nameTv=view.findViewById(R.id.name_tv)
        chaneProfilebtn=view.findViewById(R.id.change_profile_btn)
        loadingPb=view.findViewById(R.id.loading_pb)
        photoListrv=view.findViewById(R.id.photo_list_rv)
        Logoutbtn=view.findViewById(R.id.Logout_btn)

        auth=FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postList=ArrayList()
        profileAdapter=ProfileAdapter(activity!!, postList)

        photoListrv.adapter=profileAdapter
        photoListrv.layoutManager=GridLayoutManager(activity, 3)

        firestore.collection("post")
                .whereEqualTo("userId",auth.currentUser?.email)
                .orderBy("uploadDate", Query.Direction.ASCENDING)
                .addSnapshotListener{ value, error ->
                    if(value != null){
                        for(dc in value.documentChanges){
                            if(dc.type==DocumentChange.Type.ADDED){
                                var post=dc.document.toObject(Post::class.java)
                                postList.add(0,post)
                            }
                        }
                        profileAdapter.notifyDataSetChanged()
                    }
                }

        chaneProfilebtn.setOnClickListener {
            var intent= Intent(activity,ChangeProfileActivity::class.java)
            startActivityForResult(intent,REQ_CHANGE_PROFILE)
        }
        Logoutbtn.setOnClickListener {
            auth.signOut()
            var intent=Intent(activity,LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()

        }
        updateProfile()
    }

    fun updateProfile(){
        startLoading()
        firestore.collection("User")
                .document(auth.currentUser?.email!!)
                .get()
                .addOnSuccessListener{
                    endLoading()
                    var user=it.toObject(User::class.java)
                    if(user?.profileUrl!=null){
                        Glide.with(profileIv).load(user?.profileUrl).into(profileIv)
                    }
                    nameTv.text=user?.name
                }
    }
    fun startLoading(){
        loadingPb.visibility=VISIBLE
    }
    fun endLoading(){
        loadingPb.visibility=GONE
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQ_CHANGE_PROFILE&&resultCode==RESULT_OK){
            updateProfile()
        }
    }
}