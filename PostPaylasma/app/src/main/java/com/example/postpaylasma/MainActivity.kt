package com.example.postpaylasma

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.postpaylasma.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        auth= FirebaseAuth.getInstance()
        val guncelKullanici=auth.currentUser
        if(guncelKullanici !=null){
            val intent=Intent(applicationContext,PostList::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun giris(view:View){
        val email=binding.EmailText.text.toString()
        val password=binding.PasswordText.text.toString()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task->
            if(task.isSuccessful){
                val intent=Intent(applicationContext,PostList::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            expection->
            Toast.makeText(applicationContext,expection.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
    fun kayitOl(view: View){
        val email=binding.EmailText.text.toString()
        val password=binding.PasswordText.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task->
            if(task.isSuccessful){
                val intent=Intent(applicationContext,PostList::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { expection->
            Toast.makeText(applicationContext,expection.localizedMessage,Toast.LENGTH_LONG).show()
        }


    }
}