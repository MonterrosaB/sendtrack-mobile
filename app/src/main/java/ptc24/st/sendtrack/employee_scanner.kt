package ptc24.st.sendtrack

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ptc24.st.sendtrack.databinding.FragmentEmployeeScannerBinding
import java.util.UUID

class employee_scanner : Fragment() {

    val codigo_opcion_galeria = 102
    val codigo_opcion_tomar_foto = 103
    val CAMERA_REQUEST_CODE = 0
    val STORAGE_REQUEST_CODE = 1

    lateinit var imageView: ImageView
    lateinit var txtCodigo: EditText

    val uuid = UUID.randomUUID().toString()

    private var _binding: FragmentEmployeeScannerBinding? = null
    private val binding get() = _binding!!


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
        _binding = FragmentEmployeeScannerBinding.inflate(inflater, container, false)
        val root = binding.root

        val btnSiguiente = binding.btnSiguiente
        val txtCodigo = binding.txtEditCodigoPaqueteR.text.toString()

        btnSiguiente.setOnClickListener {
            checkCameraPermission()
        }

        return root


    }

    private fun checkCameraPermission() {
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
        if (employee_scanner.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            )
        ) {
        } else {

            employee_scanner.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                777
            )
        }
    }

    private fun openCamera() {
        Toast.makeText(requireContext(), "Abriendo camara", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun shouldShowRequestPermissionRationale(
            employeeScanner: employee_scanner,
            camera: String
        ): Boolean {
            TODO("Not yet implemented")
        }

        fun requestPermissions(employeeScanner: employee_scanner, arrayOf: Array<String>, i: Int) {
            TODO("Not yet implemented")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 777) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
            }


            fun shouldShowRequestPermissionRationale(
                employeeScanner: employee_scanner,
                camera: String
            ): Boolean {

                return TODO("Provide the return value")
            }
        }
    }
}




