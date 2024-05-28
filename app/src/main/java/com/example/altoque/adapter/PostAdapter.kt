package com.example.altoque.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.models.Post
import com.squareup.picasso.Picasso

class PostAdapter(var posts: List<Post>, val itemClickListener: OnItemClickListenerPost): RecyclerView.Adapter<PostPrototype>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostPrototype {
        var view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prototype_post, parent, false)

        return PostPrototype(view)
    }

    override fun onBindViewHolder(holder: PostPrototype, position: Int) {
        holder.bind(posts.get(position), itemClickListener)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}

class PostPrototype(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var tvTitle = itemView.findViewById<TextView>(R.id.tvPostTitle)
    var tvAddress = itemView.findViewById<TextView>(R.id.tvPostAdd)
    var ivImage = itemView.findViewById<ImageView>(R.id.ivPostPublication)
    var tvIsPublish = itemView.findViewById<TextView>(R.id.tvPostDescript)
    var cvPubPrototype = itemView.findViewById<CardView>(R.id.cvPostPrototype)

    fun bind(post: Post, itemClickListener: OnItemClickListenerPost){
        tvTitle.text = post.title
        tvAddress.text = post.address
        Picasso.get().load(post.image).into(ivImage)

        if (post.is_publish) tvIsPublish.text = "Hace 20 minutos" else tvIsPublish.text = "Inactivo"

        cvPubPrototype.setOnClickListener {
            itemClickListener.onItemClicked(post)
        }
    }
}

interface OnItemClickListenerPost {
    fun onItemClicked(post: Post)
}
