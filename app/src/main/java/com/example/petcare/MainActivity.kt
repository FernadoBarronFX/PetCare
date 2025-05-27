package com.example.petcare

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonAjustes = findViewById<ImageView>(R.id.btnAjustes)
        botonAjustes.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, AjustesFragment())
                .addToBackStack(null)
                .commit()
        }

        // Carga el fragmento inicial (mascotas)
        if (savedInstanceState == null) {
            cargarFragmento(FragmentMascotas())
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_mascotas -> {
                    cargarFragmento(FragmentMascotas())
                    true
                }
                R.id.nav_actividades -> {
                    cargarFragmento(FragmentActividades())
                    true
                }
                R.id.nav_calendario -> {
                    cargarFragmento(FragmentCalendario())
                    true
                }
                R.id.nav_notificaciones -> {
                    cargarFragmento(FragmentNotificaciones())
                    true
                }
                else -> false
            }
        }
    }

    private fun cargarFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, fragmento)
            .commit()
    }
}
