package com.example.petcare

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MascotaAdapter(
    private val mascotas: List<Mascota>,
    private val onItemClick: (Mascota) -> Unit
) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {

    class MascotaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val edad: TextView = view.findViewById(R.id.txtEdad)
        val raza: TextView = view.findViewById(R.id.txtRaza)
        val imagen: ImageView = view.findViewById(R.id.imagenMascota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mascota, parent, false)
        return MascotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = mascotas[position]
        holder.nombre.text = mascota.nombre
        holder.edad.text = "Edad: ${mascota.edad}"
        holder.raza.text = "Raza: ${mascota.raza}"

        // ✅ Mostrar imagen desde almacenamiento interno
        if (mascota.imagenUri.isNotBlank()) {
            val file = File(mascota.imagenUri)
            if (file.exists()) {
                holder.imagen.setImageURI(Uri.fromFile(file))
            } else {
                holder.imagen.setImageResource(R.drawable.ic_pet_placeholder)
            }
        } else {
            holder.imagen.setImageResource(R.drawable.ic_pet_placeholder)
        }

        holder.itemView.setOnClickListener {
            onItemClick(mascota)
        }
    }

    override fun getItemCount(): Int = mascotas.size
}
