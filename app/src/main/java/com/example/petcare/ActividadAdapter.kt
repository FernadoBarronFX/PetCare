package com.example.petcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActividadAdapter(
    private val actividades: List<Actividad>,
    private val onItemClick: (Actividad) -> Unit
) : RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTituloActividad)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFechaActividad)
        val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcionActividad)


        init {
            itemView.setOnClickListener {
                val actividad = actividades[adapterPosition]
                onItemClick(actividad)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actividad = actividades[position]
        holder.txtTitulo.text = actividad.titulo
        holder.txtFecha.text = actividad.fecha
        holder.txtDescripcion.text = actividad.descripcion
    }

    override fun getItemCount(): Int = actividades.size
}

