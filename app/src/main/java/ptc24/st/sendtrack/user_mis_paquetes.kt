package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtDireccion
import Modelo.dtMisPaquetes
import RVHDireccion.AdaptadorDireccion
import RVHMisPaquetes.AdaptadorMisPaquetes
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ptc24.st.sendtrack.Login.variablesGlobalesLogin.correoIngresado
import ptc24.st.sendtrack.databinding.FragmentUserDireccionesBinding
import ptc24.st.sendtrack.databinding.FragmentUserMisPaquetesBinding

class user_mis_paquetes : Fragment() {

    private var _binding: FragmentUserMisPaquetesBinding? = null
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
        _binding =  FragmentUserMisPaquetesBinding.inflate(inflater, container, false)
        val root = binding.root

        //Elementos
        val btnMostrarPaquete = root.findViewById<Button>(R.id.btnMostrarMapa)
        val rcvMispaquetes = root.findViewById<RecyclerView>(R.id.rcvMisPaquetes)

        rcvMispaquetes.layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)

        fun mostrarMisPaquetes(): List<dtMisPaquetes>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("select * from Paquete P " +
                    "INNER JOIN Direccion D ON D.IdDireccion = P.IdDireccion " +
                    "where idCliente = ?")!!

            statement.setString(1, Login.idUser)

            val resultSet = statement.executeQuery()

            val paquetes = mutableListOf<dtMisPaquetes>()

            while(resultSet.next()){
                val idPaquete = resultSet.getString("IdPaquete")
                val fecha = resultSet.getString("FechaInicio")

                val paquete = dtMisPaquetes(idPaquete, fecha)
                paquetes.add(paquete)
            }
            return paquetes
        }

        CoroutineScope(Dispatchers.IO).launch {
            val misPaquetesDB = mostrarMisPaquetes()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorMisPaquetes(misPaquetesDB)
                rcvMispaquetes.adapter = adapter
            }
        }

        btnMostrarPaquete.setOnClickListener {
            mostrarPaquete(binding.txtEditCodigoP.text.toString())
        }

        return root

    }

    private fun mostrarPaquete(idPaquete:String) {
        try {
            CoroutineScope(Dispatchers.IO).launch{
                val objConexion = ClaseConexion().cadenaConexion()
                val paquete =
                    objConexion?.prepareStatement("Select Paquete.IdPaquete from Paquete " +
                            "INNER JOIN Direccion ON Paquete.IdDireccion = Direccion.IdDireccion " +
                            "INNER JOIN CLIENTE ON Direccion.IdCliente = Cliente.IdCliente " +
                            " where Paquete.IdPaquete = ? ")!!
                paquete.setString(1, idPaquete)
                //paquete.setString(2, Login.variablesGlobalesLogin.idUser)
                val result = paquete.executeQuery()

                if (result.next() == true) {
                    val miPaquete = Intent(activity, rastrearPaquetes::class.java)
                    startActivity(miPaquete)
                }
                else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Paquete no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch (e: Exception){
            println(e)
        }
    }
}