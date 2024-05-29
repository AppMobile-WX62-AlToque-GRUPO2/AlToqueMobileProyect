package com.example.test2.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.test2.R
import com.example.test2.models.User

class EspecialistAdapter(
    private val especialists: List<User>,
    private val itemClickListener: OnEspecialistClickListener
) : RecyclerView.Adapter<EspecialistAdapter.EspecialistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspecialistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.prototype_especialist_publication, parent, false)
        return EspecialistViewHolder(view)
    }

    override fun onBindViewHolder(holder: EspecialistViewHolder, position: Int) {
        holder.bind(especialists[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return especialists.size
    }

    class EspecialistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullNameTextView: TextView = itemView.findViewById(R.id.tvPublicationEspecialistFullName)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvPublicationEspecialistEmail)
        private val phoneNumberTextView: TextView = itemView.findViewById(R.id.tvPublicationEspecialistNumber)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.ivPublicationEspecialsitContractProposal)
        private val acceptButton: ImageButton = itemView.findViewById(R.id.ibAcceptContractEspecialistProposal)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.ibDeleteContractEspecialistProposal)
        private val acceptProposal: ImageButton = itemView.findViewById(R.id.ibAcceptContractEspecialistProposal)

        @SuppressLint("SetTextI18n")
        fun bind(especialist: User, clickListener: OnEspecialistClickListener) {
            fullNameTextView.text = "${especialist.firstName} ${especialist.lastName}"
            emailTextView.text = especialist.email
            phoneNumberTextView.text = especialist.phone

            Glide.with(itemView).load(especialist.avatar).into(avatarImageView)

            acceptButton.setOnClickListener {
                clickListener.onAcceptClicked(especialist)
            }

            deleteButton.setOnClickListener {
                clickListener.onDeleteClicked(especialist)
            }

            acceptProposal.setOnClickListener {
                clickListener.onEspecialistClicked(especialist)
            }
        }
    }
}

interface OnEspecialistClickListener {
    fun onAcceptClicked(especialist: User)
    fun onDeleteClicked(especialist: User)
    fun onEspecialistClicked(especialist: User)
}
