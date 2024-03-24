package net.azarquiel.chistes.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import net.azarquiel.chistes.R
import net.azarquiel.chistes.model.Chiste

/**
 * Created by pacopulido on 9/10/18.
 */
class AdapterChistes(val context: Context,
                     val layout: Int
                    ) : RecyclerView.Adapter<AdapterChistes.ViewHolder>() {

    private var dataList: List<Chiste> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setChistes(chistes: List<Chiste>) {
        this.dataList = chistes
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Chiste){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            val ivrowchiste = itemView.findViewById(R.id.ivrowchiste) as ImageView
            val tvrowchiste = itemView.findViewById(R.id.tvrowchiste) as TextView
            tvrowchiste.text = dataItem.contenido
            if (dataItem.contenido.length>12) {
                var content = Html.fromHtml(dataItem.contenido.substring(0,12))
                tvrowchiste.text = "$content..."
            }


            // foto de internet a traves de Picasso
            Picasso.get().load("http://www.ies-azarquiel.es/paco/apichistes/img/${dataItem.idcategoria}.png").into(ivrowchiste)

            itemView.tag = dataItem
        }

    }
}