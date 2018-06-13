package mathparser

import java.util.*

class Tokenizer (inStr:String) {
    private val operators = Arrays.asList('+', '-', '*', '/')

    private fun isOperator(char: Char):Boolean {
        return operators.contains(char)
    }
    private fun isLeftParenthesis(char: Char):Boolean {
        return char == '('
    }

    private fun isRightParenthesis(char: Char):Boolean {
        return char == ')'
    }
    private fun isComma(char: Char):Boolean {
        return char == ','
    }

    private var letterBuffer: ArrayList<Char> = ArrayList()
    private var numberBuffer: ArrayList<Char> = ArrayList()
    var tokens: ArrayList<Token> = ArrayList()

    private fun emptyNumberBufferAsLiteral() {

        if (letterBuffer.isNotEmpty()) {
            val varName = ArrayList<Char>()
            varName.addAll(letterBuffer)
            varName.addAll(numberBuffer)
            tokens.add(Token("Variable", varName.joinToString("")))

            numberBuffer = ArrayList()
            letterBuffer = ArrayList()
        }

        if(numberBuffer.isNotEmpty()) {
            tokens.add(Token("Literal", numberBuffer.joinToString("")))
            numberBuffer = ArrayList()
        }
    }

    private fun emptyLetterBufferAsVariables() {
        for (i in letterBuffer.indices) {
            tokens.add(Token("Variable", letterBuffer[i].toString()))
            if(i < letterBuffer.size - 1) {
                tokens.add(Token("Operator", "*"))
            }
        }
        letterBuffer = ArrayList()
    }
    init {
        letterBuffer = ArrayList()
        numberBuffer = ArrayList()
        tokens = ArrayList()


        val reader = inStr.replace(" ", "").asIterable().iterator()
        while (reader.hasNext()){
            val char = reader.next()
            when {
                Character.isDigit(char) -> numberBuffer.add(char)
                char == '.' -> numberBuffer.add(char)
                Character.isLetter(char) -> {

                    if(numberBuffer.isNotEmpty()) {

                        emptyNumberBufferAsLiteral()
                        tokens.add(Token("Operator", "*"))

                    }

                    letterBuffer.add(char)

                }
                isOperator(char) -> {

                    emptyNumberBufferAsLiteral()
                    emptyLetterBufferAsVariables()
                    tokens.add(Token("Operator", char.toString()))

                }
                isLeftParenthesis(char) -> {

                    if(letterBuffer.isNotEmpty()) {

                        tokens.add(Token("Function", letterBuffer.joinToString("")))
                        letterBuffer = ArrayList()

                    } else if(numberBuffer.isNotEmpty()) {

                        emptyNumberBufferAsLiteral()
                        tokens.add(Token("Operator", "*"))

                    }
                    tokens.add(Token("Left Parenthesis", char.toString()))

                }
                isRightParenthesis(char) -> {

                    emptyLetterBufferAsVariables()
                    emptyNumberBufferAsLiteral()
                    tokens.add(Token("Right Parenthesis", char.toString()))

                }
                isComma(char) -> {

                    emptyNumberBufferAsLiteral()
                    emptyLetterBufferAsVariables()
                    tokens.add(Token("Function Argument Separator", char.toString()))

                }
            }
        }
        if (numberBuffer.isNotEmpty()) {
            emptyNumberBufferAsLiteral()
        }
        if (letterBuffer.isEmpty()) {
            emptyLetterBufferAsVariables()
        }
    }

    override fun toString(): String {
        return tokens.toString()
    }
}