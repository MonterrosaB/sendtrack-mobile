package RVHPaquetes

import Modelo.dtPaquetes
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class adaptadorPaquetes(var Datos: List<dtPaquetes>): RecyclerView.Adapter<ViewHolderPaquetes>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPaquetes {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_paquetes, parent, false)
        return ViewHolderPaquetes(vista)
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolderPaquetes, position: Int) {
        val itemPaquetes = Datos[position]
        holder.lblIdPaquete.text = itemPaquetes.idPaquete
        holder.lblPesoPaquete.text = itemPaquetes.peso
        holder.lblAlturaPaquete.text = itemPaquetes.altura
        holder.lblAnchoPaquete.text = itemPaquetes.ancho
        holder.lblLargoPaquete.text = itemPaquetes.largo

    }
}