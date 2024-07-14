package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtDistrito
import Modelo.dtMunicipio
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
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

        fun getMunicipio(): List<dtMunicipio>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()

            val resultSet = statement?.executeQuery("select * from Municipio")!!

            val listadoMunicipios = mutableListOf<dtMunicipio>()


            while (resultSet.next()){
                val idMunicipio = resultSet.getString("IdMunicipio")
                val municipio = resultSet.getString("NomMunicipio")

                println(municipio)

                val Municipios = dtMunicipio(idMunicipio, municipio)

                listadoMunicipios.add(Municipios)
            }
            return listadoMunicipios
        }

        fun getDistrito(): List<dtDistrito>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()

            val resultSet = statement?.executeQuery("SELECT * FROM Distrito")!!

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
                val distrito = getDistrito()[cbDistrito.selectedItemPosition].idDistrito
                val municipio = getMunicipio()[cbMunicipio.selectedItemPosition].idMunicipio

                println("hola $distrito")
               println("MM $municipio")

            //addressGeocoder(calle,distrito, municipio)
            }
        }

        btnAgregar.setOnClickListener{

            CoroutineScope(Dispatchers.IO).launch{
                try {

                    val objConexion = ClaseConexion().cadenaConexion()

                    val nombre = binding.txtEditNombre.text.toString()
                    val calle = binding.txtEditCalle.text.toString()
                    val distrito = getDistrito()[cbDistrito.selectedItemPosition].idDistrito
                    val municipio = getMunicipio()[cbMunicipio.selectedItemPosition].municipio
                    val instrucciones = binding.txtEditInstrucciones.text.toString()

                    println(distrito+ "HHH")
                    println(municipio+ "```11")
/*
                    val (latitud, longitud) =  addressGeocoder(calle, distrito, municipio)
                    if (latitud !=0.0 && longitud !=0.0){

                        val insertDireccion = objConexion?.prepareStatement("INSERT INTO Direccion" +
                                " (IdCliente, IdDistrito, NombreCompleto,Dirección, Instruccion,Ubicacion)" +
                                " VALUES ('1', , ?, ?, ?,SDO_GEOMETRY(2001, 4326, SDO_POINT_TYPE(?, ?, NULL), NULL, NULL))")!!

                        //insertDireccion.setString(2, n) idCliente
                        insertDireccion.setString(2, distrito)
                        insertDireccion.setString(3, nombre)
                        insertDireccion.setString(2,calle )
                        insertDireccion.setString(3, instrucciones)
                        insertDireccion.setDouble(4, longitud)
                        insertDireccion.setDouble(5, latitud)
                        insertDireccion.executeUpdate()

                    }else{
                        println("No quiere")
                    }*/
                }catch (e: Exception){
                    println("El erros es: $e")
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

            /*val ubiExacta = Intent(context, user_mapa_direccion::class.java)
            ubiExacta.putExtra("Cordenadas", cordenadas)
            println(cordenadas)
            context?.startActivity(ubiExacta)*/

        }
    }

    private fun setMapStyle() {
            val result: Boolean = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!result){
                Log.e("Map", "Error set map style")
            }
    }
}