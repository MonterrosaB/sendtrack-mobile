package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtMisPaquetes
import Modelo.dtSeccion
import Modelo.dtSeguro
import RVHMovimientos.AdaptadorMovimientos
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ptc24.st.sendtrack.databinding.FragmentEmployeeSeccionesBinding
import ptc24.st.sendtrack.databinding.FragmentUserEnviarPaqueteBinding
import ptc24.st.sendtrack.databinding.FragmentUserMisPaquetesBinding


class employee_Secciones : BottomSheetDialogFragment() {

    private var _binding: FragmentEmployeeSeccionesBinding? = null
    private val binding get() = _binding!!
    private lateinit var idSeccion: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =  FragmentEmployeeSeccionesBinding.inflate(inflater, container, false)
        val root = binding.root

        fun getSeccion(): List<dtSeccion>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("select * from Seccion where IdSucursal = ? ")!!

            statement.setString(1, Login.sucursal)

            val resultSet = statement.executeQuery()

            val secciones = mutableListOf<dtSeccion>()

            while(resultSet.next()){
                val idSeccion = resultSet.getString("IdSeccion")
                val nombreSeccion = resultSet.getString("Nombre")

                val seccion = dtSeccion(idSeccion, nombreSeccion)
                secciones.add(seccion)
            }
            return secciones
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaSeccion = getSeccion()
            val seccion = listaSeccion.map { it.seccion}
            withContext(Dispatchers.Main){
                val seguroAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, seccion)
                (binding.cbSeccion as? AutoCompleteTextView)?.setAdapter(seguroAdapter)
            }
        }

        binding.btnIngresarCargamentoS.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion  = ClaseConexion().cadenaConexion()

                //crear variable que contenga un PrepareStatment
                val addCargamentoAlmacen = objConexion?.prepareStatement("INSERT INTO Almacen (IdPaquete, IdSeccion, IdUsuario) " +
                        "SELECT IdPaquete, ?, ? FROM RegistroCargamento WHERE IdCargamento = ?")!!
                addCargamentoAlmacen.setString(1, idSeccion)
                addCargamentoAlmacen.setString(2,Login.idUser)
                addCargamentoAlmacen.setString(3, fragmentMovimientos.idCargamentoInhgresado)
                addCargamentoAlmacen.executeUpdate()
            }

        }


        binding.cbSeccion.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            CoroutineScope(Dispatchers.IO).launch{
                val listaSeguro = getSeccion()

                // Aquí obtienes la posición del elemento seleccionado
                val selectedSeguro = listaSeguro[position]
                idSeccion = selectedSeguro.idSeccion
                val selectedItem = parent.getItemAtPosition(position) as String
                // Haz algo con el elemento seleccionado y su posición
                println("Elemento seleccionado: $selectedItem en la posición: $idSeccion")
            }
        }


        return  root
    }
}