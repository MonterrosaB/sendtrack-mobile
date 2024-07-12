package ptc24.st.sendtrack

import Modelo.ClaseConexion
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class olvidasteContrasena : AppCompatActivity() {

    companion object envioCodigo{
        val codigoRecuperacion = (1000..9999).random()

        lateinit var Correo: String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_olvidaste_contrasena)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtCorreoolvide = findViewById<EditText>(R.id.txtEmailOlvide)
        val btnEnviarCodigo = findViewById<Button>(R.id.btnEnviarCodigo)



        var hayErrores = false


        btnEnviarCodigo.setOnClickListener{

             Correo = txtCorreoolvide.text.toString()
            if (Correo.isEmpty()){
                txtCorreoolvide.error = "Ingrese el correo"
                hayErrores = true
            }
            else if(!Correo.matches(Regex("[A-Za-z0-9+_.-]+@[a-z]+[.][a-z]+"))){
                txtCorreoolvide.error = "El correo no tiene un formato valido"
                hayErrores = true
            }
            else{
                txtCorreoolvide.error = null
            }


            if(!hayErrores){
                CoroutineScope(Dispatchers.IO).launch {

                    val objConexion = ClaseConexion().cadenaConexion()
                    var queryEjecutada: String? = null

                    val verificarCorreoCliente = objConexion?.prepareStatement("select * from Cliente where Email = ?")!!
                    verificarCorreoCliente.setString(1, Correo)
                    val resultadoCorreoCliente = verificarCorreoCliente.executeQuery()

                    val verificarCorreoEmpleado = objConexion?.prepareStatement("select * from Empleado where Email = ?")!!
                    verificarCorreoEmpleado.setString(1, Correo)
                    val resultadoCorreoEmpleado = verificarCorreoEmpleado.executeQuery()

                    if (resultadoCorreoCliente.next() == true) {
                        queryEjecutada = "Cliente"
                    } else if (resultadoCorreoEmpleado.next() == true) {
                        queryEjecutada = "Empleado"
                    }

                    if (queryEjecutada != null) {
                        Looper.prepare()
                        enviarCorreo(txtCorreoolvide.text.toString(), "Recuperacion de contraseña", "Este es tu codigo de recuperacion $codigoRecuperacion")
                        val intent = Intent(this@olvidasteContrasena, verificacionCodigoCorreo::class.java)
                        intent.putExtra("queryEjecutada", queryEjecutada)
                        startActivity(intent)
                    } else {
                        Looper.prepare()
                        Toast.makeText(this@olvidasteContrasena, "Correo no registrado", Toast.LENGTH_SHORT).show()
                    }





                }
            }


        }

    }
}