package net.azarquiel.chistes.api

import kotlinx.coroutines.Deferred
import net.azarquiel.chistes.model.Chiste
import net.azarquiel.chistes.model.Punto
import net.azarquiel.chistes.model.Respuesta
import net.azarquiel.chistes.model.Usuario
import retrofit2.Response
import retrofit2.http.*
/**
 * Created by Paco Pulido.
 */
interface ChisteService {
    // No necesita nada para trabajar
    @GET("categorias")
    fun getCategorias(): Deferred<Response<Respuesta>>

    // variable idbar en la ruta de la url => @Path
    @GET("categoria/{idcategoria}/chistes")
    fun getChistesByCategoria(@Path("idcategoria") idcategoria: Int): Deferred<Response<Respuesta>>

    // nick y pass variables sueltas en la url?nick=paco&pass=paco => @Query
    @GET("usuario")
    fun getDataUsuarioPorNickPass(
        @Query("nick") nick: String,
        @Query("pass") pass: String): Deferred<Response<Respuesta>>

    // post con objeto => @Body
    @POST("usuario")
    fun saveUsuario(@Body usuario: Usuario): Deferred<Response<Respuesta>>

    @GET("chiste/{idchiste}/avgpuntos")
    fun getAvgChiste(@Path("idchiste") idchiste: Int): Deferred<Response<Respuesta>>

    @POST("chiste")
    fun saveChiste(@Body chiste: Chiste): Deferred<Response<Respuesta>>

    @POST("chiste/{idchiste}/punto")
    fun savePunto(@Path("idchiste") idchiste: Int,
                   @Body punto: Punto): Deferred<Response<Respuesta>>

}
