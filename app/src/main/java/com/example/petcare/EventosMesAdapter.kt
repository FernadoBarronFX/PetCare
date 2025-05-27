package com.example.petcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventosMesAdapter(private val eventos: List<Pair<String, String>>) :
    RecyclerView.Adapter<EventosMesAdapter.EventoViewHolder>() {

    class EventoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDia: TextView = view.findViewById(R.id.txtDiaEvento)
        val txtTitulo: TextView = view.findViewById(R.id.txtTituloEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento_mes, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val (dia, titulo) = eventos[position]
        holder.txtDia.text = dia
        holder.txtTitulo.text = titulo
    }

    override fun getItemCount(): Int = eventos.size
}
