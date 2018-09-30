package br.ufpe.cin.if710.rss

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import br.ufpe.cin.if710.rss.MainActivity.myclass.Companion.feed
import org.jetbrains.anko.db.*

//classe do banco de dados
//associado a cada item alem dos campos de data e titulo de cada link, tambem coloquei um canto de status que determina se foi lido ou nao
class SqlLinkHelperAnko(ctx:Context) : ManagedSQLiteOpenHelper(ctx, "if710", null,1){

    companion object {
        val DATABASE_TABLE = "links"
        private val DB_VERSION = 1
        val LINK_TITLE = "titulo"
        val LINK_DATE = "data"
        val _ID = "_id"
        val STATUS = "Lido"
        val columns = arrayOf(_ID, LINK_TITLE, LINK_DATE, STATUS)

        private var instance: SqlLinkHelperAnko? = null

        @Synchronized
        fun getInstance (ctx: Context): SqlLinkHelperAnko{
            if (instance == null){
                instance = SqlLinkHelperAnko(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(DATABASE_TABLE,true,
                _ID to INTEGER + PRIMARY_KEY,
                LINK_TITLE to TEXT,
                LINK_DATE to TEXT,
                STATUS to TEXT)
        var i = 0
        for (item in feed) {
            db.insert(DATABASE_TABLE,
                    _ID to i, LINK_TITLE to item.title , LINK_DATE to item.pubDate, STATUS to "nao lido")
            i++
        }

    }



    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        //
    }

}

val Context.database: SqlLinkHelperAnko
    get() = SqlLinkHelperAnko.getInstance(applicationContext)
