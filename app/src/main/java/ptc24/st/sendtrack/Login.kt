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

class Login : AppCompatActivity() {

    companion object variablesGlobalesLogin{
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

        btnIniciarSesion.setOnClickListener{

            CoroutineScope(Dispatchers.IO).launch {
             correoIngresado
            }
        }

        lblOlvidasteContraLogin.setOnClickListener{
            val intent = Intent(this@Login, olvidasteContrasena::class.java )
            startActivity(intent)
        }
    }
}