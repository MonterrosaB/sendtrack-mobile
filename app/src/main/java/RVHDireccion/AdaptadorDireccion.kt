package RVHDireccion

import Modelo.dtDireccion
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class AdaptadorDireccion(var Datos: List<dtDireccion>):RecyclerView.Adapter<ViewHolderDireccion>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDireccion {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_direccion, parent, false)
        return ViewHolderDireccion(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderDireccion, position: Int) {
        val item = Datos[position]
        holder.lbNombreCompleto.text = item.nombre
        holder.lbDireccion.text = item.direccion
        holder.itemView.setOnClickListener{

           // holder.cardDireccion.strokeColor(res.color(R.color.yellow))


        }

    }

    fun actualizarDirecciones(nuevaLista:List<dtDireccion>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }
}