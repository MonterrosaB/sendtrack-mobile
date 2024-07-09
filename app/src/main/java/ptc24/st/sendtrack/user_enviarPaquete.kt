package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtDireccion
import RVHDireccion.AdaptadorDireccion
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ptc24.st.sendtrack.databinding.FragmentUserDireccionesBinding
import ptc24.st.sendtrack.databinding.FragmentUserEnviarPaqueteBinding
import java.sql.Date
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
class user_enviarPaquete : Fragment() {

private var _binding: FragmentUserEnviarPaqueteBinding? = null
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
        _binding = FragmentUserEnviarPaqueteBinding.inflate(inflater, container, false)
        val root = binding.root

        //Elementos
        val pesoP = binding.txtEditPeso
        val altoP = binding.txtEditAlto
        val largoP = binding.txtEditLargo
        val anchoP =binding.txtEditAncho
        var contenido: Int
        val user = "35CAEDC742FA4AF79C7548A3DD3942E3"
        val rcvDireccion = root.findViewById<RecyclerView>(R.id.rcvDireccion)

        //Accionadores
        val btnAgregarP = root.findViewById<Button>(R.id.btnAgregarPaquete)

        rcvDireccion.layoutManager = LinearLayoutManager(requireContext())

        fun mostrarDirecciones(): List<dtDireccion>{

            val objConexion = ClaseConexion().cadenaConexion()

             val statement = objConexion?.prepareStatement("SELECT * FROM Direccion WHERE IdCliente = ?")!!

            statement.setString(1, user)

            val resultSet = statement.executeQuery()

            val direcciones = mutableListOf<dtDireccion>()

            while(resultSet.next()){
                    val idDireccion = resultSet.getString("IdDireccion")
                    val idClinte = resultSet.getString("IdCliente")
                    val direccionC = resultSet.getString("Direccion")
                    val nombreCompleto = resultSet.getString("NombreC")
                    val direccion = dtDireccion(idDireccion, idClinte,nombreCompleto, direccionC)
                    direcciones.add(direccion)
                }
            return direcciones
        }

        CoroutineScope(Dispatchers.IO).launch {
            val direccionesDB = mostrarDirecciones()
            withContext(Dispatchers .Main){
                val adapter = AdaptadorDireccion(direccionesDB)
                rcvDireccion.adapter = adapter
            }
        }

        btnAgregarP.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {

                val objConexion = ClaseConexion().cadenaConexion()

                val addPaquete = objConexion?.prepareStatement("INSERT INTO Paquete(IdCliente, Peso, Alto, Largo, Ancho, FechaInicio, IdDireccion, Origen, IdEmpaquetado)" +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ? )")!!

                //addPaquete.setString(1, idCliente)  LOGIN
                addPaquete.setDouble(2, pesoP.text.toString().toDouble())
                addPaquete.setDouble(3, altoP.text.toString().toDouble())
                addPaquete.setDouble(4, largoP.text.toString().toDouble())
                addPaquete.setDouble(5, anchoP.text.toString().toDouble())
                //arreglar defaul date en db
                //addPaquete.setDate(6, )
                //addPaquete.setString(7, idDireccion) RCV
                //addPaquete.setInt(8, dropdown) DROPDOWN
                //addPaquete.setString(9, idEmpaquetado) ??
                addPaquete.executeUpdate()

                /*val nuevoTicket = mostrarDirecciones()
                withContext(Dispatchers.Main){
                    (rcvDireccion.adapter as? AdaptadorDireccion)?.actualizarDirecciones(nuevoTicket)
                }*/
            }
        }

        val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.cbContenido as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.swiSeguro.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {contenido = 1
                println(contenido)
            }else{
                contenido = 0
                println(contenido)
            }

        }
        return root
    }

}