package ptc24.st.sendtrack

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Button
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class employee_scanner : Fragment() {

    val codigo_opcion_galeria = 102
    val codigo_opcion_tomar_foto = 103
    val CAMERA_REQUEST_CODE = 0

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var previewView: PreviewView


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
        val root =  inflater.inflate(R.layout.fragment_employee_scanner, container, false)


        //1-Llamo al boton y a la vista previa
        val btnEscanear = root.findViewById<Button>(R.id.btnScanear)
        previewView = root.findViewById(R.id.previewView)

        btnEscanear.setOnClickListener {

                startCamera()

        }


        return root
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Get the camera provider
            val cameraProvider = cameraProviderFuture.get()

            // Preview Use Case
            val preview = Preview.Builder()
                .build()
                .also { previewUseCase ->
                    previewUseCase.setSurfaceProvider(previewView.surfaceProvider)
                }

            // ImageAnalysis Use Case
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(640, 480))  // Lower resolution to increase compatibility
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysisUseCase ->
                    analysisUseCase.setAnalyzer(
                        ContextCompat.getMainExecutor(requireContext()),
                        { imageProxy ->
                            processImageProxy(imageProxy)
                        }
                    )
                }

            try {
                // Unbind use cases before binding new ones
                cameraProvider.unbindAll()

                // Bind the selected use cases to the camera
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )

            } catch (exc: Exception) {
                Log.e("CameraXApp", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    //4- Metodo para leer lo del código QR
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner: BarcodeScanner = BarcodeScanning.getClient(options)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (isAdded && activity != null) {
                        for (barcode in barcodes) {
                            val rawValue = barcode.rawValue
                            println("El código QR escaneado es: $rawValue")
                            Toast.makeText(
                                requireActivity(),  // Or use `activity!!` if you are certain the activity is not null
                                "El código QR escaneado es: $rawValue",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Manejar el código QR escaneado
                            // Puede actualizar la UI o iniciar alguna acción aquí
                        }
                    }
                }
                .addOnFailureListener {
                    println("Error al escanear el código QR: $it")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
    //5- Comprobar que todos los permisos estén aceptados
    /*private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    //Esta función creo que no afecta en nada (es del repositorio anterior de subir imagenes)
    private fun pedirPermisoCamara() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireContext(),
                android.Manifest.permission.CAMERA
            )
        ) {
            //El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
            )
        }
    }*/


}




