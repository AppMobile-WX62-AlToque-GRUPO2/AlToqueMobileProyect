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

import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun Date.timeAgo(): String {
    val currentTime = Date()
    val elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(currentTime.time - this.time)

    return when {
        elapsedSeconds < 60 -> "Hace $elapsedSeconds segundos"
        else -> {
            val elapsedMinutes = TimeUnit.SECONDS.toMinutes(elapsedSeconds)
            if (elapsedMinutes == 1L) "Hace 1 minuto" else "Hace $elapsedMinutes minutos"
        }
    }
}

class PostAdapter(var posts: List<Post>, val itemClickListener: OnItemClickListenerPost): RecyclerView.Adapter<PostPrototype>() {

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            notifyDataSetChanged() // Actualiza todo el adaptador
            handler.postDelayed(this, 1000) // Vuelve a ejecutar cada segundo
        }
    }

    init {
        handler.postDelayed(updateRunnable, 1000)
    }
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

    // Cancelar el runnable cuando el adaptador no se esté usando
    fun cancelUpdates() {
        handler.removeCallbacks(updateRunnable)
    }
}

class PostPrototype(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var tvTitle = itemView.findViewById<TextView>(R.id.tvPostTitle)
    var tvAddress = itemView.findViewById<TextView>(R.id.tvPostAdd)
    var ivImage = itemView.findViewById<ImageView>(R.id.ivPostPublication)
    var tvIsPublish = itemView.findViewById<TextView>(R.id.tvPostDescript)
    var cvPubPrototype = itemView.findViewById<CardView>(R.id.cvPostPrototype)
    private val postTimestamp = Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(Random.nextInt(1, 61).toLong()))

    fun bind(post: Post, itemClickListener: OnItemClickListenerPost){
        tvTitle.text = post.title
        tvAddress.text = post.address
        Glide.with(itemView.context).load(post.image).into(ivImage)


        tvIsPublish.text = if (post.is_publish) postTimestamp.timeAgo() else "Inactivo"

        cvPubPrototype.setOnClickListener {
            itemClickListener.onItemClicked(post)
        }
    }
}

interface OnItemClickListenerPost {
    fun onItemClicked(post: Post)
}