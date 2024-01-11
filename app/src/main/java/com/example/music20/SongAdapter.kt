package com.example.music20

import SongDataModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(val data:ArrayList<SongDataModel>,val bridge:SongInterface):RecyclerView.Adapter<SongAdapter.VH>() {
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title:TextView=itemView.findViewById(R.id.songTitle)
        val details:TextView=itemView.findViewById(R.id.details)
        val layout:LinearLayout=itemView.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.songtemplate,parent,false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.title.text=data[position].title
        holder.details.text=data[position].artist+"-"+data[position].album

        holder.layout.setOnClickListener{
            bridge.onClick(holder.adapterPosition)

        }

    }
}