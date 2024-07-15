package RVHPaquetesRepartidor

import Modelo.dtPaqRepartidor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class AdaptadorPaqRepartidor(var Datos: List<dtPaqRepartidor>): RecyclerView.Adapter<ViewHolderPaqRepartidor>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPaqRepartidor {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_paquete_repartidor, parent, false)
        return ViewHolderPaqRepartidor(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderPaqRepartidor, position: Int) {
        val itemPaqRepartidor = Datos[position]
        holder.lblDireccionOrigenRepartidor.text = itemPaqRepartidor.Origen
        holder.lblIdPaqRepartidor.text = itemPaqRepartidor.idPaquete
        holder.lblPesoPaqRepartidor.text = itemPaqRepartidor.peso
        holder.lblAlturaPaqRepartidor.text = itemPaqRepartidor.altura
        holder.lblAnchoPaqRepartidor.text = itemPaqRepartidor.ancho
        holder.lblLargoPaqRepartidor.text = itemPaqRepartidor.largo
    }
}