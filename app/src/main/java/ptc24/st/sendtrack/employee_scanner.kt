package ptc24.st.sendtrack

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ptc24.st.sendtrack.databinding.FragmentEmployeeScannerBinding
import java.util.UUID

class employee_scanner : Fragment() {

    lateinit var txtCodigo: EditText
    val codigo_opcion_tomar_foto = 103
    val CAMERA_REQUEST_CODE = 0
    val STORAGE_REQUEST_CODE = 1
    val codigo_opcion_galeria = 102


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_scanner, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    val btnSiguiente = view.findViewById<Button>(R.id.btnSiguiente)

        btnSiguiente.setOnClickListener {
            //Al darle clic al botón de la galeria pedimos los permisos primero
            checkStoragePermission()
        }



    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //El permiso no está aceptado, entonces se lo pedimos
            pedirPermisoCamara()
        } else {
            //El permiso ya está aceptado
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, codigo_opcion_tomar_foto)
        }
    }



    private fun pedirPermisoCamara() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                android.Manifest.permission.CAMERA
            )
        ) {
            //El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
            )
        }
    }

    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //El permiso está aceptado, entonces Abrimos la camara:
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, codigo_opcion_tomar_foto)
                } else {
                    //El usuario ha rechazado el permiso, podemos desactivar la funcionalidad o mostrar una alerta/Toast.
                    Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                }
                return
            }

            STORAGE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //El permiso está aceptado, entonces Abrimos la galeria
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria)
                } else {
                    //El usuario ha rechazado el permiso, podemos desactivar la funcionalidad o mostrar una alerta/Toast.
                    Toast.makeText(requireContext(), "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            else -> {
                // Este else lo dejamos por si sale un permiso que no teníamos controlado.
            }
        }
    }


}




