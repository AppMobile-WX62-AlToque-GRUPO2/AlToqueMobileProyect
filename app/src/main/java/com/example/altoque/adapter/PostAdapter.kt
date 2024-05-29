package com.example.altoque.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.models.Post

class PostAdapter(var posts: List<Post>): RecyclerView.Adapter<PostPrototype>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostPrototype {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.prototype_post, parent, false)
        return PostPrototype(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostPrototype, position: Int) {
        holder.bind(posts[position])
    }
}

class PostPrototype(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvPostTitle = itemView.findViewById<TextView>(R.id.tvPostTitle)
    val tvPostAdd = itemView.findViewById<TextView>(R.id.tvPostAdd)
    val tvPostDescript = itemView.findViewById<TextView>(R.id.tvPostDescript)
    val ivPostPublication = itemView.findViewById<ImageView>(R.id.ivPostPublication)

    fun bind(post: Post) {
        tvPostTitle.text = post.title
        tvPostAdd.text = post.address
        tvPostDescript.text = post.description

        Glide.with(itemView.context).load(post.image).into(ivPostPublication)
    }
}
