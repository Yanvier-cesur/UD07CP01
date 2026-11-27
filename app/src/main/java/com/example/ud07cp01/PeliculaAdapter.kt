package com.example.ud07cp01

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ud07cp01.databinding.ItemPeliculaBinding

class PeliculaAdapter(
    private val peliculas: MutableList<Pelicula>,
    private val onClick: (Pelicula) -> Unit,
    private val onEditar: (Pelicula) -> Unit,
    private val onEliminar: (Pelicula, Int) -> Unit
) : RecyclerView.Adapter<PeliculaAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPeliculaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pelicula: Pelicula) {
            binding.tvNombre.text = pelicula.nombre
            binding.ivPoster.setImageResource(pelicula.imagen)

            binding.root.setOnClickListener { onClick(pelicula) }

            binding.root.setOnCreateContextMenuListener { menu, _, _ ->
                menu.setHeaderTitle(pelicula.nombre)
                menu.add(Menu.NONE, 1, 0, "Eliminar").setOnMenuItemClickListener {
                    onEliminar(pelicula, bindingAdapterPosition)
                    true
                }
                menu.add(Menu.NONE, 2, 1, "Editar").setOnMenuItemClickListener {
                    onEditar(pelicula)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPeliculaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(peliculas[position])

    override fun getItemCount() = peliculas.size
}