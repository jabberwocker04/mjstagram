package com.jkh.mjstagram

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class addFragment:Fragment() {

    lateinit var loadingPb: ProgressBar
    lateinit var imageIv:ImageView
    lateinit var textEt:EditText
    lateinit var subMitBtn:Button

    lateinit var auth:FirebaseAuth
    lateinit var storage:FirebaseStorage
    lateinit var firestore:FirebaseFirestore
    val REQ_IMAGE=1234
    var selectedImage: Uri?=null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.fragment_add,container,false)

        loadingPb=view.findViewById(R.id.loading_pb)
        imageIv=view.findViewById(R.id.image_iv)
        textEt=view.findViewById(R.id.text_et)
        subMitBtn=view.findViewById(R.id.submit_btn)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        imageIv.setOnClickListener {
            var intent= Intent(ACTION_PICK)
            intent.type="image/*"

            startActivityForResult(intent,REQ_IMAGE)
        }

        subMitBtn.setOnClickListener {
            var text = textEt.text.toString()
            if(text.length==0){
                Toast.makeText(activity,"문구를 입력해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(selectedImage==null){
                Toast.makeText(activity,"이미지를 선택해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var fileName= UUID.randomUUID().toString()
            startLoading()
            storage.getReference().child("post").child(fileName)
                    .putFile(selectedImage!!)
                    .addOnSuccessListener {
                        it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        var imageUri = it.toString()
                        var post = Post(auth.currentUser?.email!!, imageUri, text)
                            firestore.collection("post")
                                    .document()
                                    .set(post)
                                    .addOnSuccessListener {
                                        endLoading()
                                        clearView()
                                        var mainActivity=activity as MainActivity
                                        mainActivity.moveTab(0)
                                    }
                        }
            }
        }
    }

    fun clearView(){
        textEt.text.clear()
        imageIv.setImageDrawable(activity?.resources?.getDrawable(R.drawable.baseline_add_black_48))
        imageIv.scaleType=ImageView.ScaleType.CENTER_INSIDE
        selectedImage=null

    }

    fun startLoading(){
        loadingPb.visibility=VISIBLE
    }
    fun endLoading(){
        loadingPb.visibility=GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQ_IMAGE&&resultCode==RESULT_OK){
        selectedImage=data?.data
            imageIv.setImageURI(selectedImage)
            imageIv.scaleType=ImageView.ScaleType.CENTER_CROP
        }
    }

}