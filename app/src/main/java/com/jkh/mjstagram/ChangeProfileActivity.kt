package com.jkh.mjstagram

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.util.*

class ChangeProfileActivity : AppCompatActivity() {

    lateinit var profileIv:ImageView
    lateinit var nameEt:EditText
    lateinit var modifyBtn: Button
    lateinit var loadingpb: ProgressBar
    val REQ_IMAGE=1000
    var selectedImage: Uri?=null

    lateinit var auth:FirebaseAuth
    lateinit var storage:FirebaseStorage
    lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        profileIv=findViewById(R.id.profile_iv)
        nameEt=findViewById(R.id.name_et)
        modifyBtn=findViewById(R.id.modify_btn)
        loadingpb=findViewById(R.id.Loading_pb)

        auth=FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance()
        firestore= FirebaseFirestore.getInstance()

        profileIv.setOnClickListener {
            var intent = Intent(ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,REQ_IMAGE)
        }
        modifyBtn.setOnClickListener {
            if(selectedImage!=null){
               var filename=UUID.randomUUID().toString()
                storage.getReference("profile").child(filename)
                        .putFile(selectedImage!!)
                        .addOnSuccessListener {
                            it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                                var profileUrl=it.toString()
                                firestore.collection("User")
                                        .document(auth.currentUser?.email!!)
                                        .update("profileUrl",profileUrl)
                                        .addOnSuccessListener {
                                            changeName()
                                        }
                            }

                        }
            }
            else {
                changeName()
            }
        }
    }
    fun changeName(){
        var name=nameEt.text.toString()
        startLoading()
        firestore.collection("User")
                .document(auth.currentUser?.email!!)
                .update("name",name)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        endLoading()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
    }

    fun startLoading(){
        loadingpb.visibility=VISIBLE
    }
    fun endLoading(){
        loadingpb.visibility=GONE
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQ_IMAGE&&resultCode== Activity.RESULT_OK){
            selectedImage=data?.data
            profileIv.setImageURI(selectedImage)
        }
    }
}