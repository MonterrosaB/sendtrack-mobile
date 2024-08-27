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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import ptc24.st.sendtrack.databinding.ActivityMainEmployeeBinding
import ptc24.st.sendtrack.databinding.FragmentEmployeeScannerBinding
import java.util.UUID

class employee_scanner : Fragment() {

    val CAMERA_REQUEST_CODE = 0
    private lateinit var binding: FragmentEmployeeScannerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        arguments?.let {

        }
    }

    private fun initView() {
        binding.root.setOnClickListener{
            checkPermissionCamera(this)
        }
    }

    private fun checkPermissionCamera(employeeScanner: employee_scanner) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ){
            showCamera()

        }
        else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            Toast.makeText(requireContext(), "Camara permiso requerido", Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_employee_scanner, container, false)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted: Boolean ->
                if (isGranted){
                    showCamera()

                }
                else{

                }
            }



        return root
    }

    private val scanLauncher =
        registerForActivityResult(ScanContract()){
            result: ScanIntentResult ->
            {
                if (result.contents == null){
                    Toast.makeText(requireContext(), "Camara permiso requerido", Toast.LENGTH_SHORT).show()

                }
                else{
                    setResult(result.contents)

                }

            }
        }


    private fun setResult(string: String){
        binding.textResult.text = string
    }


    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan QR code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)

    }
}




