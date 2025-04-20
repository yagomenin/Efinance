package com.example.efinance

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.efinance.adapter.LancamentoAdapter
import com.example.efinance.database.DatabaseHandler
import com.example.efinance.databinding.ActivityGastosBinding
import java.text.NumberFormat
import java.util.Locale

class GastosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGastosBinding
    private var banco = DatabaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGastosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createSaldo()

        binding.btLanc.setOnClickListener() {
            onClickbtLanc()
        }

        binding.rcView.layoutManager = LinearLayoutManager(this)

        val cursor = banco.listLanc()

        val adapter = LancamentoAdapter(cursor)

        binding.rcView.adapter = adapter

        val itemTouchHelperCallback = ItemTouchHelperCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rcView)
    }

    fun onClickbtLanc () {
        val intent = Intent(this, TelaLancamento::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        createSaldo()
        val cursor = banco.listLanc()
        (binding.rcView.adapter as LancamentoAdapter).swapCursor(cursor)
    }

    fun calculaSaldo(): String{
        var result: String = ""
        var receitasCredito: Double = 0.0
        var receitasDebito: Double = 0.0
        val cursor = banco.listLanc()
        with(cursor) {
            while (moveToNext()) {
                val tipo = getString(getColumnIndexOrThrow("tipo")).toString()
                val valor = getString(getColumnIndexOrThrow("valor")).toDouble()
                if (tipo == "Credito") {
                    receitasCredito += valor
                } else {
                    receitasDebito += valor
                }
            }
            val total: Double = receitasCredito - receitasDebito
            result = formatValor(total)
        }
        return result
    }

    private fun formatValor(valor: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(valor)
    }


    private fun createSaldo() {
        val vlrsaldo: MenuItem = binding.topAppBar.menu.findItem(R.id.vlrSaldo)
        vlrsaldo.title = calculaSaldo()
    }

    inner class ItemTouchHelperCallback(private val adapter: LancamentoAdapter) : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            adapter.remove(position, banco)
            adapter.notifyItemRemoved(viewHolder.adapterPosition)
            createSaldo()
        }
    }
}