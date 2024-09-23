package Modelo

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection? {

        try {
            val url = "jdbc:oracle:thin:@192.168.56.99:1521:xe"
            val usuario = "PTCSend"
            val contrasena = "1234"

            /*//Conexion Gerardo
            val url = "jdbc:oracle:thin:@192.168.1.21:1521:xe"
            val usuario = "PTSend"
            val contrasena = "1234"*/

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        }catch (e: Exception){
            println("error: $e")
            return null
        }
    }
}