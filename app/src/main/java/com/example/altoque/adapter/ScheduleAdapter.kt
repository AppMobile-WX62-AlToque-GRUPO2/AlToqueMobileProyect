package com.example.altoque.adapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.altoque.R
import com.example.altoque.models.Schedule
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleAdapter(var schedules : List<Schedule>, var itemDeleteClickListener: OnItemDeleteClickListener) : Adapter<SchedulePrototype>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchedulePrototype {
        var view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prototype_create_date_and_time, parent, false)
        
        return SchedulePrototype(view)
    }
    
    override fun onBindViewHolder(holder: SchedulePrototype, position: Int) {
        holder.bind(schedules.get(position), itemDeleteClickListener)
    }
    
    override fun getItemCount(): Int {
        return schedules.size
    }
}

class SchedulePrototype(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    var etDatePost = itemView.findViewById<EditText>(R.id.etDatePost)
    var etStartTimePostProto = itemView.findViewById<EditText>(R.id.etStartTimePostProto)
    var etEndTimePostProto = itemView.findViewById<EditText>(R.id.etEndTimePostProto)
    var fabDelete = itemView.findViewById<FloatingActionButton>(R.id.fabDeleteScheduleProto)
    
    fun bind(schedule: Schedule, itemDeleteClickListener: OnItemDeleteClickListener){
        // Define the date format
        //val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        // Convert the Date to String
        //val dateString = dateFormat.format(schedule.fecha)
        // Convert the String to Editable
        //val editableDate = Editable.Factory.getInstance().newEditable(dateString)
        // Set the Editable to EditText
        //etDatePost.text = editableDate
        
        val editableDate = Editable.Factory.getInstance().newEditable(schedule.fecha)
        etDatePost.text = editableDate

        //val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        //val timeString = timeFormat.format(schedule.hora)
        //val editableTime = Editable.Factory.getInstance().newEditable(timeString)
        //etTimePost.text = editableTime
        
        val editableStartTime = Editable.Factory.getInstance().newEditable(schedule.horainicio)
        etStartTimePostProto.text = editableStartTime
        
        val editableEndTime = Editable.Factory.getInstance().newEditable(schedule.horafin)
        etEndTimePostProto.text = editableEndTime

        fabDelete.setOnClickListener {
            itemDeleteClickListener.onItemClicked(schedule)
        }
    }
    
}

interface OnItemDeleteClickListener {
    fun onItemClicked(schedule: Schedule)
}