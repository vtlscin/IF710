package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.custom.onUiThread
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import org.jetbrains.anko.db.insert


class MainActivity : Activity() {

    private lateinit var RSS_FEED: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RSS_FEED = getString(R.string.rssfeed)
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        conteudoRSS.layoutManager = layoutManager
        setActionBar(actionbar)
    }
    // classe criada com o intuito de usar essa variavel feed em outro codigo
    class myclass {
        companion object {
            lateinit var feed: List<ItemRSS>
        }
    }
    //Cria a actionbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Define um menu na actionbar
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.action_settings -> {
                startActivity(Intent(this@MainActivity,PrefsActivity::class.java))
            }
            //alem do botao pedido no item 9 do exercicio, tbm criei um botao para visualizar os itens no banco de dados
            R.id.banco ->{
                startActivity(Intent(this@MainActivity,SQLActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onStart() {
        super.onStart()
        try {
            atualizarconteudo()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }
    // Realizando a operaçao de rede fora da thread principal 
    private fun atualizarconteudo() {
        doAsync {
            val feedXML = getRssFeed(RSS_FEED)
            var rParse = ParserRSS.parse(feedXML)
            myclass.feed = rParse
            uiThread {
                conteudoRSS.adapter = ItemAdapter(rParse,this@MainActivity)
            }
        }

    }
    @Throws(IOException::class)
    private fun getRssFeed(feed: String): String {
        var `in`: InputStream? = null
        var rssFeed = ""
        try {
            val url = URL(feed)
            val conn = url.openConnection() as HttpURLConnection
            `in` = conn.inputStream
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var count: Int
            count = 0
            while (count != -1) {
                out.write(buffer, 0, count)
                count = `in`!!.read(buffer)
            }
            val response = out.toByteArray()
            rssFeed = String(response, charset("UTF-8"))
        } finally {
            `in`?.close()
        }
        return rssFeed
    }
}
