package com.example.myapplication
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class calculator_app  : AppCompatActivity() {
    private lateinit var expressionText: TextView
    private lateinit var tvResult: TextView

    private var currentNumber = ""
    private var previousNumber = ""
    private var operator: String? = null
    private var isOperatorClicked = false

    private var isResultDisplayed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calculator_app)

        expressionText = findViewById(R.id.expressionText)
        tvResult = findViewById(R.id.tvResult)

        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2,
            R.id.btn3, R.id.btn4, R.id.btn5,
            R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener { numberButtonClick((it as Button).text.toString()) }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { operatorClick("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { operatorClick("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { operatorClick("x") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { operatorClick("/") }

        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }

        findViewById<Button>(R.id.btnC).setOnClickListener {
            currentNumber = ""
            previousNumber = ""
            operator = null
            isOperatorClicked = false
            expressionText.text = ""
            tvResult.text = ""
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (isResultDisplayed) {
                currentNumber = "0."
                previousNumber = ""
                operator = null
                expressionText.text = ""
                isResultDisplayed = false
            } else {
                if (!currentNumber.contains(".")) {
                    currentNumber = if (currentNumber.isEmpty()) {
                        "0."
                    } else {
                        currentNumber + "."
                    }
                }
            }
            tvResult.text = currentNumber
        }

        findViewById<Button>(R.id.btnCE).setOnClickListener {
            currentNumber = ""
            tvResult.text = ""
        }

        findViewById<Button>(R.id.btnBackspace).setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                currentNumber = currentNumber.dropLast(1)
                tvResult.text = currentNumber
            }
        }


        findViewById<Button>(R.id.btnNegate).setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                currentNumber = if (currentNumber.startsWith("-")) {
                    currentNumber.removePrefix("-")
                } else {
                    "-$currentNumber"
                }
                tvResult.text = currentNumber
            }
        }
    }

    private fun numberButtonClick(number: String) {
        if (isResultDisplayed) {
            currentNumber = number
            previousNumber = ""
            operator = null
            expressionText.text = ""
            isResultDisplayed = false
        } else {
            currentNumber += number
        }
        tvResult.text = currentNumber
    }

    private fun operatorClick(op: String) {
        isResultDisplayed = false
        if (currentNumber.isNotEmpty()) {
            if (previousNumber.isNotEmpty() && operator != null) {
                calculateResult()
            }
            operator = op
            previousNumber = currentNumber
            currentNumber = ""
            expressionText.text = "$previousNumber $operator"
            tvResult.text = ""
            isOperatorClicked = true
        }
    }

    private fun calculateResult() {
        if (previousNumber.isNotEmpty() && currentNumber.isNotEmpty() && operator != null) {
            val num1 = previousNumber.toDouble()
            val num2 = currentNumber.toDouble()
            val result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "x" -> num1 * num2
                "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }

            expressionText.text = "$previousNumber $operator $currentNumber"

            val resultText = if (result % 1 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }

            tvResult.text = resultText

            currentNumber = resultText
            operator = null
            isResultDisplayed = true
        }
    }

}