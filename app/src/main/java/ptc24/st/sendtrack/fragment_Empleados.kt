package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtEmpleados
import RVHEmpleados.AdaptadorEmpleados
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class fragment_Empleados : Fragment() {


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
        val root = inflater.inflate(R.layout.fragment__empleados, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rcvEmpleados)
        recyclerView.layoutManager = LinearLayoutManager(context)

        ///// TODO: Mostrar datos
        fun obtenerEmpleados(): List<dtEmpleados>{

            val objConexion = ClaseConexion().cadenaConexion()

            val cargar = objConexion?.prepareStatement("Select E.DUI, E.Nombre, E.apellidopaterno, E.apellidomaterno, E.email, " +
                    "E.telefono , E.salario, E.fechana, Rol.NomRol,Sucursal.Nombre as Sucursal, E.masculino, " +
                    "E.estado, rol.idrol, sucursal.idsucursal " +
                    "from empleado E " +
                    "inner join Rol on E.IdRol = Rol.idRol " +
                    "inner join Sucursal on E.idSucursal = sucursal.idsucursal " +
                    "where Sucursal.idsucursal = ? ")!!
            cargar.setInt(1, Login.variablesGlobalesLogin.sucursal.toInt())
            val resultSet = cargar.executeQuery()

            val listaEmpleados = mutableListOf<dtEmpleados>()

            while (resultSet.next())
            {
                val dui = resultSet.getString("DUI")
                val nombre = resultSet.getString("Nombre")
                val apellidoPa = resultSet.getString("ApellidoPaterno")
                val apellidoMa = resultSet.getString("ApellidoMaterno")
                val Email = resultSet.getString("Email")
                val Telefono = resultSet.getString("Telefono")
                val Salario = resultSet.getDouble("Salario")
                val FechaNa = resultSet.getString("FechaNa")
                val Rol = resultSet.getString("NomRol")
                val idRol = resultSet.getInt("IdRol")
                val Sucursal = resultSet.getString("Sucursal")
                val idSucursal = resultSet.getInt("IdSucursal")
                val Masculino = resultSet.getInt("Masculino")
                val Estado = resultSet.getInt("Estado")


                val valoresJuntos = dtEmpleados(dui, nombre, apellidoPa, apellidoMa, Email, Salario, FechaNa, Rol, idRol,
                    Sucursal, idSucursal, Masculino, Estado, Telefono)

                listaEmpleados.add(valoresJuntos)
            }
            return listaEmpleados
        }

        CoroutineScope(Dispatchers.IO).launch {
            val EmpleadosDB = obtenerEmpleados()
            withContext(Dispatchers.Main)
            {
                val adapter = AdaptadorEmpleados(EmpleadosDB)
                recyclerView.adapter = adapter
            }
        }

        //recyclerView.layoutManager = LinearLayoutManager(context)
        //recyclerView.adapter = adapter


        return root
    }
}