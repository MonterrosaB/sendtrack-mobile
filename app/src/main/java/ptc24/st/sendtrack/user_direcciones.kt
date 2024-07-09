package ptc24.st.sendtrack

import Modelo.ClaseConexion
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ptc24.st.sendtrack.databinding.FragmentUserDireccionesBinding
import java.util.Locale
import java.util.UUID

class user_direcciones : Fragment(), OnMapReadyCallback {

private lateinit var map: GoogleMap
    private var _binding: FragmentUserDireccionesBinding? = null
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
        _binding = FragmentUserDireccionesBinding.inflate(inflater, container, false)
        val root = binding.root

        CreateFragment()

        //Elementos

        //accionadores
        val btnVerificar = root.findViewById<Button>(R.id.btnVerificarD)
        val btnAgregar = root.findViewById<Button>(R.id.btnAgregarD)



        btnVerificar.setOnClickListener {
            val calle = binding.txtEditCalle.text.toString()
            val distrito = binding.txtEditDistrito.text.toString()
            val municipio = binding.txtEditMunicipio.text.toString()
            addressGeocoder(calle,distrito, municipio)
        }

        btnAgregar.setOnClickListener{

            CoroutineScope(Dispatchers.IO).launch{
                try {

                    val objConexion = ClaseConexion().cadenaConexion()

                    val calle = binding.txtEditCalle.text.toString()
                    val distrito = binding.txtEditDistrito.text.toString()
                    val municipio = binding.txtEditMunicipio.text.toString()
                    val codigoPostal = binding.txtEditCodigoPostal.text.toString()
                    val instrucciones = binding.txtEditInstrucciones.text.toString()

                    val (latitud, longitud) =  addressGeocoder(calle, distrito, municipio)
                    if (latitud !=0.0 && longitud !=0.0){

                        val insertDireccion = objConexion?.prepareStatement("INSERT INTO Direccion" +
                                " (IdDireccion, IdCliente, IdDistrito, NombreCompleto,Dirección, Instruccione,Ubicacion)" +
                                " VALUES (?, '35CAEDC742FA4AF79C7548A3DD3942E3', 1, 'Rodrigo', ?, ?,SDO_GEOMETRY(2001, 4326, SDO_POINT_TYPE(?, ?, NULL), NULL, NULL))")!!

                        insertDireccion.setString(1, UUID.randomUUID().toString())
                        //insertDireccion.setString(2, n) idCliente
                        //insertDireccion.setString(2, distrito)
                        //insertDireccion.setString(3, n) nombre
                        insertDireccion.setString(2,calle )
                        insertDireccion.setString(3, instrucciones)
                        insertDireccion.setDouble(4, longitud)
                        insertDireccion.setDouble(5, latitud)
                        insertDireccion.executeUpdate()

                    }else{
                        println("No quiere")
                    }
                }catch (e: Exception){
                    println("El erros es: $e")
                }
            }
        }
        return root
    }

    fun addressGeocoder(street: String, city: String, state: String ):Pair<Double, Double> {

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val address = "$street,$city, $state"

            try {
                val addresses = geocoder.getFromLocationName(address, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val location = addresses[0]
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val cordenadas = LatLng(latitude,longitude)
                    val marker = MarkerOptions().position(cordenadas)
                    map.addMarker(marker)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(cordenadas, 18f),2000, null)

                    println("Latitude: $latitude, Longitude: $longitude")
                    return Pair(latitude,longitude)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return Pair(0.0, 0.0)
    }

    private fun CreateFragment(){
        //GlobalScope.launch(Dispatchers.Main) {  }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        setMapStyle()

        GlobalScope.launch(Dispatchers.Main) {

            val limitesSV = LatLngBounds(
                LatLng(13.0669, -90.1546),  // Suroeste
                LatLng(14.4465, -87.6932)
            )

            map.moveCamera(CameraUpdateFactory.newLatLngBounds(limitesSV, 0))

            map.setLatLngBoundsForCameraTarget(limitesSV)

            map.setOnCameraMoveListener {
                val position = map.cameraPosition.target
                if (!limitesSV.contains(position)) {
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(limitesSV, 0))
                }
            }

        }
        map.setOnMapClickListener {
        }
    }

    private fun setMapStyle() {
            val result: Boolean = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!result){
                Log.e("Map", "Error set map style")
            }
    }
}