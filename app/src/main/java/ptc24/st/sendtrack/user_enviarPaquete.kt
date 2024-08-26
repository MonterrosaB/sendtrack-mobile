package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtDireccion
import Modelo.dtDistrito
import Modelo.dtMunicipio
import Modelo.dtSeguro
import RVHDireccion.AdaptadorDireccion
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
    private lateinit var idSeguro: String
    private lateinit var idDistrito: String

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
        val txtAlto = root.findViewById<EditText>(R.id.txtEditAlto)
        val txtAncho = root.findViewById<EditText>(R.id.txtEditAncho)
        val txtLargo = root.findViewById<EditText>(R.id.txtEditLargo)
        val txtPeso = root.findViewById<EditText>(R.id.txtEditPeso)
        val spnSeguro = binding.cbSeguro
        val spnOrigen = binding.cbOrigen
        var contenido: Int

        val rcvDireccion = root.findViewById<RecyclerView>(R.id.rcvDireccion)

        //Accionadores
        val btnAgregarP = root.findViewById<Button>(R.id.btnAgregarPaquete)

        rcvDireccion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        fun mostrarDirecciones(): List<dtDireccion>{

            val objConexion = ClaseConexion().cadenaConexion()

             val statement = objConexion?.prepareStatement("SELECT D.IdDireccion, D.IdCliente, D.NombreCompleto as Nombre, D.Direccion, D.Instruccion, DI.Distrito, M.NomMunicipio as Municipio " +
                     "FROM Direccion D " +
                     "INNER JOIN Distrito DI ON D.IdDistrito = DI.IdDistrito " +
                     "INNER JOIN Municipio M ON DI.IdMunicipio = M.IdMunicipio " +
                     "WHERE D.IdCliente = ?")!!

            statement.setString(1, Login.variablesGlobalesLogin.idUser)

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

                val seguros = dtSeguro(idSeguro, tipoSeguro, precio)

                listadoSeguro.add(seguros)
            }
            return listadoSeguro
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


        CoroutineScope(Dispatchers.IO).launch {
            val direccionesDB = mostrarDirecciones()

             val listaSeguro = getSeguro()
            val tipoSeguro = listaSeguro.map { "Pecio:" + it.precio+ "$" + " Valor asegurado: " + it.tipoSeguro + "$"}

            val listaDistrito = getDistrito()
            val distrito = listaDistrito.map { it.distrito }

            withContext(Dispatchers.Main){
                val adapter = AdaptadorDireccion(direccionesDB)
                rcvDireccion.adapter = adapter

                val seguroAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, tipoSeguro)
                (binding.cbSeguro as? AutoCompleteTextView)?.setAdapter(seguroAdapter)

                val distritoAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, distrito)
                (binding.cbOrigen as? AutoCompleteTextView)?.setAdapter(distritoAdapter)
            }
        }

        spnOrigen.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            CoroutineScope(Dispatchers.IO).launch{
                val listaDistrito = getDistrito()

                // Aquí obtienes la posición del elemento seleccionado
                val selectedDistrito = listaDistrito[position]
                idDistrito = selectedDistrito.idDistrito
                val selectedItem = parent.getItemAtPosition(position) as String
                // Haz algo con el elemento seleccionado y su posición
                println("Elemento seleccionado: $selectedItem en la posición: $idDistrito")
            }
        }

        spnSeguro.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            CoroutineScope(Dispatchers.IO).launch{
                val listaSeguro = getSeguro()

                // Aquí obtienes la posición del elemento seleccionado
                val selectedSeguro = listaSeguro[position]
                idSeguro = selectedSeguro.idSeguro
                val selectedItem = parent.getItemAtPosition(position) as String
                // Haz algo con el elemento seleccionado y su posición
                println("Elemento seleccionado: $selectedItem en la posición: $idSeguro")
            }
        }

       btnAgregarP.setOnClickListener{
           try {

               val pesoP = binding.txtEditPeso.text.toString()
               val altoP = binding.txtEditAlto.text.toString()
               val largoP = binding.txtEditLargo.text.toString()
               val anchoP =binding.txtEditAncho.text.toString()

               var hayErrores = false

               if (pesoP.isEmpty()){
                   txtPeso.error = "Campo Obligatorio"
                   hayErrores = true
               }else {
                   txtPeso.error = null
               }

               if (altoP.isEmpty()){
                   txtAlto.error = "Campo Obligatorio"
                   hayErrores = true
               }else {
                   txtAlto.error = null
               }

               if (largoP.isEmpty()){
                   txtLargo.error = "Campo Obligatorio"
                   hayErrores = true
               }else {
                   txtLargo.error = null
               }

               if (anchoP.isEmpty()){
                   txtAncho.error = "Campo Obligatorio"
                   hayErrores = true
               }else {
                   txtAncho.error = null
               }

               if (pesoP.toDouble()>=150){
                   txtPeso.error = "El peso maximo es 150lb"
                   hayErrores = true
               }else {
                   txtPeso.error = null
               }

               if (altoP.toDouble()>=120){
                   txtAlto.error = "La alturan maxima es de 120cm"
                   hayErrores = true
               }else {
                   txtAlto.error = null
               }

               if (largoP.toDouble()>=80){
                   txtLargo.error = "El largo maximo es de 80cm"
                   hayErrores = true
               }else {
                   txtLargo.error = null
               }

               if (anchoP.toDouble()>=80){
                   txtAncho.error = "El ancho maximo es de 80cm"
                   hayErrores = true
               }else {
                   txtAncho.error = null
               }



                   if (hayErrores == false && AdaptadorDireccion.variableDireccion.idDireccion.isNotEmpty()){
                       CoroutineScope(Dispatchers.IO).launch {
                           val objConexion = ClaseConexion().cadenaConexion()


                           val addPaquete = objConexion?.prepareStatement("INSERT INTO Paquete(Peso, Alto, Largo, Ancho, IdDireccion, Origen, IdSeguro)" +
                                   " VALUES(?, ?, ?, ?, ?, ?, ? )")!!

                           addPaquete.setDouble(1, pesoP.toDouble())
                           addPaquete.setDouble(2, altoP.toDouble())
                           addPaquete.setDouble(3, largoP.toDouble())
                           addPaquete.setDouble(4, anchoP.toDouble())
                           addPaquete.setString(5, AdaptadorDireccion.variableDireccion.idDireccion)
                           addPaquete.setString(6, idDistrito)
                           addPaquete.setString(7, idSeguro)

                           addPaquete.executeUpdate()


                           val nuevaDireccion = mostrarDirecciones()
                           withContext(Dispatchers.Main){
                               binding.txtEditPeso.text = null
                               binding.txtEditAlto.text = null
                               binding.txtEditAncho.text = null
                               binding.txtEditLargo.text = null
                               (rcvDireccion.adapter as? AdaptadorDireccion)?.actualizarDirecciones(nuevaDireccion)
                       }
                       }
                   }
                   else{
                       Toast.makeText(requireContext(), "Verifica los campos", Toast.LENGTH_SHORT).show()
                   }


           }catch (e: Exception){
               println(e)
           }
       }
        return root
    }

}