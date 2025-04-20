package com.example.efinance.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.efinance.models.Lancamento
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import android.database.Cursor

class DatabaseHandlerTest {

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()

        databaseHandler = DatabaseHandler(context)

        databaseHandler.writableDatabase.execSQL("DELETE FROM lancamento")
    }

    @Test
    fun `testa insercao de lancamento`() {

        val lancamento = Lancamento(
            _id = 0,
            tipo = "Credito",
            detalhe = "Salario",
            data_lanc = "2025-04-20",
            valor = 1000.0
        )


        databaseHandler.insertLanc(lancamento)


        val cursor: Cursor = databaseHandler.listLanc()


        assertTrue(cursor.moveToFirst())


        val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
        val detalhe = cursor.getString(cursor.getColumnIndexOrThrow("detalhe"))
        val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))

        assertEquals("Credito", tipo)
        assertEquals("Salario", detalhe)
        assertEquals(1000.0, valor, 0.0)
    }

    @Test
    fun `testa listagem de lancamentos`() {

        val lancamento1 = Lancamento(0, "Credito", "Salario", "2025-04-20", 1500.0)
        val lancamento2 = Lancamento(0, "Debito", "Compra", "2025-04-21", 200.0)

        databaseHandler.insertLanc(lancamento1)
        databaseHandler.insertLanc(lancamento2)

        val cursor: Cursor = databaseHandler.listLanc()

        assertTrue(cursor.moveToFirst())

        val tipo1 = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
        val valor1 = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))


        assertEquals("Credito", tipo1)
        assertEquals(1500.0, valor1, 0.0)


        assertTrue(cursor.moveToNext())

        val tipo2 = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
        val valor2 = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))


        assertEquals("Debito", tipo2)
        assertEquals(200.0, valor2, 0.0)
    }
}
