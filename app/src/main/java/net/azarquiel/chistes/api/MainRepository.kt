package net.azarquiel.chistes.api

import net.azarquiel.chistes.model.Categoria
import net.azarquiel.chistes.model.Chiste
import net.azarquiel.chistes.model.Punto
import net.azarquiel.chistes.model.Usuario

/**
 * Created by Paco Pulido.
 */

class MainRepository() {
    val service = WebAccess.chisteService

    suspend fun getCategorias(): List<Categoria> {
        val webResponse = service.getCategorias().await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.categorias
        }
        return emptyList()
    }
    suspend fun getChistesByCategoria(idcategoria:Int): List<Chiste> {
        val webResponse = service.getChistesByCategoria(idcategoria).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.chistes
        }
        return emptyList()
    }
    suspend fun getAvgChiste(idchiste:Int): String {
        val webResponse = service.getAvgChiste(idchiste).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.avg
        }
        return ""
    }
    suspend fun getDataUsuarioPorNickPass(nick:String, pass:String): Usuario? {
        val webResponse = service.getDataUsuarioPorNickPass(nick, pass).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.usuario
        }
        return null
    }


    suspend fun saveUsuario(usuario: Usuario): Usuario? {
        var usuarioResponse:Usuario? = null
        val webResponse = service.saveUsuario(usuario).await()
        if (webResponse.isSuccessful) {
            usuarioResponse = webResponse.body()!!.usuario
        }
        return usuarioResponse
    }
    suspend fun saveChiste(chiste: Chiste): Chiste? {
        var chisteResponse:Chiste? = null
        val webResponse = service.saveChiste(chiste).await()
        if (webResponse.isSuccessful) {
            chisteResponse = webResponse.body()!!.chiste
        }
        return chisteResponse
    }
    suspend fun savePunto(idchiste: Int, punto:Punto): Punto? {
        var puntoResponse:Punto? = null
        val webResponse = service.savePunto(idchiste, punto).await()
        if (webResponse.isSuccessful) {
            puntoResponse = webResponse.body()!!.punto
        }
        return puntoResponse
    }


// ……..   resto de métodos del Repository ………..
}
