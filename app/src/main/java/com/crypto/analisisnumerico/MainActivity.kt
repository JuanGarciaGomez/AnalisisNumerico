package com.crypto.analisisnumerico

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.crypto.analisisnumerico.databinding.ActivityMainBinding
import java.lang.StringBuilder
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var btnAdd: Button
    private lateinit var btnSolve: Button
    private lateinit var funcion: TextView
    private lateinit var variable: RadioGroup
    private lateinit var coeficiente: EditText
    private lateinit var exponente: EditText
   private  var error:Boolean = false
    private  var message:String = ""
    private var variableFinal:String = ""
    private var NO_VARIABLE:String = ""
    private var VARIABLE:String = "X"
    private var  builder: StringBuilder = StringBuilder()

    private var function= {
            radio:RadioGroup, _:Int ->
        when (radio.checkedRadioButtonId){

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
        /*add()
        solve()*/
    }
    var encontrado =false
    private var contador:Int = 0
    private var totalValueFunction:Int = 0
    private fun solve() {


        btnSolve.setOnClickListener {
            if (funcion.text != null) {
                val vectorSplit = funcion.text.split(" ")
                println("f $vectorSplit")

                do {
                    val x0=contador

                    for(i in vectorSplit.indices ){
                        var vectorActual = vectorSplit.get(i)
                        var result = Math.pow(x0.toDouble(),vectorActual.substring(vectorActual.length-1,vectorActual.length).toDouble())
                        result *= vectorActual.substringBefore(VARIABLE).toDouble()
                        println("resultado$i : $result")
                        totalValueFunction += result.toInt()
                    }
                    contador+= contador+1
                }while(contador<=10);

            }
        }
    }


    private fun checkbox() {
        variable.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener(function))
    }

    private fun addPart(){

        btnAdd.setOnClickListener {
            val rta:String = method()

            if(error){
                funcion.text = rta
            }else{
                Toast.makeText(this,rta,Toast.LENGTH_LONG).show()
                error = false
            }
            funcion.text = rta

        }

    }

    private var firstPass:Boolean = false
    private fun method():String {

        if( firstPass){
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
        }else{
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
        /*else{

            if( coeficiente.text.toString().toInt() == 0){
                error = true
                message = "no puede ser 0"
                return  message
            }
            builder.append(coeficiente.text)
            builder.append(variableFinal)

            if(variableFinal==EXPONENT){
                if(exponente.text !=null || !exponente.text.equals("")){
                    builder.append("^")
                    builder.append(exponente.text)
                    builder.append(" ")
                }

            }else{
                builder.append(" ")
            }

        }*/
        return builder.toString()

    }

    private fun initBinding() {
        btnAdd = binding.btnAdd
        btnSolve = binding.btnSolve
        funcion = binding.txtFuncion
        variable = binding.opcionVariable
        coeficiente = binding.etCoeficiente
        exponente = binding.etExponente
    }
   /* private fun solve() {
        btnSolve.setOnClickListener {
            //funcionSeparada(funcion.text.split("="))
        }
    }

    fun funcionSeparada(split: List<String>, x: Double): Double {
        var funcion = split[2]
        return 0.0
    }
   fun funcion(x: Double): Double {
        return (x * x * x) + 2 * (x * x) + (10 * x) - 20

    }

    fun funcalcRaiz():Double {
        var i = 1
        while(e>0.001)
        {
            x[i+1]=x[i]-( (funcion(x[i])*(x[i-1]-x[i]))/(funcion(x[i-1])-funcion(x[i])) );

            e= kotlin.math.abs((x[i + 1] - x[i]) / (x[i + 1])) *100;
            i++
        }
        return x[i];
    }

    fun respuesta(): String {
        var i = 2
        var x0 = 0.0
        var x1 = 1.0
        var rta: Double
        var rtaAnterior = 0.0
        do {
            rta = x1 - ((x1-x0)*funcionSeparada(funcion.text.split("="),x1))/(funcionSeparada(funcion.text.split("="),x1)-funcionSeparada(funcion.text.split("="),x0))
            x0 = x1
            x1 = rta
            rtaAnterior = x0
            i++
        } while(rta != rtaAnterior)

        return "i$rta"
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


            coeficiente.setText("")
            exponente.setText("")
        }
    }
*/


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
}
