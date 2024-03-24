package net.azarquiel.chistes.view

import android.os.Bundle
import android.text.Html
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.squareup.picasso.Picasso
import net.azarquiel.chistes.R
import net.azarquiel.chistes.databinding.ActivityDetailChisteBinding
import net.azarquiel.chistes.model.Categoria
import net.azarquiel.chistes.model.Chiste
import net.azarquiel.chistes.model.Punto
import net.azarquiel.chistes.model.Usuario
import net.azarquiel.chistes.viewmodel.MainViewModel

class DetailChisteActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var categoria: Categoria
    private var usuario: Usuario?=null
    private lateinit var chiste: Chiste
    private lateinit var binding: ActivityDetailChisteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailChisteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        chiste = intent.getSerializableExtra("chiste") as Chiste
        usuario = intent.getSerializableExtra("usuario") as Usuario?
        categoria = intent.getSerializableExtra("categoria") as Categoria

        binding.cdc.rbvotardetail.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            newRating(rating)
        }

        showChiste()

    }

    private fun newRating(rating: Float) {
        viewModel.savePunto(chiste.id, Punto(0, chiste.id, rating.toInt())).observe(this, Observer {
            it?.let{
                showAVG()
                binding.cdc.rbvotardetail.isEnabled = false
            }
        })
    }

    private fun showChiste() {
        binding.cdc.tvcategoriadetail.text = categoria.nombre+"-"+chiste.id
        binding.cdc.tvcontenidodetail.text = Html.fromHtml(chiste.contenido)
        Picasso.get().load("http://www.ies-azarquiel.es/paco/apichistes/img/${categoria.id}.png").into(binding.cdc.ivdetail)
        showAVG()

    }

    private fun showAVG() {
        viewModel.getAvgChiste(chiste.id).observe(this, Observer {
            it?.let{
                binding.cdc.rbavgdetail.rating = it.toFloat()
            }
        })
    }
}