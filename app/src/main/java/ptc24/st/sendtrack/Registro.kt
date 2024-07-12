package ptc24.st.sendtrack

import Modelo.ClaseConexion
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ptc24.st.sendtrack.databinding.ActivityRegistroBinding
import java.util.UUID

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrarse)


        btnRegistrar.setOnClickListener {
            val txtNombre = binding.txtNombreR.text.toString()
            val txtTelefono = binding.txtTelefonoR.text.toString()
            val txtEmail = binding.txtEmailR.text.toString()
            val txtUsuario = binding.txtUsuarioR.text.toString()
            val txtContrasena = binding.txtPasswordR.text.toString()
            val txtConfirmCont = binding.txtConfirmPasswordR.text.toString()


            if(txtContrasena == txtConfirmCont || txtConfirmCont == txtContrasena){
                CoroutineScope(Dispatchers.IO).launch {

                    val objConexion = ClaseConexion().cadenaConexion()

                    val addUser =objConexion?.prepareStatement("insert into Cliente(idCliente, NombreCompleto, Telefono, Email, Usuario, Contrasena) values(?, ?, ?, ?, ?, ?)")!!
                    addUser.setString(1, UUID.randomUUID().toString())
                    addUser.setString(2, txtNombre)
                    addUser.setString(3, txtTelefono)
                    addUser.setString(4, txtEmail)
                    addUser.setString(5, txtUsuario)
                    addUser.setString(6, txtContrasena)
                    addUser.executeUpdate()

                }


            }
            else {
                Toast.makeText(this, "Contraseña no coincide", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun emailFocusListener(){
        binding.txtEmailR.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                binding.EmailR.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.txtEmailR.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Correo electronico invalido"
        }

        return null
    }
}