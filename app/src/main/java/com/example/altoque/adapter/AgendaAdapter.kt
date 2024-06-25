package com.example.altoque.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.models.Contract
import com.example.altoque.models.Post
import com.example.altoque.networking.ContractService
import com.example.altoque.networking.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgendaAdapter(val posts: List<Post>, val contracts: List<Contract>, val itemClickListener: OnItemClickListenerContract): RecyclerView.Adapter<PrototypeAgenda>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrototypeAgenda {
        var view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prototype_agenda, parent, false)

        return PrototypeAgenda(view)
    }

    override fun onBindViewHolder(holder: PrototypeAgenda, position: Int) {
        holder.bind(posts.get(position), contracts.get(position), itemClickListener)
    }

    override fun getItemCount(): Int {
        return contracts.size
    }

}

class PrototypeAgenda(itemView: View): RecyclerView.ViewHolder(itemView) {
    var tvTitle = itemView.findViewById<TextView>(R.id.tvSpeAgeTitle)
    var tvAddress = itemView.findViewById<TextView>(R.id.tvSpeAgeAddress)
    var ivImage = itemView.findViewById<ImageView>(R.id.ivSpeAgeImage)
    var tvState = itemView.findViewById<TextView>(R.id.tvSpeAgeState)
    var btnFinish = itemView.findViewById<TextView>(R.id.btnSpeAgeFinish)

    @SuppressLint("ResourceAsColor")
    fun bind(post: Post, contract: Contract, itemClickListener: OnItemClickListenerContract){
        tvTitle.text = post.title
        tvAddress.text = post.address
        Glide.with(itemView.context).load(post.image).into(ivImage)

        when (contract.state) {
            1 -> {
                tvState.text = "En Progreso"
                tvState.setTextColor(R.color.in_progress)
            }
            2 -> {
                tvState.text = "Rechazado"
                tvState.setTextColor(R.color.rejected)
            }
            3 -> {
                tvState.text = "Aceptado"
                tvState.setTextColor(R.color.accepted)
                btnFinish.visibility = View.VISIBLE
                btnFinish.setOnClickListener {
                    contract.state = 4
                    val contractService = RetrofitClient.createService<ContractService>()
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val response = contractService.updateContract(contract.id, contract)
                            if (response.isSuccessful) {
                                Toast.makeText(itemView.context, "Contrato actualizado", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(itemView.context, "Error al actualizar el contrato", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(itemView.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            4 -> {
                tvState.text = "Terminado"
                tvState.setTextColor(R.color.finished)
            }
        }
        ivImage.setOnClickListener {
            itemClickListener.onItemClicked(contract)
        }
    }
}

interface OnItemClickListenerContract {
    fun onItemClicked(contract: Contract)
}