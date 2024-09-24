package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtPaqRepartidor
import Modelo.dtPaqueteEntregar
import RVHEmpleados.AdaptadorEmpleados
import RVHScanner.AdaptadorScanner
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ptc24.st.sendtrack.paquetesRepartidor.variableGlobalRepartidor.ruta

class employee_scanner : Fragment() {

    val codigo_opcion_tomar_foto = 103
    val CAMERA_REQUEST_CODE = 0

    private lateinit var previewView: PreviewView


    private lateinit var btnEntrgar: Button

    var codigos: List<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    fun mostrarPaquetes(): List<dtPaqRepartidor>{

        val objConexion = ClaseConexion().cadenaConexion()

        val statement = objConexion?.prepareStatement("Select P.IdPaquete, Peso, Alto, Ancho, Largo, Distrito " +
                "from Paquete P " +
                "INNER JOIN Distrito D ON D.IdDistrito = P.Origen " +
                "where IdPaquete in " +
                "(select IdPaquete from RegistroContenedor RC " +
                "INNER JOIN Contenedor C ON C.IdContenedor = RC.IdContenedor " +
                "WHERE C.IdRuta = ?) ")!!

        statement.setString(1, ruta)

        val resultSet = statement.executeQuery()

        val dtPaqRepartidor = mutableListOf<dtPaqRepartidor>()


        while (resultSet.next()){
            val Origen = resultSet.getString("Distrito")
            val idPaquete = resultSet.getString("idPaquete")
            val peso = resultSet.getString("peso")
            val altura = resultSet.getString("Alto")
            val ancho = resultSet.getString("ancho")
            val largo = resultSet.getString("largo")
            val paquete = dtPaqRepartidor(Origen ,idPaquete, peso, altura, ancho, largo)

            dtPaqRepartidor.add(paquete)
        }
        return dtPaqRepartidor
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_employee_scanner, container, false)

        //1-Llamo a los elementos
        previewView = root.findViewById(R.id.previewView)
        val rcvPaqueteEntregar = root.findViewById<RecyclerView>(R.id.rcvPaquetesScanner)
        btnEntrgar = root.findViewById(R.id.btnEntregar)

        btnEntrgar.setOnClickListener {
            checkStoragePermission()
        }


        codigos = listOf("1002")


        fun mostrarPaquete(): List<dtPaqueteEntregar>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("Select P.IdPaquete, D.Direccion, D.Instruccion " +
                    "from Paquete P " +
                    "INNER JOIN Direccion D ON D.IdDireccion = P.IdDireccion " +
                    "where IdPaquete in " +
                    "(select IdPaquete from RegistroContenedor RC " +
                    "INNER JOIN Contenedor C ON C.IdContenedor = RC.IdContenedor " +
                    "WHERE C.IdRuta = ?)")!!

            statement.setString(1, ruta)

            val resultSet = statement.executeQuery()

            val dtPaqueteEntregar = mutableListOf<dtPaqueteEntregar>()


            while (resultSet.next()){
                val idPaquete = resultSet.getString("IdPaquete")
                val direccion = resultSet.getString("Direccion")
                val instruccion = resultSet.getString("Instruccion")
                val paquete = dtPaqueteEntregar(idPaquete ,direccion, instruccion)

                dtPaqueteEntregar.add(paquete)
            }
            return dtPaqueteEntregar
        }

        CoroutineScope(Dispatchers.IO).launch {
            val PaquetesDB = mostrarPaquete()
            withContext(Dispatchers.Main)
            {
                val adapter = AdaptadorScanner(PaquetesDB)
                rcvPaqueteEntregar.adapter = adapter
            }
        }

        startCamera()

        return root
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
        }    }

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
            else -> {
                // Este else lo dejamos por si sale un permiso que no teníamos controlado.
            }
        }
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
                            codigos?.let { processImageProxy(imageProxy, it) }
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
    private fun processImageProxy(imageProxy: ImageProxy, validQRCodes: List<String>) {
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

                            // Verificar si el código escaneado está en la lista de códigos válidos
                            if (rawValue != null && validQRCodes.contains(rawValue)) {
                                btnEntrgar.setEnabled(true);
                            //Asigna valor true.
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "Código QR no válido: $rawValue",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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


}




