package com.example.efinance

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import com.example.efinance.database.DatabaseHandler
import com.example.efinance.databinding.ActivityTelaLancamentoBinding
import com.example.efinance.models.Lancamento
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date
import java.util.Locale

class TelaLancamento : AppCompatActivity() {
    private lateinit var binding: ActivityTelaLancamentoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaLancamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val banco = DatabaseHandler(this)

        binding.btLancar.setOnClickListener() {
            btlancOnClickListener(banco)
        }

        binding.data.isClickable = false

        binding.tvData.setEndIconOnClickListener() {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(supportFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener {position ->
                val umDiaEmMiliSeconds = 24 * 60 * 60 * 1000
                val dataCorreta = position + umDiaEmMiliSeconds
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dataFormat = simpleDateFormat.format(Date(dataCorreta)).toString()

                binding.data.setText(dataFormat)
            }
        }



        val tipo = arrayOf("Credito", "Debito")
        val adapterOptions = ArrayAdapter(this, R.layout.drop_itens_spinner1, tipo)
        binding.tipo.setAdapter(adapterOptions)

        binding.tipo.setOnItemClickListener{ parent, view, position, id ->
            when (position) {
                0 -> {
                    val opcaoCredito = arrayOf("Salario", "Extras")
                    val adapterCredito = ArrayAdapter(this, R.layout.drop_itens_spinner1, opcaoCredito)
                    binding.detalhe.setAdapter(adapterCredito)
                    binding.detalhe.setText(opcaoCredito[0], false)
                }
                1 -> {
                    val opcaoDebito = arrayOf("Alimentação", "Transporte", "Contas")
                    val adapterDebito = ArrayAdapter(this, R.layout.drop_itens_spinner1, opcaoDebito)
                    binding.detalhe.setAdapter(adapterDebito)
                    binding.detalhe.setText(opcaoDebito[0], false)

                }
            }
        }
    }

    fun btlancOnClickListener(banco: DatabaseHandler) {
        val lancamento = Lancamento(
            0,
            binding.tipo.text.toString(),
            binding.detalhe.text.toString(),
            binding.data.text.toString(),
            binding.valor.text.toString().toDouble()
        )

        banco.insertLanc(lancamento)
        Toast.makeText(this, "Lançamento Realizado", Toast.LENGTH_LONG).show()

        val intent = Intent(this, GastosActivity::class.java)
        startActivity(intent)
        finish()
    }
}