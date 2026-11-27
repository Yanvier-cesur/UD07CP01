package com.example.ud07cp01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.example.ud07cp01.databinding.ActivityEditarBinding

class EditarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val p = IntentCompat.getSerializableExtra(intent, "pelicula", Pelicula::class.java) ?: return

        binding.ivPoster.setImageResource(p.imagen)
        binding.etNombre.setText(p.nombre)

        binding.btnCambiar.setOnClickListener {
            val nuevo = binding.etNombre.text.toString().trim()
            if (nuevo.isEmpty()) {
                binding.etNombre.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }
            val resultado = Intent().apply {
                putExtra("id", p.id)
                putExtra("nombre", nuevo)
            }
            setResult(Activity.RESULT_OK, resultado)
            finish()
        }

        binding.btnCancelar.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}