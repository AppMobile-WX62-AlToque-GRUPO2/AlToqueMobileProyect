package com.example.altoque.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.models.Post

class ServicesAdapter(val posts: List<Post>, val itemClickListener: OnItemClickListener): Adapter<PrototypePublication>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrototypePublication {
        var view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prototype_post, parent, false)

        return PrototypePublication(view)
    }

    override fun onBindViewHolder(holder: PrototypePublication, position: Int) {
        holder.bind(posts.get(position), itemClickListener)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

}

class PrototypePublication(itemView: View): ViewHolder(itemView) {
    var tvTitle = itemView.findViewById<TextView>(R.id.tvPostTitle)
    var tvAddress = itemView.findViewById<TextView>(R.id.tvPostAdd)
    var ivImage = itemView.findViewById<ImageView>(R.id.ivPostPublication)
    var tvIsPublish = itemView.findViewById<TextView>(R.id.tvPostDescript)
    var cvPubPrototype = itemView.findViewById<CardView>(R.id.cvPostPrototype)

    fun bind(post: Post, itemClickListener: OnItemClickListener){
        tvTitle.text = post.title
        tvAddress.text = post.address
        //Picasso.get().load(post.image).into(ivImage)
        Glide.with(itemView.context).load(post.image).into(ivImage)

        if (post.is_publish) tvIsPublish.text = "Hace 20 minutos" else tvIsPublish.text = "Inactivo"

        cvPubPrototype.setOnClickListener {
            itemClickListener.onItemClicked(post)
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(post: Post)
}
