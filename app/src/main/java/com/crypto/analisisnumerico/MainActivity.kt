package com.crypto.analisisnumerico

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.crypto.analisisnumerico.databinding.ActivityMainBinding
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var btnAdd: Button
    private lateinit var btnSolve: Button
    private lateinit var btnClear: Button
    private lateinit var funcion: TextView
    private lateinit var resultados: TextView
    private lateinit var variable: RadioGroup
    private lateinit var coeficiente: EditText
    private lateinit var exponente: EditText
    private var error: Boolean = false
    private var message: String = ""
    private var variableFinal: String = ""
    private var NO_VARIABLE: String = ""
    private var VARIABLE: String = "X"
    private var builder: StringBuilder = StringBuilder()
    private var builderInfo:StringBuilder = StringBuilder()

    private var function = { radio: RadioGroup, _: Int ->
        when (radio.checkedRadioButtonId) {

            R.id.radio_no -> variableFinal = NO_VARIABLE
            R.id.radio_si -> variableFinal = VARIABLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBinding()
        checkbox()
        addPart()
        solve()
        clearView()
    }

    private fun clearView() {
        btnClear.setOnClickListener {
            exponente.setText("")
            coeficiente.setText("")
            funcion.text = ""
            encontrado = false
            isPrimary = true
            builder = StringBuilder()
            firstPass = false
            builderInfo = StringBuilder()
            resultados.setText("")
        }
    }


    private var encontrado = false
    private var isPrimary = true
    private var totalValueFunction: Double = 0.0
    private var porcentaje: Double = 0.0
    private fun solve() {


        btnSolve.setOnClickListener {
            methodTwo()
        }
    }

    private fun methodTwo() {
        if (funcion.text != null || !funcion.text.toString().contains(VARIABLE)) {
            val vectorSplit = funcion.text.split(" ")
            println("f $vectorSplit")

            var contador = 0
            var inicial = 0.0
            var x0Value = 0.0
            var x1Value = 1.0
            var rtaAfter = 0.0

            do {


                inicial = when (contador) {
                    0 -> x0Value
                    1 -> x1Value
                    else -> totalValueFunction

                }

                if (!isPrimary) {
                    totalValueFunction = 0.0
                }

                for (i in vectorSplit.indices) {
                    val vectorActual = vectorSplit.get(i)
                    var result = Math.pow(
                        inicial.toDouble(),
                        vectorActual.substring(vectorActual.length - 1, vectorActual.length)
                            .toDouble()
                    )
                    result *= vectorActual.substringBefore(VARIABLE).toDouble()
                    println("resultado$i : $result")
                    totalValueFunction += result

                }

                if (isPrimary) {
                    rtaAfter = totalValueFunction
                    isPrimary = false
                } else {

                    val fuctionValue =
                        (x1Value - totalValueFunction * ((x0Value - x1Value) / (rtaAfter - (totalValueFunction))))
                    rtaAfter = totalValueFunction
                    x0Value = x1Value
                    x1Value = fuctionValue
                    totalValueFunction = fuctionValue
                    val rtaPorcentaje = calcularPorcentaje(fuctionValue, x0Value)

                    builderInfo.append("Iteracion ${contador+1} valor : $totalValueFunction con porcentaje: $porcentaje").append("\n").append("\n")
                    resultados.text =builderInfo.toString()
                    if (rtaPorcentaje) {
                        encontrado = true
                        return
                    }

                    try {
                        if(x0Value.toString().substring(0,4).equals(x1Value.toString().substring(0,4))){
                            encontrado=true
                            return
                        }

                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }

                contador++
            } while (contador <= 10 || encontrado)

        }
    }

    private fun calcularPorcentaje(fuctionValue: Double, x0Value: Double): Boolean {
        porcentaje = 0.0
        porcentaje = abs((fuctionValue - x0Value) / fuctionValue) * 100

        if (porcentaje < 1.0) {
            return true
        }
        return false
    }


    private fun checkbox() {
        variable.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener(function))
    }

    private fun addPart() {

        btnAdd.setOnClickListener {
            val rta: String = method()

            if (error) {
                funcion.text = rta
            } else {
                Toast.makeText(this, rta, Toast.LENGTH_LONG).show()
                error = false
            }
            funcion.text = rta

        }

    }

    private var firstPass: Boolean = false
    private fun method(): String {

        if (firstPass) {
            builder.append(" ")
        }
        if (coeficiente.text.toString()
                .toInt() == 0 || (coeficiente.text.equals("") || coeficiente.text == null)
        ) {
            error = true
            message = "no puede ser 0"
            return message
        }

        if (firstPass && (coeficiente.text.substring(0, 1) != "+" && coeficiente.text.substring(
                0,
                1
            ) != "-"
                    )
        ) {
            builder.append("+")
        }

        if (coeficiente.text.substring(0, 1) == "+") {
            if (!firstPass) {
                builder.append(coeficiente.text.substring(1, coeficiente.length()))
                firstPass = true
            }
            builder.append(coeficiente.text)
        } else {
            builder.append(coeficiente.text)
        }

        builder.append(variableFinal)

        if (variableFinal == VARIABLE) {
            if (exponente.text != null || !exponente.text.equals("")) {
                builder.append("^")
                builder.append(exponente.text)
            }
        }
        firstPass = true
        return builder.toString()

    }

    private fun initBinding() {
        resultados = binding.etInfo
        btnClear = binding.btnClean
        btnAdd = binding.btnAdd
        btnSolve = binding.btnSolve
        funcion = binding.txtFuncion
        variable = binding.opcionVariable
        coeficiente = binding.etCoeficiente
        exponente = binding.etExponente
    }
}
