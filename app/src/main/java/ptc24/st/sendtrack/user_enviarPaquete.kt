package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtDireccion
import Modelo.dtMunicipio
import Modelo.dtSeguro
import RVHDireccion.AdaptadorDireccion
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
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
        val user = "0FAE51E657574734BDE792E40A0337A4"

        val rcvDireccion = root.findViewById<RecyclerView>(R.id.rcvDireccion)

        //Accionadores
        val btnAgregarP = root.findViewById<Button>(R.id.btnAgregarPaquete)

        rcvDireccion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        fun mostrarDirecciones(): List<dtDireccion>{

            val objConexion = ClaseConexion().cadenaConexion()

             val statement = objConexion?.prepareStatement("SELECT D.IdDireccion, D.IdCliente, D.NombreCompleto as Nombre, D.Direccion, D.Instruccion, DI.Distrito, M.NomMunicipio as Municipio\n" +
                     "FROM Direccion D " +
                     "INNER JOIN Distrito DI ON D.IdDistrito = DI.IdDistrito " +
                     "INNER JOIN Municipio M ON DI.IdMunicipio = M.IdMunicipio " +
                     "WHERE D.IdCliente = ?")!!

            statement.setString(1, user)

            val resultSet = statement.executeQuery()

            val direcciones = mutableListOf<dtDireccion>()

            while(resultSet.next()){
                    val idDireccion = resultSet.getString("IdDireccion")
                    val idClinte = resultSet.getString("IdCliente")
                    val direccionC = resultSet.getString("Direccion")
                    val instrucion = resultSet.getString("Instruccion")
                    val nombreCompleto = resultSet.getString("Nombre")
                    val distrito = resultSet.getString("Distrito")
                    val municipio = resultSet.getString("Municipio")

                    val direccion = dtDireccion(idDireccion, idClinte,nombreCompleto, direccionC, instrucion,distrito, municipio)
                    direcciones.add(direccion)
                }
            return direcciones
        }

        fun getSeguro(): List<dtSeguro>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()

            val resultSet = statement?.executeQuery("select * from Seguro")!!

            val listadoSeguro = mutableListOf<dtSeguro>()


            while (resultSet.next()){
                val idSeguro = resultSet.getString("IdSeguro")
                val tipoSeguro = resultSet.getString("ValorMercancia")
                val precio = resultSet.getDouble("Costo")

                println(idSeguro)

                val seguros = dtSeguro(idSeguro, tipoSeguro, precio)

                listadoSeguro.add(seguros)
            }
            return listadoSeguro
        }

        CoroutineScope(Dispatchers.IO).launch {
            val direccionesDB = mostrarDirecciones()
             val listaSeguro = getSeguro()
            val tipoSeguro = listaSeguro.map { it.tipoSeguro }

            withContext(Dispatchers.Main){
                val adapter = AdaptadorDireccion(direccionesDB)
                rcvDireccion.adapter = adapter

                val seguroAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, tipoSeguro)
                (binding.cbContenido as? AutoCompleteTextView)?.setAdapter(seguroAdapter)            }
        }

       btnAgregarP.setOnClickListener{
           CoroutineScope(Dispatchers.IO).launch {

               val objConexion = ClaseConexion().cadenaConexion()

               val addPaquete = objConexion?.prepareStatement("INSERT INTO Paquete(Peso, Alto, Largo, Ancho, IdDireccion, Origen, IdSeguro)" +
                       " VALUES(?, ?, ?, ?, ?, ?, ? )")!!


               addPaquete.setDouble(1, pesoP.text.toString().toDouble())
               addPaquete.setDouble(2, altoP.text.toString().toDouble())
               addPaquete.setDouble(3, largoP.text.toString().toDouble())
               addPaquete.setDouble(4, anchoP.text.toString().toDouble())
               //addPaquete.setDate(6, )
               //addPaquete.setString(7, idDireccion) RCV
               //addPaquete.setInt(8, dropdown) DROPDOWN
               //addPaquete.setString(9, idEmpaquetado) ??
               addPaquete.executeUpdate()

               val nuevoTicket = mostrarDirecciones()
               withContext(Dispatchers.Main){
                   (rcvDireccion.adapter as? AdaptadorDireccion)?.actualizarDirecciones(nuevoTicket)
               }
           }
       }

        /*val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.cbContenido as? AutoCompleteTextView)?.setAdapter(adapter)*/

        return root
    }

}