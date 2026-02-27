package com.example.calculadora

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var tvPantalla: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvPantalla = findViewById(R.id.txtResultado)

        // -------------------------
        // BOTONES NUMÉRICOS
        // -------------------------
        val botonesNumeros = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9
        )

        botonesNumeros.forEach { id ->
            findViewById<MaterialButton>(id).setOnClickListener {
                val textoActual = tvPantalla.text.toString()

                if (textoActual == "0" || textoActual == "Error") {
                    tvPantalla.text = (it as MaterialButton).text
                } else {
                    tvPantalla.append((it as MaterialButton).text)
                }
            }
        }

        // -------------------------
        // BOTÓN PUNTO (coma visual)
        // -------------------------
        findViewById<MaterialButton>(R.id.btnPunto).setOnClickListener {
            tvPantalla.append(".")
        }

        // -------------------------
        // OPERACIONES
        // -------------------------
        val operaciones = mapOf(
            R.id.btnSumar to "+",
            R.id.btnRestar to "-",
            R.id.btnMultiplicar to "x",
            R.id.btnDividir to "÷",
            R.id.btnPorcentaje to "%"
        )

        operaciones.forEach { (id, simbolo) ->
            findViewById<MaterialButton>(id).setOnClickListener {
                tvPantalla.append(simbolo)
            }
        }

        // -------------------------
        // BOTÓN AC
        // -------------------------
        findViewById<MaterialButton>(R.id.btnAC).setOnClickListener {
            tvPantalla.text = "0"
        }

        // -------------------------
        // BOTÓN BORRAR
        // -------------------------
        findViewById<MaterialButton>(R.id.btnBorrar).setOnClickListener {
            val texto = tvPantalla.text.toString()
            if (texto.isNotEmpty() && texto != "0") {
                tvPantalla.text = texto.dropLast(1)
                if (tvPantalla.text.isEmpty()) {
                    tvPantalla.text = "0"
                }
            }
        }

        // -------------------------
        // BOTÓN +/-
        // -------------------------
        findViewById<MaterialButton>(R.id.btnInverso).setOnClickListener {
            val texto = tvPantalla.text.toString()
            if (texto.isNotEmpty() && texto != "0" && texto != "Error") {
                if (texto.startsWith("-")) {
                    tvPantalla.text = texto.substring(1)
                } else {
                    tvPantalla.text = "-$texto"
                }
            }
        }

        // -------------------------
        // BOTÓN IGUAL
        // -------------------------
        findViewById<MaterialButton>(R.id.btnIgual).setOnClickListener {
            val resultado = calcularResultado(tvPantalla.text.toString())
            tvPantalla.text = resultado
        }
    }

    // -------------------------
    // FUNCIÓN DE CÁLCULO
    // -------------------------
    private fun calcularResultado(operacion: String): String {

        if (operacion.isEmpty() || operacion == "Error") return "0"

        return try {
            val textoLimpio = operacion
                .replace("x", "*")
                .replace("÷", "/")
                .replace("%", "/100")

            val expresion = ExpressionBuilder(textoLimpio).build()
            val resultado = expresion.evaluate()

            if (resultado % 1 == 0.0) {
                resultado.toLong().toString()
            } else {
                resultado.toString()
            }

        } catch (e: Exception) {
            "Error"
        }
    }
}