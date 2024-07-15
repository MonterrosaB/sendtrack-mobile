package RVHPaquetesRepartidor

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class ViewHolderPaqRepartidor(view: View): RecyclerView.ViewHolder(view) {
    val lblIdPaqRepartidor = view.findViewById<TextView>(R.id.idPaqueteRepartidor)
    val lblDireccionOrigenRepartidor = view.findViewById<TextView>(R.id.lblDireccionOrigenRepartidor)
    val lblPesoPaqRepartidor = view.findViewById<TextView>(R.id.lblPesoPaqRepartidor)
    val lblAlturaPaqRepartidor = view.findViewById<TextView>(R.id.lblAlturaPaqRepartidor)
    val lblAnchoPaqRepartidor = view.findViewById<TextView>(R.id.lblAnchoPaqRepartidor)
    val lblLargoPaqRepartidor = view.findViewById<TextView>(R.id.lblLargoPaqRepartidor)
}