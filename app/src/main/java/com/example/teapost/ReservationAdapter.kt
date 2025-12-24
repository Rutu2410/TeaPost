package com.example.teapost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReservationAdapter(
    private val reservations: List<Reservation>,
    private val onDelete: (Int) -> Unit,
    private val onUpdate: (Reservation) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.bind(reservation)
        holder.itemView.findViewById<View>(R.id.button_delete).setOnClickListener {
            onDelete(reservation.id)
        }
        holder.itemView.findViewById<View>(R.id.button_update).setOnClickListener {
            onUpdate(reservation)
        }
    }

    override fun getItemCount(): Int = reservations.size

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.text_name)
        private val dateTextView: TextView = itemView.findViewById(R.id.text_date)
        private val timeTextView: TextView = itemView.findViewById(R.id.text_time)
        private val peopleTextView: TextView = itemView.findViewById(R.id.text_people)
        private val specialEventTextView: TextView = itemView.findViewById(R.id.text_special_event)

        fun bind(reservation: Reservation) {
            nameTextView.text = reservation.name
            dateTextView.text = reservation.date
            timeTextView.text = reservation.time
            peopleTextView.text = reservation.people.toString()
            specialEventTextView.text = reservation.specialEvent
        }
    }
}
