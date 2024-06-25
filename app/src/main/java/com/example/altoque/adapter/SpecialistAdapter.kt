package com.example.altoque.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.models.User

class SpecialistAdapter(
    private val specialists: List<User>,
    private val itemClickListener: OnSpecialistClickListener
) : RecyclerView.Adapter<SpecialistAdapter.SpecialistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.prototype_specialist_publication, parent, false)
        return SpecialistViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpecialistViewHolder, position: Int) {
        holder.bind(specialists[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return specialists.size
    }

    class SpecialistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullNameTextView = itemView.findViewById<TextView>(R.id.tvPublicationEspecialistFullName)
        private val emailTextView = itemView.findViewById<TextView>(R.id.tvPublicationEspecialistEmail)
        private val phoneNumberTextView = itemView.findViewById<TextView>(R.id.tvPublicationEspecialistNumber)
        private val avatarImageView = itemView.findViewById<ImageView>(R.id.ivPublicationEspecialsitContractProposal)
        private val seeMoreButton = itemView.findViewById<Button>(R.id.btnVerMasSpecialistPublication)

        @SuppressLint("SetTextI18n")
        fun bind(especialist: User, clickListener: OnSpecialistClickListener) {
            fullNameTextView.text = "${especialist.firstName} ${especialist.lastName}"
            emailTextView.text = especialist.email
            phoneNumberTextView.text = especialist.phone

            Glide.with(itemView).load(especialist.avatar).into(avatarImageView)

            seeMoreButton.setOnClickListener {
                clickListener.onSpecialistClicked(especialist)
            }
        }
    }
}

interface OnSpecialistClickListener {
    fun onAcceptClicked(specialist: User)
    fun onDeleteClicked(specialist: User)
    fun onSpecialistClicked(specialist: User)
}
