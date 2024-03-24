package net.azarquiel.chistes.model

import java.io.Serializable

data class Usuario (
    var id: Int,
    var nick: String,
    var pass: String
): Serializable
data class Categoria (
    var id: Int,
    var nombre: String
): Serializable
data class Chiste (
    var id: Int,
    var idcategoria:Int,
    var contenido: String
): Serializable
data class Punto (
    var id: Int,
    var idchiste: Int,
    var puntos: Int
)
data class Respuesta (
    var categorias: List<Categoria>,
    var chistes: List<Chiste>,
    var usuario:Usuario,
    var avg: String,
    var chiste: Chiste,
    var punto: Punto
)