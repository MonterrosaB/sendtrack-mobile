package Modelo

data class dtDireccion(
    val idDireccion: String,
    val idCliente: String,
    var nombre: String,
    var direccion: String,
    var instruccion: String,
    var distrito: String,
    var municipio: String
)
