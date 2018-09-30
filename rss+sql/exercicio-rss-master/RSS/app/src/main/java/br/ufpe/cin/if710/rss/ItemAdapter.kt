package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.itemlista.view.*
import kotlinx.coroutines.experimental.newCoroutineContext
import org.jetbrains.anko.db.update
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


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
        holder.titulo.text = rss.title
        holder.data.text = rss.pubDate
        //Deixando o titulo clicavel para abrir o link associado a ele
            holder.titulo.setOnClickListener {
                val i = Intent(ACTION_VIEW)
                i.data = Uri.parse(rss.link)
                //marca cada item no banco de dados como lido apos o click
                doAsync {
                    context.database.use {
                        update(SqlLinkHelperAnko.DATABASE_TABLE, SqlLinkHelperAnko.STATUS to "lido")
                                .whereSimple(SqlLinkHelperAnko.LINK_TITLE, rss.title)
                                .exec()

                    }
                    uiThread {
                    }
                }
                context.startActivity(i)
            }
    }
    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        var titulo = item.item_titulo
        var data = item.item_data
    }

}








