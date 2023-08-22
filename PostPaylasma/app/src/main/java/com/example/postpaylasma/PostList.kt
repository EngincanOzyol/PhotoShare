package com.example.postpaylasma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postpaylasma.databinding.ActivityPostListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class PostList : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database:FirebaseFirestore
    private lateinit var binding:ActivityPostListBinding
    private lateinit var recylerviewAdapter:PostAdapter
    var listPost=ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPostListBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()

        val layoutManager=LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        recylerviewAdapter= PostAdapter(listPost)
        binding.recyclerView.adapter=recylerviewAdapter

        veriAl()

    }
fun veriAl(){
    database.collection("post").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { snapshot, expection ->

        if(expection !=null){
            Toast.makeText(applicationContext,expection.localizedMessage,Toast.LENGTH_LONG)
        }
        else{
            if(snapshot !=null){
                if(!snapshot.isEmpty){
                    val docs=snapshot.documents
                    listPost.clear()
                    for(doc in docs){
                       val gorselUrl= doc.get("gorselurl") as String
                        val email=doc.get("email") as String
                        val yorum=doc.get("yorum") as String
                        val postList=Post(email,yorum,gorselUrl)
                        listPost.add(postList)
                    }
                    recylerviewAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=MenuInflater(this)
        inflater.inflate(R.menu.secenek_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.cıkısYapSecenek){
            auth.signOut()
            val intent= Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }
        else if(item.itemId==R.id.fotografEkleSecenek){
            val intent=Intent(applicationContext,PostPaylasma::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}