package com.crypto.analisisnumerico

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crypto.analisisnumerico.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var btnAdd: Button
    private lateinit var btnSolve: Button
    private lateinit var funcion: TextView
    private lateinit var variable: EditText
    private lateinit var coeficiente: EditText
    private lateinit var exponente: EditText
    private var finalFunction = ""
    private var firstAdd = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBinding()
        add()
        solve()
    }

    private fun solve() {
        btnSolve.setOnClickListener {

        }
    }

    @SuppressLint("SetTextI18n")
    private fun addParts() {
        val coeficienteConSigno =
            if (coeficiente.text.substring(0, 1) != "-" && coeficiente.text.substring(
                    0,
                    1
                ) != "+"
            ) "+${coeficiente.text}"
            else coeficiente.text.toString()

        finalFunction += if (exponente.text.equals("1") || exponente.text.isEmpty())
            "${coeficienteConSigno}${variable.text}"
        else "${coeficienteConSigno}${variable.text}^${exponente.text}"

        val finalVariable = finalFunction.substring(1, 2)
        if (finalFunction.substring(0, 1) == "+") finalFunction =
            finalFunction.replace(finalFunction.substring(0, 1), "")

        funcion.text = "F(${finalVariable}) = $finalFunction"

    }

    private fun add() {
        btnAdd.setOnClickListener {
            if (firstAdd) {
                if (variable.text.isNotEmpty()) addParts()
                else Toast.makeText(this, "Asigne una variable inicial", Toast.LENGTH_SHORT).show()
            } else {
                if (coeficiente.text.isNotEmpty() || !coeficiente.text.equals("0")) {
                    addParts()
                }
            }

            variable.setText("")
            coeficiente.setText("")
            exponente.setText("")
        }
    }

    private fun initBinding() {
        btnAdd = binding.btnAdd
        btnSolve = binding.btnSolve
        funcion = binding.txtFuncion
        variable = binding.etVaribale
        coeficiente = binding.etCoeficiente
        exponente = binding.etExponente
    }
}
