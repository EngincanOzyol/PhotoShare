package com.example.postpaylasma

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.PostMessageBackend
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.postpaylasma.databinding.ActivityPostPaylasmaBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PostPaylasma : AppCompatActivity() {
    private var gorselSec: Uri? =null
    private var bitmapSec:Bitmap?=null
    private lateinit var binding: ActivityPostPaylasmaBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var database:FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPostPaylasmaBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        storage= FirebaseStorage.getInstance()
    }
    fun gorselSec(view:View){
        if(ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
        else{
            val galeriIntent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(galeriIntent)

        }

    }
    val resultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        results: ActivityResult->
        gorselSec= results.data?.data

        if(gorselSec !=null && results.resultCode==Activity.RESULT_OK){
            if(Build.VERSION.SDK_INT>=28){
                val source=ImageDecoder.createSource(applicationContext.contentResolver, gorselSec!!)
                bitmapSec=ImageDecoder.decodeBitmap(source)
                binding.imageView2.setImageBitmap(bitmapSec)

            }
            else{
                bitmapSec=MediaStore.Images.Media.getBitmap(applicationContext.contentResolver,gorselSec)
                binding.imageView2.setImageBitmap(bitmapSec)
            }

        }
    }
    fun paylas(view:View){
        val uuid=UUID.randomUUID()
        val gorselSecim="${uuid}.jpg"
       val reference=storage.reference
       val gorselReference= reference.child("images").child(gorselSecim)
        if(gorselSec !=null){
            gorselReference.putFile(gorselSec!!).addOnSuccessListener {
                    taskSnapshot->

                val yuklenenGorselReference=FirebaseStorage.getInstance().reference.child("images").child(gorselSecim)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener {
                    uri->
                    val downoladUrl=uri.toString()
                    val kullaniciYorum=binding.yorumText.text.toString()
                    val kullaniciEmail=auth.currentUser?.email.toString()
                    val tarih=Timestamp.now()

                    //database islemleri
                    val map=HashMap<String,Any>()
                     map.put("gorselurl",downoladUrl)
                    map.put("yorum",kullaniciYorum)
                    map.put("email",kullaniciEmail)
                    map.put("tarih",tarih)

                    database.collection("post").add(map).addOnCompleteListener {
                        task->
                        if(task.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener {
                        expection->
                        Toast.makeText(applicationContext,expection.localizedMessage,Toast.LENGTH_LONG)

                    }




                }.addOnFailureListener {
                    expection->
                    Toast.makeText(applicationContext,expection.localizedMessage,Toast.LENGTH_LONG)
                }

            }
        }


      val intent=Intent(applicationContext,PostList::class.java)
        startActivity(intent)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val galeriIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                resultLauncher.launch(galeriIntent)

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}