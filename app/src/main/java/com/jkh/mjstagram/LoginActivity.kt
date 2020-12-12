package com.jkh.mjstagram

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity: AppCompatActivity() {

    lateinit var emailEt:EditText
    lateinit var passwordEt:EditText
    lateinit var loginBtn:Button
    lateinit var loadingPb:ProgressBar

    lateinit var auth:FirebaseAuth
    lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEt=findViewById(R.id.email_et)
        passwordEt=findViewById(R.id.password_et)
        loginBtn=findViewById(R.id.login_btn)
        loadingPb=findViewById(R.id.Loading_pb)



        auth = FirebaseAuth.getInstance()
        moveMain(auth.currentUser)

        firestore= FirebaseFirestore.getInstance()
        loginBtn.setOnClickListener{
            var email=emailEt.text.toString()
            var password=passwordEt.text.toString()

            if(email.equals("")){
                Toast.makeText(this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.length<6){
                Toast.makeText(this,"패스워드를 6자이상 입력해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startLoading()
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                startLoading()
                if(it.isSuccessful){
                    moveMain(auth.currentUser)
                }
                else{
                    join(email,password)
                }
            }

        }
    }
    fun join(email:String,password:String){
        startLoading()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                stopLoading()
                if(it.isSuccessful){
                    var user=User(auth.currentUser?.email!!)
                    startLoading()
                    firestore.collection("User")
                        .document(user.email!!)
                        .set(user)
                        .addOnSuccessListener {
                            stopLoading()
                            moveMain(auth.currentUser)
                        }
                }
                else{
                    Toast.makeText(this,"이메일 또는 패스워드가 틀렸습니다.",Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun moveMain(user:FirebaseUser?){
        if(user==null)
            return
        var intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }




    fun startLoading(){
        loadingPb.visibility= VISIBLE
    }
    fun stopLoading(){
        loadingPb.visibility=GONE
    }
}