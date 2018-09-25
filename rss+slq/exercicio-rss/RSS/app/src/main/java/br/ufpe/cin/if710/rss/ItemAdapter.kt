package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.itemlista.view.*


class ItemAdapter (private val item: List<ItemRSS>, private val context : Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){


    override fun getItemCount(): Int {
        return item.size
    }
    //retorna o layout criado pelo ViewHolder ja inflado na view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemlista,parent,false)
        return ViewHolder(view)
    }

    //Recupera o objeto da lista de objetos pela posição e associa a ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rss = item[position]
        val link = rss.link
        holder.titulo.text = rss.title
        holder.data.text = rss.pubDate
        //Deixando o titulo clicavel para abrir o link associado a ele
        holder.titulo.setOnClickListener{
            val i = Intent(ACTION_VIEW)
            i.data = Uri.parse(rss.link)
            context.startActivity(i)

        }

    }
    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        var titulo = item.item_titulo
        var data = item.item_data
    }

}








