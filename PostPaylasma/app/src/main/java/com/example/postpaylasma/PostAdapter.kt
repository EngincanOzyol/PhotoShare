package com.example.postpaylasma

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postpaylasma.databinding.RecylerviewRowBinding
import com.squareup.picasso.Picasso
import java.util.*

class PostAdapter(val postList:ArrayList<Post>):RecyclerView.Adapter<PostAdapter.PostHolder>() {
    class PostHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding=RecylerviewRowBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
       val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recylerview_row,parent,false)
       return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recylerTextEmail.text=postList[position].email
        holder.binding.recylerTextYorum.text=postList[position].yorum
        Picasso.get().load(postList[position].gorselUrl).into(holder.binding.recylerImageView)



    }

    override fun getItemCount(): Int {
       return postList.size
    }
}