package ptc24.st.sendtrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ptc24.st.sendtrack.olvidasteContrasena.envioCodigo.codigoRecuperacion

class verificacionCodigoCorreo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verificacion_codigo_correo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val correoRecibido = olvidasteContrasena.envioCodigo.Correo
        val codigoRecibido = olvidasteContrasena.envioCodigo.codigoRecuperacion.toString()

        val queryEjecutada = intent.getStringExtra("queryEjecutada")

        val txtCodigoCorreo = findViewById<EditText>(R.id.txtCodigoCorreo)
        val lblVolverEnviarCorreo = findViewById<TextView>(R.id.lblVolverEnviarCodigo)
        val btnVerificarCodigo = findViewById<Button>(R.id.btnVerificarCodigo)
        val lblIndicacion = findViewById<TextView>(R.id.lblindicacion)

        val nuevoCodigo = (1000..9999).random()
        var hayErrores = false
        var intentos = 0


        lblVolverEnviarCorreo.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                enviarCorreo(correoRecibido,"Recuperacion de contraseña", "Este es tu codigo de recuperacion $nuevoCodigo")

                Toast.makeText(this@verificacionCodigoCorreo, "Codigo enviado correctamente", Toast.LENGTH_SHORT).show()
                lblIndicacion.text = "Si no recibiste el correo puedes"

            }
        }

        btnVerificarCodigo.setOnClickListener{
            intentos++
            if(intentos <= 3){
                val codigo = txtCodigoCorreo.text.toString()

                if(codigo.isEmpty()){
                    txtCodigoCorreo.error = "Ingresa el codigo enviado"
                    hayErrores = true
                }
                else{
                    txtCodigoCorreo.error = null
                }

                if(codigo == codigoRecibido || codigo == nuevoCodigo.toString()){
                    Toast.makeText(this@verificacionCodigoCorreo, "Codigo verificado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@verificacionCodigoCorreo, cambioContrasena::class.java)
                    intent.putExtra("queryEjecutada", queryEjecutada)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this@verificacionCodigoCorreo, "Codigo incorrecto. Intentos restantes: ${3- intentos}", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                lblIndicacion.text = "Haz alcanzado el limite de intentos "
                intentos = 0
            }
        }


    }
}