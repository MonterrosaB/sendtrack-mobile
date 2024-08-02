package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.SHA256
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    companion object variablesGlobalesLogin {
        lateinit var correoIngresado: String
        lateinit var idUser: String
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val txtEmailLogin = findViewById<EditText>(R.id.txtEditEmail)
        val txtContrasenaLogin = findViewById<EditText>(R.id.txtContrasenaLogin)
        val lblOlvidasteContraLogin = findViewById<TextView>(R.id.lblOlvidasteContraLogin)
        //val ivConectarGoogleLogin = findViewById<ImageView>(R.id.ivConectarGoogleLogin)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val lbRegistro = findViewById<TextView>(R.id.lbRegister)

        lbRegistro.setOnClickListener {
            val registro = Intent(this,Registro::class.java)
            startActivity(registro)
        }


        btnIniciarSesion.setOnClickListener {
            var hayErrores = false
            if(txtEmailLogin.text.isEmpty()){
                txtEmailLogin.error = "Campo obligatorio"
                hayErrores = true
            }else{
                txtEmailLogin.error = null
            }
            if(txtContrasenaLogin.text.isEmpty()){
                txtContrasenaLogin.error = "Campo obligatorio"
                hayErrores = true
            }else{
                txtContrasenaLogin.error = null
            }
                if (!hayErrores){
                    GlobalScope.launch(Dispatchers.IO) {
                        correoIngresado = txtEmailLogin.text.toString()
                        val contrasenaIngresada = SHA256().hashSHA256(txtContrasenaLogin.text.toString())
                        val objConexion = ClaseConexion().cadenaConexion()
                        var queryEjecutada: String? = null

                        val rolCliente =
                            objConexion?.prepareStatement("Select * from Cliente where Email = ? and Contrasena = ?")!!
                        rolCliente.setString(1, correoIngresado)
                        rolCliente.setString(2, contrasenaIngresada)
                        val resultadoCliente = rolCliente.executeQuery()


                        val rolEmpleado = objConexion?.prepareStatement(
                            "SELECT E.IdRol, U.IdUsuario FROM Usuario U " +
                                    "INNER JOIN Empleado E ON U.DUI = E.DUI " +
                                    "WHERE E.Email = ? AND U.Contrasena = ?"
                        )!!
                        rolEmpleado.setString(1, correoIngresado)
                        rolEmpleado.setString(2, contrasenaIngresada)
                        val resultadoEmpleado = rolEmpleado.executeQuery()

                        if (resultadoCliente.next() == true) {

                            val intent = Intent(this@Login, main_user::class.java)
                            idUser = resultadoCliente.getString("IdCliente")
                            startActivity(intent)
                            withContext(Dispatchers.Main){
                                txtContrasenaLogin.text = null
                                txtContrasenaLogin.text = null
                            }
                        } else if (resultadoEmpleado.next() == true) {
                            val idRol = resultadoEmpleado.getInt("IdRol")
                            if (idRol == 1) {
                                val intent = Intent(this@Login, main_admin::class.java)
                                idUser = resultadoEmpleado.getString("IdUsuario")
                                startActivity(intent)
                                withContext(Dispatchers.Main){
                                    txtContrasenaLogin.text = null
                                    txtContrasenaLogin.text = null
                                }
                            } else if (idRol == 2) {
                                val intent = Intent(this@Login, main_employeein::class.java)
                                idUser = resultadoEmpleado.getString("IdUsuario")
                                startActivity(intent)
                                withContext(Dispatchers.Main){
                                    txtContrasenaLogin.text = null
                                    txtContrasenaLogin.text = null
                                }
                            } else if (idRol == 3) {
                                val intent = Intent(this@Login, main_employee::class.java)
                                idUser = resultadoEmpleado.getString("IdUsuario")
                                startActivity(intent)
                                withContext(Dispatchers.Main){
                                    txtContrasenaLogin.text = null
                                    txtContrasenaLogin.text = null
                                }
                            }
                        }
                    }
                }


        }
        lblOlvidasteContraLogin.setOnClickListener {
            val intent = Intent(this@Login, olvidasteContrasena::class.java)
            startActivity(intent)
        }
    }
}
