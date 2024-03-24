package net.azarquiel.chistes.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import net.azarquiel.chistes.R
import net.azarquiel.chistes.adapters.AdapterCategorias
import net.azarquiel.chistes.databinding.ActivityMainBinding
import net.azarquiel.chistes.model.Categoria
import net.azarquiel.chistes.model.Usuario
import net.azarquiel.chistes.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var categorias: List<Categoria>
    private var titulo: String=""
    private lateinit var sh: SharedPreferences
    private var usuario: Usuario?=null
    private lateinit var adapter: AdapterCategorias
    private lateinit var binding: ActivityMainBinding
    private  lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        titulo = title.toString()
        sh = getSharedPreferences("login", Context.MODE_PRIVATE)

        getUsuarioSH()
        initRV()

        getCategorias()
    }

    private fun getUsuarioSH() {
        val usuarioJson = sh.getString("usuario", null)
        if (usuarioJson!=null) {
            usuario = Gson().fromJson(usuarioJson, Usuario::class.java)
            msg("Welcome ${usuario!!.nick}...")
            showTitle()
        }
    }

    private fun msg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun getCategorias() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getCategorias().observe(this, Observer {
            it?.let{
                categorias = it
                adapter.setCategorias(it)
            }
        })
    }

    private fun initRV() {
        adapter = AdapterCategorias(this, R.layout.rowcategoria)
        binding.cm.rvcategorias.adapter = adapter
        binding.cm.rvcategorias.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Search...")
        searchView.setOnQueryTextListener(this)
// ************* </Filtro> ************

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_login -> {
                onClickLoginRegister()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onClickLoginRegister() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login/Register")
        val ll = LinearLayout(this)
        ll.setPadding(30,30,30,30)
        ll.orientation = LinearLayout.VERTICAL

        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(0,50,0,50)

        val textInputLayoutNick = TextInputLayout(this)
        textInputLayoutNick.layoutParams = lp
        val etnick = EditText(this)
        etnick.setPadding(0, 80, 0, 80)
        etnick.textSize = 20.0F
        etnick.hint = "Nick"
        textInputLayoutNick.addView(etnick)

        val textInputLayoutPass = TextInputLayout(this)
        textInputLayoutPass.layoutParams = lp
        val etpass = EditText(this)
        etpass.setPadding(0, 80, 0, 80)
        etpass.textSize = 20.0F
        etpass.hint = "Pass"
        textInputLayoutPass.addView(etpass)
        ll.addView(textInputLayoutNick)
        ll.addView(textInputLayoutPass)
        builder.setView(ll)

        builder.setPositiveButton("Aceptar") { dialog, which ->
            login(etnick.text.toString(), etpass.text.toString())
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
        }

        builder.show()
    }

    private fun login(nick: String, pass: String) {
        viewModel.getDataUsuarioPorNickPass(nick, pass).observe(this, Observer {
            if (it!=null){
                msg("Login ok...")
                saveSH(it)
            }
            else {
                viewModel.saveUsuario(Usuario(0, nick, pass)).observe(this, Observer {
                    saveSH(it)
                    msg("Registrado usuario...")
                })
            }
        })
    }

    private fun saveSH(usuario: Usuario) {
        this.usuario = usuario
        val editor = sh.edit()
        editor.putString("usuario", Gson().toJson(usuario))
        editor.commit()
        showTitle()
    }

    private fun showTitle() {
        binding.ctb.title = "$titulo - ${usuario!!.nick}"
    }
    // ************* <Filtro> ************
    override fun onQueryTextChange(query: String): Boolean {
        val original = ArrayList<Categoria>(categorias)
        adapter.setCategorias(original.filter { categoria -> categoria.nombre.contains(query,true) })
        return false
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        return false
    }
// ************* </Filtro> ************
    fun onClickCategoria(v:View){
        val categoriaPulsada = v.tag as Categoria
        val intent = Intent(this, ChistesActivity::class.java)
        intent.putExtra("categoria", categoriaPulsada)
        intent.putExtra("usuario", usuario)
        startActivity(intent)
    }

}