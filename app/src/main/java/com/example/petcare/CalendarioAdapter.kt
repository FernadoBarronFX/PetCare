package com.example.petcare

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarioAdapter(
    private val dias: List<LocalDate>,
    private val diasConActividades: List<LocalDate>,
    private var fechaSeleccionada: LocalDate? = null,
    private val onDiaClick: (LocalDate) -> Unit
) : RecyclerView.Adapter<CalendarioAdapter.DiaViewHolder>() {

    private val formatter = DateTimeFormatter.ofPattern("dd")

    inner class DiaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDia: TextView = view.findViewById(R.id.txtDia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dia_calendario, parent, false)
        return DiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaViewHolder, position: Int) {
        val fecha = dias[position]
        holder.txtDia.text = formatter.format(fecha)

        val context = holder.txtDia.context

        val fondoNormal = ContextCompat.getColor(context, android.R.color.transparent)
        val fondoActividad = ContextCompat.getColor(context, R.color.azulFuerte)
        val bordeSeleccionado = ContextCompat.getColor(context, R.color.colorOnSecondary)

        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 10f

            // Día con actividad
            if (diasConActividades.contains(fecha)) {
                setColor(fondoActividad)
            } else {
                setColor(fondoNormal)
            }

            // Día seleccionado
            if (fechaSeleccionada != null && fecha == fechaSeleccionada) {
                setStroke(4, bordeSeleccionado) // borde de 4dp
            } else {
                setStroke(0, fondoNormal)
            }
        }

        holder.txtDia.background = drawable

        holder.itemView.setOnClickListener {
            val anteriorSeleccionado = fechaSeleccionada
            fechaSeleccionada = fecha
            notifyItemChanged(position)
            // Actualiza el anterior seleccionado para quitarle el borde
            anteriorSeleccionado?.let {
                val index = dias.indexOf(it)
                if (index != -1) notifyItemChanged(index)
            }
            onDiaClick(fecha)
        }
    }

    override fun getItemCount(): Int = dias.size
}
