package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.onUiThread
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : Activity() {

    private lateinit var RSS_FEED: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RSS_FEED = getString(R.string.rssfeed)
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        conteudoRSS.layoutManager = layoutManager
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
