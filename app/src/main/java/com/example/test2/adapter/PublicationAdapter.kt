package com.example.test2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.test2.R
import com.example.test2.models.Publication

class PublicationAdapter (val publications : List<Publication>, val itemClickListener: OnItemClickListener) : Adapter<PrototypePublication>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrototypePublication {
        var view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prototype_publication, parent, false)

        return PrototypePublication(view)
    }

    override fun onBindViewHolder(holder: PrototypePublication, position: Int) {
        holder.bind(publications.get(position), itemClickListener)
    }

    override fun getItemCount(): Int {
        return publications.size
    }
}

class PrototypePublication(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvPubTitle = itemView.findViewById<TextView>(R.id.tvPublicationTitle)
    var tvPubDesc = itemView.findViewById<TextView>(R.id.tvPublicationDescription)
    var ivPub = itemView.findViewById<ImageView>(R.id.ivPublication)
    var btnVerMas = itemView.findViewById<TextView>(R.id.btnVerMas)

    fun bind(publication : Publication, itemClickListener : OnItemClickListener ){
        tvPubTitle.text = publication.title
        tvPubDesc.text = publication.address

        Glide.with(itemView).load(publication.image).into(ivPub)

        btnVerMas.setOnClickListener {
            itemClickListener.onItemClicked(publication)
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(publication : Publication)
}