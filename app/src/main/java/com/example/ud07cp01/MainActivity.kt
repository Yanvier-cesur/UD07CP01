package com.example.ud07cp01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ud07cp01.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PeliculaAdapter
    private val peliculas = mutableListOf<Pelicula>()

    private val editarLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val id = result.data?.getIntExtra("id", -1) ?: -1
                val nombre = result.data?.getStringExtra("nombre") ?: return@registerForActivityResult
                val pos = peliculas.indexOfFirst { it.id == id }
                if (pos != -1) {
                    peliculas[pos].nombre = nombre
                    adapter.notifyItemChanged(pos)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PeliculaAdapter(
            peliculas,
            onClick = { abrirDetalle(it) },
            onEditar = { abrirEditar(it) },
            onEliminar = { p, pos -> confirmarEliminar(p, pos) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener { recargarLista() }

        recargarLista()
    }

    private fun recargarLista() {
        peliculas.clear()
        peliculas.addAll(PeliculaProvider.listaCarga.map { it.copy() })
        adapter.notifyDataSetChanged()
        binding.swipeRefresh.isRefreshing = false
    }

    private fun abrirDetalle(pelicula: Pelicula) {
        val intent = Intent(this, DetalleActivity::class.java)
        intent.putExtra("pelicula", pelicula)
        startActivity(intent)
    }

    private fun abrirEditar(pelicula: Pelicula) {
        val intent = Intent(this, EditarActivity::class.java)
        intent.putExtra("pelicula", pelicula)
        editarLauncher.launch(intent)
    }

    private fun confirmarEliminar(pelicula: Pelicula, pos: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar ${pelicula.nombre}")
            .setMessage("¿Estás seguro de que quieres eliminar ${pelicula.nombre}?")
            .setNegativeButton("Cerrar", null)
            .setPositiveButton("Aceptar") { _, _ ->
                peliculas.removeAt(pos)
                adapter.notifyItemRemoved(pos)
            }
            .show()
    }

    private fun confirmarLimpiar() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar todas las películas")
            .setMessage("¿Estás seguro de que quieres eliminar la lista completa?")
            .setNegativeButton("Cerrar", null)
            .setPositiveButton("Aceptar") { _, _ ->
                peliculas.clear()
                adapter.notifyDataSetChanged()
                Snackbar.make(binding.root, "Se ha eliminado la lista completa", Snackbar.LENGTH_LONG).show()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_recargar -> { recargarLista(); true }
        R.id.action_limpiar -> { confirmarLimpiar(); true }
        else -> super.onOptionsItemSelected(item)
    }
}