package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtDistrito
import Modelo.dtMunicipio
import android.content.ContentValues.TAG
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Space
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ptc24.st.sendtrack.databinding.FragmentUserDireccionesBinding
import java.util.Locale
import java.util.UUID

class user_direcciones : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

private lateinit var map: GoogleMap
    private var _binding: FragmentUserDireccionesBinding? = null
    private val binding get() = _binding!!

    private  var latitud: Double = 0.0
    private  var longitud: Double = 0.0

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

        //Elementos
        val txtNombre = root.findViewById<EditText>(R.id.txtEditNombre)
        val txtCalle = root.findViewById<EditText>(R.id.txtEditCalle)
        val txtInstruciones = root.findViewById<EditText>(R.id.txtEditInstrucciones)

        CreateFragment()

        fun getMunicipio(): List<dtMunicipio>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()

            val resultSet = statement?.executeQuery("SELECT * FROM Municipio")!!

            val listaMunicipios = mutableListOf<dtMunicipio>()

            while (resultSet.next()){
                val idMunicipio = resultSet.getString("IdMunicipio")
                val municipio = resultSet.getString("NomMunicipio")

                val municipios = dtMunicipio(idMunicipio, municipio)

                listaMunicipios.add(municipios)
            }
            return listaMunicipios
        }


        fun getDistrito(): List<dtDistrito>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()

            val resultSet = statement?.executeQuery("SELECT * FROM Distrito order by Distrito asc")!!

            val listadoDistritos = mutableListOf<dtDistrito>()

            while (resultSet.next()){
                val idDistrito = resultSet.getString("IdDistrito")
                val distrito = resultSet.getString("Distrito")

                val distritos = dtDistrito(idDistrito, distrito)

                listadoDistritos.add(distritos)
            }
            return listadoDistritos
        }

        //Elementos
        val cbMunicipio = root.findViewById<Spinner>(R.id.spnMunicipio)
        val cbDistrito = root.findViewById<Spinner>(R.id.spnDistrito)

        //accionadores
        val btnVerificar = root.findViewById<Button>(R.id.btnVerificarD)
        val btnAgregar = root.findViewById<Button>(R.id.btnAgregarD)



        btnVerificar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val calle = binding.txtEditCalle.text.toString()
                val municipio = getMunicipio()[cbMunicipio.selectedItemPosition].municipio
                val distrito = getDistrito()[cbDistrito.selectedItemPosition].distrito

                withContext(Dispatchers.Main){
                    addressGeocoder(calle,distrito, municipio)
                }
            }
        }

        btnAgregar.setOnClickListener{

            CoroutineScope(Dispatchers.IO).launch{
                try {

                    var hayErrores = false

                    val nombre = binding.txtEditNombre.text.toString()
                    val calle = binding.txtEditCalle.text.toString()
                    val distrito = getDistrito()[cbDistrito.selectedItemPosition].idDistrito
                    val instrucciones = binding.txtEditInstrucciones.text.toString()

                    withContext(Dispatchers.Main){
                        if(nombre.isEmpty()){
                            txtNombre.error = "Campo obligatorio"
                            hayErrores = true
                        } else{
                            txtNombre.error = null
                        }

                        if(calle.isEmpty()){
                            txtCalle.error = "Campo obligatorio"
                            hayErrores = true
                        } else{
                            txtCalle.error = null
                        }

                        if(instrucciones.isEmpty()){
                            txtInstruciones.error = "Campo obligatorio"
                            hayErrores = true
                        } else{
                            txtInstruciones.error = null
                        }

                        if(nombre.length>50){
                            txtNombre.error = "Máximo de 60 caracteres"
                            hayErrores = true
                        } else{
                            txtNombre.error = null
                        }

                        if(calle.length>60){
                            txtCalle.error = "Máximo de 60 caracteres"
                            hayErrores = true
                        } else{
                            txtCalle.error = null
                        }

                        if(instrucciones.length>30){
                            txtInstruciones.error = "Máximo de 30 caracteres"
                            hayErrores = true
                        } else{
                            txtInstruciones.error = null
                        }
                    }

                    if (!hayErrores && latitud !=0.0 && longitud !=0.0){

                        val objConexion = ClaseConexion().cadenaConexion()

                        val insertDireccion = objConexion?.prepareStatement("INSERT INTO Direccion" +
                                " (IdCliente, IdDistrito, NombreCompleto,Direccion, Instruccion,Ubicacion)" +
                                " VALUES (?, ?, ?, ?, ?, SDO_GEOMETRY(2001, 4326, SDO_POINT_TYPE(?, ?, NULL), NULL, NULL))")!!

                        insertDireccion.setString(1, Login.idUser)
                        insertDireccion.setString(2, distrito)
                        insertDireccion.setString(3, nombre)
                        insertDireccion.setString(4,calle )
                        insertDireccion.setString(5, instrucciones)
                        insertDireccion.setDouble(6, longitud)
                        insertDireccion.setDouble(7, latitud)

                        insertDireccion.executeUpdate()


                        withContext(Dispatchers.Main){
                            binding.txtEditNombre.text = null
                            binding.txtEditCalle.text = null
                            binding.txtEditInstrucciones.text = null
                        }
                    }
                }catch (e: Exception){
                    println("Error: $e")
                }
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            val listaMunicipio = getMunicipio()
            val listaDistrito = getDistrito()

            val municipio = listaMunicipio.map { it.municipio }
            val distrito = listaDistrito.map { it.distrito }

            withContext(Dispatchers.Main){
                val municipioAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, municipio)
                cbMunicipio.adapter = municipioAdapter

                val distritoAdaptador =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, distrito)
                cbDistrito.adapter = distritoAdaptador
            }
        }
        return root
    }

    fun addressGeocoder(street: String, city: String, state: String ) {

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val address = "$street,$city, $state"

            try {
                val addresses = geocoder.getFromLocationName(address, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val location = addresses[0]
                     latitud = location.latitude
                     longitud = location.longitude

                    val cordenadas = LatLng(latitud,longitud)
                    val marker = MarkerOptions().position(cordenadas).draggable(true)
                    map.addMarker(marker)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(cordenadas, 18f),2000, null)

                    println("Latitude: $latitud, Longitude: $longitud")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    private fun CreateFragment(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapND) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        setMapStyle()

        map.setOnMarkerDragListener(this)


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
    }

    private fun setMapStyle() {
            val result: Boolean = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!result){
                Log.e("Map", "Error set map style")
            }
    }

    override fun onMarkerDrag(googleMap: Marker) {
        Log.d(TAG, "onMarkerDrag: ")
    }

    override fun onMarkerDragEnd(googleMap: Marker) {
        latitud = googleMap.position.latitude
        longitud = googleMap.position.longitude

        Log.d("MarkerDrag", "Nueva posición: Lat = $latitud, Lng = $longitud")

    }

    override fun onMarkerDragStart(googleMap: Marker) {
        Log.d(TAG, "onMarkerDrag: ")
    }
}