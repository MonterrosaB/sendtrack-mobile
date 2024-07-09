package ptc24.st.sendtrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class olvidasteContrasena : AppCompatActivity() {
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

        val Correo = txtCorreoolvide.text.toString()

        var hayErrores = false
        val codigoRecuperacion = (1000..9999).random()


        btnEnviarCodigo.setOnClickListener{
            if (Correo.isEmpty()){
                txtCorreoolvide.error = "Ingrese el correo"
                hayErrores = true
            }

            else{
                txtCorreoolvide.error = null
            }

            if(!Correo.matches(Regex("[A-Za-z0-9+_.-]+ @ [a-z]+ [.] [a-z]+"))){
            txtCorreoolvide.error = "El correo no tiene un formato valido"
            hayErrores = true
            }
            else{
                txtCorreoolvide.error = null
            }

            CoroutineScope(Dispatchers.Main).launch {
                enviarCorreo(Correo, "Recuperacion de contraseña", "Este es tu codigo de recuperacion $codigoRecuperacion")
                val intent = Intent(this@olvidasteContrasena, verificacionCodigoCorreo::class.java)
                
            }
        }

    }
}