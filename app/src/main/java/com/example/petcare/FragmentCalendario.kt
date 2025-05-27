package com.example.petcare

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class FragmentCalendario : Fragment(R.layout.fragment_calendario) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerEventosMes: RecyclerView
    private lateinit var txtEventosDia: TextView
    private lateinit var db: AppDatabase
    private var mesActual: YearMonth = YearMonth.now()
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerCalendario)
        recyclerEventosMes = view.findViewById(R.id.recyclerEventosMes)
        txtEventosDia = view.findViewById(R.id.txtEventosDia)
        db = AppDatabase.getDatabase(requireContext())

        recyclerEventosMes.layoutManager = LinearLayoutManager(requireContext())

        val btnAnterior = view.findViewById<Button>(R.id.btnMesAnterior)
        val btnSiguiente = view.findViewById<Button>(R.id.btnMesSiguiente)
        val txtMes = view.findViewById<TextView>(R.id.txtMesActual)

        actualizarCalendario(mesActual, txtMes)

        btnAnterior.setOnClickListener {
            mesActual = mesActual.minusMonths(1)
            actualizarCalendario(mesActual, txtMes)
        }

        btnSiguiente.setOnClickListener {
            mesActual = mesActual.plusMonths(1)
            actualizarCalendario(mesActual, txtMes)
        }

        // ⛔ No mostrar actividades al iniciar
        txtEventosDia.text = ""
        recyclerEventosMes.adapter = null
    }

    private fun generarDiasDelMes(fecha: LocalDate): List<LocalDate> {
        val primerDia = fecha.withDayOfMonth(1)
        val dias = mutableListOf<LocalDate>()
        val totalDias = primerDia.lengthOfMonth()
        for (i in 0 until totalDias) {
            dias.add(primerDia.plusDays(i.toLong()))
        }
        return dias
    }

    private fun actualizarCalendario(mes: YearMonth, txtMes: TextView) {
        val diasDelMes = generarDiasDelMes(mes.atDay(1))
        txtMes.text = mes.month.getDisplayName(TextStyle.FULL, Locale("es")) + " ${mes.year}"

        lifecycleScope.launch {
            val usuarioId = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE)
                .getInt("usuarioId", -1)

            val mascotas = db.mascotaDao().obtenerPorUsuario(usuarioId)
            val actividades = mascotas.flatMap { db.actividadDao().obtenerPorMascota(it.id) }

            val diasConActividades = actividades.map {
                LocalDate.parse(it.fecha, formatter)
            }

            recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
            recyclerView.adapter = CalendarioAdapter(diasDelMes, diasConActividades) { fecha ->
                mostrarActividadesDelDia(fecha)
            }

            // ⛔ Evitar mostrar actividades al cargar el mes
            txtEventosDia.text = ""
            recyclerEventosMes.adapter = null
        }
    }

    private fun mostrarActividadesDelDia(fechaSeleccionada: LocalDate) {
        lifecycleScope.launch {
            val prefs = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE)
            val usuarioId = prefs.getInt("usuarioId", -1)

            val mascotas = db.mascotaDao().obtenerPorUsuario(usuarioId)

            val actividadesDelDia = mascotas.flatMap { mascota ->
                db.actividadDao().obtenerPorMascota(mascota.id).mapNotNull { actividad ->
                    if (actividad.fecha == formatter.format(fechaSeleccionada)) {
                        Triple(actividad.titulo, actividad.descripcion, mascota.nombre)
                    } else null
                }
            }

            if (actividadesDelDia.isNotEmpty()) {
                txtEventosDia.text = "Actividades del ${formatter.format(fechaSeleccionada)}"
                recyclerEventosMes.adapter = ActividadesDelDiaAdapter(actividadesDelDia)
            } else {
                txtEventosDia.text = "No hay actividades para el ${formatter.format(fechaSeleccionada)}"
                recyclerEventosMes.adapter = ActividadesDelDiaAdapter(emptyList())
            }
        }
    }

    private fun cargarEventosDelMes(fecha: LocalDate, recycler: RecyclerView) {
        lifecycleScope.launch {
            val usuarioId = requireContext()
                .getSharedPreferences("sesion", Context.MODE_PRIVATE)
                .getInt("usuarioId", -1)

            val mascotas = db.mascotaDao().obtenerPorUsuario(usuarioId)
            val actividades = mascotas.flatMap { mascota ->
                db.actividadDao().obtenerPorMascota(mascota.id)
            }

            val actividadesDelMes = actividades.filter {
                val actividadFecha = LocalDate.parse(it.fecha, formatter)
                actividadFecha.month == fecha.month && actividadFecha.year == fecha.year
            }.map {
                Pair(it.fecha, it.titulo)
            }

            recycler.adapter = EventosMesAdapter(actividadesDelMes)
        }
    }
}
