package RVHMovimientos

import Modelo.dtMovimientos
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ptc24.st.sendtrack.R

class AdaptadorMovimientos(var Datos: List<dtMovimientos>): RecyclerView.Adapter<ViewHolderMovimientos>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMovimientos {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_movimientos, parent, false)
        return ViewHolderMovimientos(vista)
    }

    override fun getItemCount() = Datos.size

    fun actualizarDatos(nuevaLista: List<dtMovimientos>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderMovimientos, position: Int) {
        val itemMovimientos = Datos[position]
        holder.lblidCodigoCargamento.text = itemMovimientos.IdCargamento
        holder.lblHoraEntrada.text = itemMovimientos.horaEntrada
        holder.lbNumeroPaquetes.text = "N° Paquetes: " + itemMovimientos.numPaquetes
        holder.lbSeccion.text = itemMovimientos.seccion

    }
}