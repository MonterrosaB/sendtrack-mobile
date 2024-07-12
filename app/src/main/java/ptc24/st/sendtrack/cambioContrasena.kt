package ptc24.st.sendtrack

import Modelo.ClaseConexion
import android.os.Bundle
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
import kotlinx.coroutines.withContext
import java.sql.SQLException

class cambioContrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cambio_contrasena)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val queryEjecutada = intent.getStringExtra("queryEjecutada")
        val correoRecibido = olvidasteContrasena.envioCodigo.Correo

        val txtNuevaContrasena = findViewById<EditText>(R.id.txtNuevaContrasena)
        val txtConfirmarNuevaContrasena = findViewById<EditText>(R.id.txtConfirmarNuevaContrasena)
        val btnContinuarNuevaContrasena = findViewById<Button>(R.id.btnContinuarNuevaContrasena)

        btnContinuarNuevaContrasena.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val nuevaContrasena = txtNuevaContrasena.text.toString()
                val confirmarContrasenaNueva = txtConfirmarNuevaContrasena.text.toString()

                val objConexion = ClaseConexion().cadenaConexion()

                if (nuevaContrasena == confirmarContrasenaNueva){
                    try {
                        val actualizarContrasena = when (queryEjecutada) {
                            "Cliente" -> objConexion?.prepareStatement("update Cliente set Contrasena = ? where Email = ?")
                            "Empleado" -> objConexion?.prepareStatement("update Usuario SET Contrasena = ? where Email = ?")
                            else -> null
                        }

                        if (actualizarContrasena != null) {
                            actualizarContrasena.setString(1, confirmarContrasenaNueva)
                            actualizarContrasena.setString(2, correoRecibido)

                            val rowsUpdated = actualizarContrasena.executeUpdate()

                            withContext(Dispatchers.Main) {
                                if (rowsUpdated > 0) {
                                    Toast.makeText(this@cambioContrasena, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@cambioContrasena, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } catch (e: SQLException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@cambioContrasena, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }
}