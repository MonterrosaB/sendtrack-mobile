package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.SHA256
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ptc24.st.sendtrack.databinding.ActivityRegistroBinding

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    var hayErrores = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNombre = findViewById<EditText>(R.id.txtNombreR)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefonoR)
        val txtEmail = findViewById<EditText>(R.id.txtEmailR)
        val txtUsuario = findViewById<EditText>(R.id.txtUsuarioR)
        val txtPass = findViewById<EditText>(R.id.txtPasswordR)
        val txtConPass = findViewById<EditText>(R.id.txtConfirmPasswordR)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
        val lbIniciarSesion = findViewById<TextView>(R.id.lbIniciarSesion)

        lbIniciarSesion.setOnClickListener {
            val pLogin = Intent(this,Login::class.java)
            startActivity(pLogin)
            finish()
        }

        btnRegistrarse.setOnClickListener{
            val nombre = binding.txtNombreR.text.toString()
            val telefono = binding.txtTelefonoR.text.toString()
            val email = binding.txtEmailR.text.toString()
            val usuario = binding.txtUsuarioR.text.toString()
            val contrasena = binding.txtPasswordR.text.toString()
            val confirmCont = binding.txtConfirmPasswordR.text.toString()

            val contraEncript = SHA256().hashSHA256(contrasena)

            if(nombre.isEmpty()){
                txtNombre.error = "Nombre obligatorio"
                hayErrores = true
            } else{
                txtNombre.error = null
            }
            if(contrasena.isEmpty()){
                txtPass.error = "Contraseña obligatoria"
                hayErrores = true
            }

            else if(!contrasena.matches(".*[A-Z].*".toRegex()))
            {
                txtPass.error = "Contraseña debe tener una mayuscula"
                hayErrores = true
            }
            else if(!contrasena.matches(".*[a-z].*".toRegex()))
            {
                txtPass.error = "Contraseña debe tener minusculas"
                hayErrores = true
            }
            else if(!contrasena.matches(".*[0-9].*".toRegex()))
            {
                txtPass.error = "Contraseña debe tener al menos 1 numero"
                hayErrores = true
            }
            else if(!contrasena.matches(".*[@#\$%^&+=_].*".toRegex()))
            {
                txtPass.error = "Contraseña debe tener un caracter espacial (@#\$%^&+=_)"
                hayErrores = true
            } else{
                txtPass.error = null
            }
            if(confirmCont.isEmpty()){
                txtConPass.error = " Confirmacion de Contraseña obligatoria"
                hayErrores = true
            }
            else{
                txtConPass.error = null
            }
            if(telefono.isEmpty()){
                txtTelefono.error = "Confirmacion de Contraseña obligatoria"
                hayErrores = true
            }
            else if(!telefono.matches(".*[0-9].*".toRegex()))
            {
                txtTelefono.error = "Ingresar digitos correctamente"
                hayErrores = true
            }
            else if(telefono.length != 8)
            {
                txtTelefono.error = "El telefono debe tener 8 digitos"
                hayErrores = true
            }
            else{
                txtTelefono.error = null
            }
            if(email.isEmpty()){
                txtEmail.error = "Correo obligatorio"
                hayErrores = true
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                txtEmail.error = "Correo electronico invalido"
                hayErrores = true
            }
            else{
                txtEmail.error = null
            }
            if(usuario.isEmpty()){
                txtUsuario.error = "Usuario obligatorio"
                hayErrores = true
            }
            else{
                txtEmail.error = null
            }

            if (hayErrores == false) {

                    if (contrasena == confirmCont && hayErrores == false || confirmCont == contrasena && hayErrores == false) {
                        CoroutineScope(Dispatchers.IO).launch {

                            val objConexion = ClaseConexion().cadenaConexion()

                            val addUser =
                                objConexion?.prepareStatement("insert into Cliente(NombreCompleto, Telefono, Email, Usuario, Contrasena) values(?, ?, ?, ?, ?)")!!
                            addUser.setString(1, nombre)
                            addUser.setString(2, telefono)
                            addUser.setString(3, email)
                            addUser.setString(4, usuario)
                            addUser.setString(5, contraEncript)
                            addUser.executeUpdate()
                        }
                        AlertDialog.Builder(this)
                            .setTitle("Registro completado")
                            .setMessage("Se ha agregado un paquete")
                            .setPositiveButton("Okay"){ _,_ ->

                                binding.txtNombreR.text = null
                                binding.txtTelefonoR.text = null
                                binding.txtEmailR.text = null
                                binding.txtUsuarioR.text = null
                                binding.txtPasswordR.text = null
                                binding.txtConfirmPasswordR.text = null
                            }
                            .show()
                    } else {
                        Toast.makeText(this, "Contraseña no coincide", Toast.LENGTH_SHORT).show()
                    }

            }
            else{
                AlertDialog.Builder(this)
                    .setTitle("Error de registro")
                    .setMessage("No ha sido posible completar el registro")
                    .setPositiveButton("Okay"){ _,_ ->

                        hayErrores = false
                    }
                    .show()
            }
        }
    }
}