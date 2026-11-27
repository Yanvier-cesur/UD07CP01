package com.example.ud07cp01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.example.ud07cp01.databinding.ActivityDetalleBinding

class DetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val p = IntentCompat.getSerializableExtra(intent, "pelicula", Pelicula::class.java) ?: return

        binding.tvTitulo.text = p.nombre
        binding.ivPoster.setImageResource(p.imagen)
        binding.tvDescripcion.text = "Descripción: ${p.descripcion}"
        binding.tvAnyo.text = "Año: ${p.anyo}"
        binding.tvDuracion.text = "Duración: ${p.duracion} minutos"
        binding.tvNacionalidad.text = "Nacionalidad: ${p.nacionalidad}"
    }
}