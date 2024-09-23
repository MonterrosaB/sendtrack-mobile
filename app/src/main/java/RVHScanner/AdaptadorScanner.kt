package RVHScanner

import Modelo.dtPaqueteEntregar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class AdaptadorScanner(var Datos: List<dtPaqueteEntregar>):RecyclerView.Adapter<ViewHolderScanner>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderScanner {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_paquetes_scanner, parent, false)
        return ViewHolderScanner(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderScanner, position: Int) {
        val paqueteEntregar = Datos[position]
        holder.lbDireccion.text = paqueteEntregar.direccion
        holder.lbPaquete.text = paqueteEntregar.idPaquete
        holder.lbIndicacion.text = paqueteEntregar.instruccion

    }
}