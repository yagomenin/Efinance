package com.example.efinance.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.example.efinance.models.Lancamento

class DatabaseHandler (context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + COLUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TIPO + " TEXT," +
            DETALHE + " TEXT," +
            DATA + " DATE," +
            VALOR + " REAL )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertLanc(lanc: Lancamento) {
        val db = this.writableDatabase
        val itens = ContentValues()

        itens.put(TIPO, lanc.tipo)
        itens.put(DETALHE, lanc.detalhe)
        itens.put(DATA, lanc.data_lanc)
        itens.put(VALOR, lanc.valor)

        db.insert(TABLE_NAME, null, itens)

    }

    fun listLanc(): Cursor {
        val db = this.writableDatabase

        val itens = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return itens
    }

    fun deleteLanc(id : Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "_id=$id", null)
    }




















    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NAME = "lancamento"
        private const val COLUM_ID = "_id"
        private const val TIPO = "tipo"
        private const val DETALHE = "detalhe"
        private const val DATA = "data"
        private const val VALOR = "valor"
    }


}