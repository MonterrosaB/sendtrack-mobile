package Modelo

data class dtEmpleados(
    val dui: String,
    val nombre: String,
    val apellidoPa: String,
    val apellidoMa: String,
    val Email: String,
    val Salario: Double,
    val FechaNa: String,
    val Rol: Int,
    val Sucursal: Int,
    val Masculino: Int,
    val Estado: Int,
    val Telefono: String
)
