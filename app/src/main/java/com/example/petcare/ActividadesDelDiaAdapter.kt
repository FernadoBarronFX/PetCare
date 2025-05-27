package com.example.petcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActividadesDelDiaAdapter(private val actividades: List<Triple<String, String, String>>) :
    RecyclerView.Adapter<ActividadesDelDiaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val descripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val nombreMascota: TextView = view.findViewById(R.id.txtNombreMascota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad_dia, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (titulo, descripcion, nombreMascota) = actividades[position]
        holder.titulo.text = titulo
        holder.descripcion.text = descripcion
        holder.nombreMascota.text = "Mascota: $nombreMascota"
    }

    override fun getItemCount(): Int = actividades.size
}

