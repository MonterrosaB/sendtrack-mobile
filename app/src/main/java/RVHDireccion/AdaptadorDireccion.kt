package RVHDireccion

import Modelo.ClaseConexion
import Modelo.dtDireccion
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ptc24.st.sendtrack.R

class AdaptadorDireccion(var Datos: List<dtDireccion>):RecyclerView.Adapter<ViewHolderDireccion>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDireccion {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_direccion, parent, false)
        return ViewHolderDireccion(vista)
    }

    companion object variableDireccion{
        lateinit var  idDireccion: String
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderDireccion, position: Int) {
        val item = Datos[position]
        holder.lbNombreCompleto.text = item.nombre
        holder.lbDireccion.text = item.direccion + ", "+item.distrito + ", " + item.municipio

        holder.cardDireccion.setOnClickListener {

            idDireccion = item.idDireccion
            println(idDireccion)
        }

        holder.lbEliminar.setOnClickListener{

            val context = holder.itemView.context

            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle("¿Estás seguro que deseas eliminar esta dirección?")
            builder.setNegativeButton("Cancelar"){
                    dialog, wich ->
                dialog.dismiss()
            }
            builder.setPositiveButton("Eliminar"){
                    dialog, wich->
                eliminarDatos(item.idDireccion, position)
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun eliminarDatos(idDireccion:String, position: Int) {
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(position)

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()

            val dDireccion = objConexion?.prepareStatement("Delete from Direccion where IdDireccion = ?")!!

            dDireccion.setString(1, idDireccion)
            dDireccion.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun actualizarDirecciones(nuevaLista:List<dtDireccion>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }
}