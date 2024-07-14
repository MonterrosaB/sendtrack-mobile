package RVHEmpleados

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class AdaptadorEmpleados(val context: Context): RecyclerView.Adapter<AdaptadorEmpleados.ViewHolder>() {
    val nombres = arrayOf("Rodrigo Monterrosa",
        "Gerardo Quintanilla",
        "Rodrigo Torres")

    val detailsV = arrayOf("P9901",
        "P9902",
        "P9903")

    val detailsZ = arrayOf("San Miguel",
        "Soyapango",
        "San Salvador")

    val detailsH = arrayOf("7:00 am - 2:00 pm",
        "12:00 md - 8:00 pm",
        "9:00 am - 4:00 pm")

    val images = arrayOf(
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground)


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder{
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return nombres.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i:Int){
        holder.itemNombre.text = nombres[i]
        holder.itemDetailsV.text = detailsV[i]
        holder.itemDetailsZ.text = detailsZ[i]
        holder.itemDetailsH.text = detailsH[i]
        holder.itemImage.setImageResource(images[i])
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemNombre: TextView
        var itemDetailsV: TextView
        var itemDetailsZ: TextView
        var itemDetailsH: TextView


        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemNombre = itemView.findViewById(R.id.lbNombre)
            itemDetailsV = itemView.findViewById(R.id.lbDetalleV)
            itemDetailsZ = itemView.findViewById(R.id.lbDetalleZ)
            itemDetailsH = itemView.findViewById(R.id.lbDetalleH)
        }
    }
}