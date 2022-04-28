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
    private lateinit var rta: TextView
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
            rta.text= respuesta()
        }
    }
    /*
     var funcion = "3X^1+5X^2"
        var funcionRemplazada = funcion.replace("X", "1")
        println(funcionRemplazada)
        var x = funcionRemplazada.split("+")
        println(x)
        var y = x[1].split("^")
        println(y)
        var z = Math.pow(y[0].substring(1,2).toDouble(),y[1].toDouble())
        println(z)
     */

    /*    fun funcionSeparada(split: List<String>, x: Double): Double {
            //3x+1x+2
            var funcion = split[1]
            var funcionRemplazada = funcion.replace("X", x.toString())

            var x = funcionRemplazada.split("^")
            Math.pow(x[0].substring(0, 1).toDouble(), 2.0)


            return 0.0
        }*/
    private fun funcion(x: Double): Double {
        return (x * x * x) + 2 * (x * x) + (10 * x) - 20
    }

    private fun respuesta(): String {
        var i = 2
        var x0 = 0.0
        var x1 = 1.0
        var rta: Double
        var rtaAnterior = 0.0
        do {
            rta = x1 - ((x1 - x0) * funcion(x1)) / (funcion(x1) - funcion(x0))
            x0 = x1
            x1 = rta
            rtaAnterior = x0
            i++
        } while (rta != rtaAnterior)

        return "En la iteracion $i la raiz es $rta"
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
        rta = binding.txtRta
    }
}
