package com.crypto.analisisnumerico

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.crypto.analisisnumerico.databinding.ActivityMainBinding
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
    private var builderInfo: StringBuilder = StringBuilder()

    private var function = { radio: RadioGroup, _: Int ->
        when (radio.checkedRadioButtonId) {
            R.id.radio_no -> {
                variableFinal = NO_VARIABLE
                exponente.visibility = View.GONE
            }
            R.id.radio_si -> {
                variableFinal = VARIABLE
                exponente.visibility = View.VISIBLE
            }
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
            resultados.text = ""
            totalValueFunction = 0.0
        }
    }


    private var encontrado = false
    private var isPrimary = true
    private var totalValueFunction: Double = 0.0
    private var porcentaje: Double = 0.0
    private fun solve() {
        //Falta validar si esta vacio
        btnSolve.setOnClickListener {
            methodTwo()
        }
    }

    private fun methodTwo() {
        if (!funcion.text.toString().isEmpty() && funcion.text.toString().contains(VARIABLE)) {
            val vectorSplit = funcion.text.split(" ")
            println("f $vectorSplit")

            var contador = 0
            var inicial = 0.0
            var x0Value = 0.0
            var x1Value = 1.0
            var rtaAfter = 0.0

            do {

                var result =1.0
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
                    if(vectorActual.contains(VARIABLE)) {
                         result = Math.pow(
                            inicial.toDouble(),
                            vectorActual.substring(vectorActual.length - 1, vectorActual.length)
                                .toDouble()
                        )
                    }else{
                        result = 1.0
                    }
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

                    builderInfo.append("Iteracion ${contador + 1} valor : $totalValueFunction con porcentaje: $porcentaje")
                        .append("\n").append("\n")
                    resultados.text = builderInfo.toString()
                    if (rtaPorcentaje) {
                        encontrado = true
                        return
                    }

                    try {
                        if (x0Value.toString().substring(0, 5) == x1Value.toString()
                                .substring(0, 5)
                        ) {
                            encontrado = true
                            return
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                contador++
            } while (contador <= 13 || encontrado)

        }else{
           Toast.makeText(this,"FUNCION VACIA O SIN VARIBALE",Toast.LENGTH_LONG).show()
        }
    }

    private fun calcularPorcentaje(fuctionValue: Double, x0Value: Double): Boolean {
        porcentaje = 0.0
        porcentaje = abs((fuctionValue - (x0Value)) / fuctionValue) * 100

        if (porcentaje < 1.0) {
            return true
        }
        return false
    }


    private fun checkbox() {
        variable.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener(function))
    }

    private fun addPart() {
        //Validar si esta vacio
        btnAdd.setOnClickListener {
            val rta: String = method()
            if (error) {
                error = false
                Toast.makeText(this, rta,Toast.LENGTH_LONG).show()
            } else {
                funcion.text = rta
                coeficiente.text = null
                exponente.text = null
            }
        }

    }

   private  fun messageCoeficienteExponenteEmpty() :String{
       if (coeficiente.text.toString() == "" || exponente.text.toString() == "") {
           message = "El coeficiente y el exponente se encuentran vacio"
       }else{
           message = "Debe marcar una opcion del combo box"

       }

       error =true
        return message
    }

    private var firstPass: Boolean = false
    private fun method(): String {

        when(variable.checkedRadioButtonId) {
            R.id.radio_no -> {
                if (variableFinal == NO_VARIABLE && coeficiente.text.toString() == "") {
                    error = true
                    message = "El coeficiente se encuentra vacio"
                    return  message
                }
            }

            R.id.radio_si -> {
                if (coeficiente.text.toString() == "" || exponente.text.toString() == "") {
                    error = true
                    message = "El coeficiente y el exponente se encuentran vacio"
                    return message
                }
            }

            else -> {
                return messageCoeficienteExponenteEmpty()
            }

        }

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

        if (variableFinal == VARIABLE && (exponente.text != null || !exponente.text.equals(""))) {
            builder.append("^")
            builder.append(exponente.text)
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
