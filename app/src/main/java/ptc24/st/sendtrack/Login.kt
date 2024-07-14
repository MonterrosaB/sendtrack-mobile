package ptc24.st.sendtrack

import Modelo.ClaseConexion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    companion object variablesGlobalesLogin {
        lateinit var correoIngresado: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtEmailLogin = findViewById<EditText>(R.id.txt)
        val txtContrasenaLogin = findViewById<EditText>(R.id.txtContrasenaLogin)
        val lblOlvidasteContraLogin = findViewById<TextView>(R.id.lblOlvidasteContraLogin)
        val ivConectarGoogleLogin = findViewById<ImageView>(R.id.ivConectarGoogleLogin)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)


        btnIniciarSesion.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                correoIngresado = txtEmailLogin.text.toString()
                val contrasenaIngresada = txtContrasenaLogin.text.toString()
                val objConexion = ClaseConexion().cadenaConexion()
                var queryEjecutada: String? = null

                val rolCliente =
                    objConexion?.prepareStatement("Select * from Clientes where Email = ? and Contrasena = ?")!!
                rolCliente.setString(1, correoIngresado)
                rolCliente.setString(2, contrasenaIngresada)
                val resultadoCliente = rolCliente.executeQuery()


                val rolEmpleado = objConexion?.prepareStatement(
                    "SELECT E.IdRol FROM Usuario U JOIN \n" +
                            "Empleado E ON U.DUIEmp = E.DUI \n" +
                            "WHERE E.Email = ? AND U.Contrasena = ?;"
                )!!
                rolEmpleado.setString(1, correoIngresado)
                rolEmpleado.setString(2, contrasenaIngresada)
                val resultadoEmpleado = rolEmpleado.executeQuery()

                if (resultadoCliente.next() == true) {
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@Login, main_employee::class.java)
                    }
                } else if (resultadoEmpleado.next() == true) {
                    val idRol = resultadoEmpleado.getInt("IdRol")
                    withContext(Dispatchers.Main) {
                        if (idRol == 1) {
                            val intent = Intent(this@Login, main_admin::class.java)
                            startActivity(intent)
                        } else if (idRol == 2) {
                            val intent = Intent(this@Login, main_employee::class.java)
                            startActivity(intent)
                        } else if (idRol == 3) {
                            //Repartidor
                        }

                    }
                }

                lblOlvidasteContraLogin.setOnClickListener {
                    val intent = Intent(this@Login, olvidasteContrasena::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
