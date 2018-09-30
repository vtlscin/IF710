package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.widget.SimpleCursorAdapter
import android.database.Cursor
import android.view.View
import android.widget.AdapterView
import android.widget.CursorAdapter
import kotlinx.android.synthetic.main.banco.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


//classe criada com o intuito de visualizar os itens no banco de dados


class SQLActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.banco)

        val adapter = SimpleCursorAdapter(this,R.layout.itembanco,null,
                arrayOf(SqlLinkHelperAnko.LINK_DATE,SqlLinkHelperAnko.LINK_TITLE,SqlLinkHelperAnko.STATUS),
                intArrayOf(R.id.item_tituloB,R.id.item_dataB,R.id.item_status),0)

        link.setAdapter(adapter)

        link.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            public override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val cursor = (parent.adapter as SimpleCursorAdapter).cursor

                cursor.moveToPosition(position)
            }
        })

        }


    protected override fun onResume() {
        super.onResume()

        doAsync {
            val c = doQuery()
            uiThread {
                (link.getAdapter() as CursorAdapter).changeCursor(c)
            }
        }
    }

    protected override fun onDestroy() {
        //Fecha o Cursor
        (link.getAdapter() as SimpleCursorAdapter).getCursor().close()
        //Fecha conexão com banco de dados
        database.close()
        super.onDestroy()
    }

    internal fun doQuery(): Cursor {
        val result = database.readableDatabase.query(
                SqlLinkHelperAnko.DATABASE_TABLE,
                SqlLinkHelperAnko.columns,
                null,
                null,
                null,
                null,
                SqlLinkHelperAnko.LINK_TITLE)

        //força a query a ser executada
        //so executa quando fazemos algo que precisa do resultset
        result.getCount()

        return result
    }
}


