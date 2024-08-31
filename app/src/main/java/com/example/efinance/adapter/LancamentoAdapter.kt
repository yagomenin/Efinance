package com.example.efinance.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.efinance.R
import com.example.efinance.database.DatabaseHandler
import java.text.NumberFormat
import java.util.Locale

class LancamentoAdapter(var cursor: Cursor ): RecyclerView.Adapter<LancamentoAdapter.LancamentoViewHolder>() {

    inner class LancamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        private val tvData: TextView = itemView.findViewById(R.id.tvData)
        private val tvValor: TextView = itemView.findViewById(R.id.tvValor)
        private val imgTipo: ImageView = itemView.findViewById(R.id.imgTipo)
        private var valor: Double = 0.0

        fun bind(cursor: Cursor) {
            with(cursor) {
                tvTipo.text = getString(getColumnIndexOrThrow("detalhe")).toString()
                tvData.text = getString(getColumnIndexOrThrow("data")).toString()
                valor = getString(getColumnIndexOrThrow("valor")).toDouble()
                tvValor.text = formatValor(valor)
                if (getString(getColumnIndexOrThrow("tipo")).toString() == "Credito") {
                    imgTipo.setImageResource(R.drawable.add_ic)
                } else {
                    imgTipo.setImageResource(R.drawable.less_icon)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LancamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lancamento_item, parent, false)
        return LancamentoViewHolder(view)
    }



    override fun getItemCount(): Int {
        return cursor.count
    }

    override fun onBindViewHolder(holder: LancamentoViewHolder, position: Int) {
        if (cursor.moveToPosition(position)) {
            holder.bind(cursor)
        }
    }

    private fun formatValor(valor: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(valor)
    }

    fun swapCursor(cursorAtualizado: Cursor) {
        if (cursor != cursorAtualizado) {
            cursor.close()
            cursor = cursorAtualizado
            notifyDataSetChanged()
        }
        var cursor = cursorAtualizado
        notifyDataSetChanged()
    }

    fun remove(position: Int, banco: DatabaseHandler) {
        cursor.moveToPosition(position)
        val id = cursor.getLong(cursor.getColumnIndexOrThrow("_id")).toInt()
        banco.deleteLanc(id)
        swapCursor(updateCursor(banco))
        notifyItemRemoved(position)
    }

    private fun updateCursor(banco: DatabaseHandler) : Cursor {
        return banco.listLanc()
    }
}