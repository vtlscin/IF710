package br.ufpe.cin.if710.calculadora

import android.app.Activity
import android.os.Bundle
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var valor = ""
        btn_0.setOnClickListener{
            valor += "0"
            text_calc.setText(valor)
        }
        btn_1.setOnClickListener{
            valor += "1"
            text_calc.setText(valor)
        }
        btn_2.setOnClickListener{
            valor += "2"
            text_calc.setText(valor)
        }
        btn_3.setOnClickListener{
            valor += "3"
            text_calc.setText(valor)
        }
        btn_4.setOnClickListener{
            valor += "4"
            text_calc.setText(valor)
        }
        btn_5.setOnClickListener{
            valor += "5"
            text_calc.setText(valor)
        }
        btn_6.setOnClickListener{
            valor += "6"
            text_calc.setText(valor)
        }
        btn_7.setOnClickListener{
            valor += "0"
            text_calc.setText(valor)
        }
        btn_8.setOnClickListener{
            valor += "8"
            text_calc.setText(valor)
        }
        btn_9.setOnClickListener{
            valor += "9"
            text_calc.setText(valor)
        }
        btn_Add.setOnClickListener{
            valor += "+"
            text_calc.setText(valor)
        }
        btn_Dot.setOnClickListener{
            valor += "."
            text_calc.setText(valor)
        }
        btn_Divide.setOnClickListener{
            valor += "/"
            text_calc.setText(valor)
        }
        btn_Multiply.setOnClickListener{
            valor += "*"
            text_calc.setText(valor)
        }
        btn_Subtract.setOnClickListener{
            valor += "-"
            text_calc.setText(valor)
        }
        btn_Power.setOnClickListener{
            valor += "^"
            text_calc.setText(valor)
        }
        btn_LParen.setOnClickListener{
            valor += "("
            text_calc.setText(valor)
        }
        btn_RParen.setOnClickListener{
            valor += ")"
            text_calc.setText(valor)
        }
        btn_Clear.setOnClickListener{
            valor = ""
            text_calc.setText(valor)
            text_info.setText(valor)

        }

        //Como usar a função:
        // eval("2+2") == 4.0
        // eval("2+3*4") = 14.0
        // eval("(2+3)*4") = 20.0
        //Fonte: https://stackoverflow.com/a/26227947
        fun eval(str: String): Double {
            return object : Any() {
                var pos = -1
                var ch: Char = ' '
                fun nextChar() {
                    val size = str.length
                    ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
                }

                fun eat(charToEat: Char): Boolean {
                    while (ch == ' ') nextChar()
                    if (ch == charToEat) {
                        nextChar()
                        return true
                    }
                    return false
                }

                fun parse(): Double {
                    nextChar()
                    val x = parseExpression()
                    if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                    return x
                }

                // Grammar:
                // expression = term | expression `+` term | expression `-` term
                // term = factor | term `*` factor | term `/` factor
                // factor = `+` factor | `-` factor | `(` expression `)`
                // | number | functionName factor | factor `^` factor
                fun parseExpression(): Double {
                    var x = parseTerm()
                    while (true) {
                        if (eat('+'))
                            x += parseTerm() // adição
                        else if (eat('-'))
                            x -= parseTerm() // subtração
                        else
                            return x
                    }
                }

                fun parseTerm(): Double {
                    var x = parseFactor()
                    while (true) {
                        if (eat('*'))
                            x *= parseFactor() // multiplicação
                        else if (eat('/'))
                            x /= parseFactor() // divisão
                        else
                            return x
                    }
                }

                fun parseFactor(): Double {
                    if (eat('+')) return parseFactor() // + unário
                    if (eat('-')) return -parseFactor() // - unário
                    var x: Double
                    val startPos = this.pos
                    if (eat('(')) { // parênteses
                        x = parseExpression()
                        eat(')')
                    } else if ((ch in '0'..'9') || ch == '.') { // números
                        while ((ch in '0'..'9') || ch == '.') nextChar()
                        x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                    } else if (ch in 'a'..'z') { // funções
                        while (ch in 'a'..'z') nextChar()
                        val func = str.substring(startPos, this.pos)
                        x = parseFactor()
                        if (func == "sqrt")
                            x = Math.sqrt(x)
                        else if (func == "sin")
                            x = Math.sin(Math.toRadians(x))
                        else if (func == "cos")
                            x = Math.cos(Math.toRadians(x))
                        else if (func == "tan")
                            x = Math.tan(Math.toRadians(x))
                        else
                            throw RuntimeException("Função desconhecida: " + func)
                    } else {
                        throw RuntimeException("Caractere inesperado: " + ch.toChar())
                    }
                    if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                    return x
                }
            }.parse()
        }


        btn_Equal.setOnClickListener{
            try {
                valor = eval(valor).toString()
                text_info.text = valor
            }
            catch(e:RuntimeException){
                Toast.makeText(this,"Expressao errada",Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("calc",text_calc.text.toString())
        outState?.putString("info",text_info.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val calc = savedInstanceState.getString("calc")
        val info = savedInstanceState.getString("info")
        text_calc.setText(calc)
        text_info.text = info
    }


}