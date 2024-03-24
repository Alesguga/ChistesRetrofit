package net.azarquiel.chistes.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import net.azarquiel.chistes.R
import net.azarquiel.chistes.adapters.AdapterChistes
import net.azarquiel.chistes.databinding.ActivityChistesBinding
import net.azarquiel.chistes.model.Categoria
import net.azarquiel.chistes.model.Chiste
import net.azarquiel.chistes.model.Usuario
import net.azarquiel.chistes.viewmodel.MainViewModel

class ChistesActivity : AppCompatActivity() {

    private lateinit var adapter: AdapterChistes
    private lateinit var chistes: List<Chiste>
    private lateinit var viewModel: MainViewModel
    private var usuario: Usuario?=null
    private lateinit var categoria: Categoria
    private lateinit var binding: ActivityChistesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChistesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        categoria = intent.getSerializableExtra("categoria") as Categoria
        usuario = intent.getSerializableExtra("usuario") as Usuario?
        initRV()
        getChistes()

        binding.fab.setOnClickListener {
            dialogAddChiste()
        }

    }

    private fun dialogAddChiste() {
        if (usuario==null) {
            msg("No login, no chiste....")
            return
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Chiste")
        val ll = LinearLayout(this)
        ll.setPadding(30,30,30,30)
        ll.orientation = LinearLayout.VERTICAL

        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(0,50,0,50)

        val textInputLayoutContent = TextInputLayout(this)
        textInputLayoutContent.layoutParams = lp
        val etcontenido = EditText(this)
        etcontenido.setPadding(0, 80, 0, 80)
        etcontenido.textSize = 20.0F
        etcontenido.hint = "New Chiste"
        textInputLayoutContent.addView(etcontenido)

        ll.addView(textInputLayoutContent)

        builder.setView(ll)

        builder.setPositiveButton("Aceptar") { dialog, which ->
            if(!etcontenido.text.toString().isEmpty()) {
                addchiste(etcontenido.text.toString())
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
        }

        builder.show()

    }

    private fun msg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    }

    private fun addchiste(contenido: String) {
        val chiste = Chiste(0, categoria.id, contenido)
        viewModel.saveChiste(chiste).observe(this, Observer {
            it?.let{
                val chistesaux = ArrayList(chistes)
                chistesaux.add(0, it)
                chistes = chistesaux
                adapter.setChistes(chistes)
            }
        })
    }

    private fun getChistes() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getChistesByCategoria(categoria.id).observe(this, Observer {
            it?.let{
                chistes = it
                adapter.setChistes(it)
            }
        })
    }

    private fun initRV() {
        adapter = AdapterChistes(this, R.layout.rowchiste)
        binding.cc.rvchistes.adapter = adapter
        binding.cc.rvchistes.layoutManager = LinearLayoutManager(this)
    }

    fun onClickChiste(v: View) {
        val chiste = v.tag as Chiste
        val intent = Intent(this, DetailChisteActivity::class.java)
        intent.putExtra("chiste", chiste)
        intent.putExtra("usuario", usuario)
        intent.putExtra("categoria", categoria)
        startActivity(intent)
    }

}