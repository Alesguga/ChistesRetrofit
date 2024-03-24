package net.azarquiel.chistes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.azarquiel.chistes.api.MainRepository
import net.azarquiel.chistes.model.Categoria
import net.azarquiel.chistes.model.Chiste
import net.azarquiel.chistes.model.Punto
import net.azarquiel.chistes.model.Usuario

// ……

/**
 * Created by Paco Pulido.
 */
class MainViewModel : ViewModel() {

    private var repository: MainRepository = MainRepository()

    fun getCategorias(): MutableLiveData<List<Categoria>> {
        val categorias = MutableLiveData<List<Categoria>>()
        GlobalScope.launch(Main) {
            categorias.value = repository.getCategorias()
        }
        return categorias
    }
    fun getChistesByCategoria(idcategoria:Int): MutableLiveData<List<Chiste>> {
        val chistes = MutableLiveData<List<Chiste>>()
        GlobalScope.launch(Main) {
            chistes.value = repository.getChistesByCategoria(idcategoria)
        }
        return chistes
    }
    fun getAvgChiste(idchiste:Int): MutableLiveData<String> {
        val avg = MutableLiveData<String>()
        GlobalScope.launch(Main) {
            avg.value = repository.getAvgChiste(idchiste)
        }
        return avg
    }
    fun getDataUsuarioPorNickPass(nick:String, pass: String): MutableLiveData<Usuario> {
        val usuario = MutableLiveData<Usuario>()
        GlobalScope.launch(Main) {
            usuario.value = repository.getDataUsuarioPorNickPass(nick, pass)
        }
        return usuario
    }

    fun saveChiste(chiste:Chiste):MutableLiveData<Chiste> {
        val chisteResponse= MutableLiveData<Chiste>()
        GlobalScope.launch(Main) {
            chisteResponse.value = repository.saveChiste(chiste)
        }
        return chisteResponse
    }

    fun saveUsuario(usuario: Usuario):MutableLiveData<Usuario> {
        val usuarioResponse= MutableLiveData<Usuario>()
        GlobalScope.launch(Main) {
            usuarioResponse.value = repository.saveUsuario(usuario)
        }
        return usuarioResponse
    }

    fun savePunto(idchiste: Int, punto: Punto):MutableLiveData<Punto> {
        val puntoResponse= MutableLiveData<Punto>()
        GlobalScope.launch(Main) {
            puntoResponse.value = repository.savePunto(idchiste, punto)
        }
        return puntoResponse
    }
}
