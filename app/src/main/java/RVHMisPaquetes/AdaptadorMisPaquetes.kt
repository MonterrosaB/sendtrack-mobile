package RVHMisPaquetes

import Modelo.dtMisPaquetes
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class AdaptadorMisPaquetes(var Datos: List<dtMisPaquetes>):RecyclerView.Adapter<ViewHolderMisPaquetes>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMisPaquetes {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_mis_paquetes, parent, false)
        return ViewHolderMisPaquetes(vista)
    }

    override fun onBindViewHolder(holder: ViewHolderMisPaquetes, position: Int) {
    }

    override fun getItemCount() = Datos.size
}