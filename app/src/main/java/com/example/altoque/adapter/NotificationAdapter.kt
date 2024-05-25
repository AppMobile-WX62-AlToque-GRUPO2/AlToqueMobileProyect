package com.example.altoque.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.models.Notification

class NotificationAdapter(var notifications: List<Notification>): RecyclerView.Adapter<NotificationPrototype>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationPrototype {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.prototype_notification, parent, false)
        return NotificationPrototype(view)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationPrototype, position: Int) {
        holder.bind(notifications[position])
    }
}

class NotificationPrototype(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvAtenderConsulta = itemView.findViewById<TextView>(R.id.tvAtenderConsulta)

    fun bind(notification: Notification) {
        tvAtenderConsulta.text = notification.text
    }
}
