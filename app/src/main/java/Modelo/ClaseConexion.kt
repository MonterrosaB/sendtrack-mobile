package Modelo

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection? {

        try {
            val url = "jdbc:oracle:thin:@192.168.56.99:1521:xe"
            val usuario = "PTC24"
            val contrasena = "2244"

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        }catch (e: Exception){
            println("error: $e")
            return null
        }
    }
}