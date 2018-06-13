package mathparser

import java.util.*

class Tokenizer {
    private val operators = Arrays.asList('+', '-', '*', '/', '^')

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
    private var result: ArrayList<Token> = ArrayList()

    private fun emptyNumberBufferAsLiteral() {

        if (letterBuffer.isNotEmpty()) {
            val varName = ArrayList<Char>()
            varName.addAll(letterBuffer)
            varName.addAll(numberBuffer)
            result.add(Token("Variable", varName.joinToString("")))

            numberBuffer = ArrayList()
            letterBuffer = ArrayList()
        }

        if(numberBuffer.isNotEmpty()) {
            result.add(Token("Literal", numberBuffer.joinToString("")))
            numberBuffer = ArrayList()
        }
    }

    private fun emptyLetterBufferAsVariables() {
        for (i in letterBuffer.indices) {
            result.add(Token("Variable", letterBuffer[i].toString()))
            if(i < letterBuffer.size - 1) {
                result.add(Token("Operator", "*"))
            }
        }
        letterBuffer = ArrayList()
    }
    fun tokenize(inStr:String): ArrayList<Token> {
        letterBuffer = ArrayList()
        numberBuffer = ArrayList()
        result = ArrayList()


        val reader = inStr.replace(" ", "").asIterable().iterator()
        while (reader.hasNext()){
            val char = reader.next()
            when {
                Character.isDigit(char) -> numberBuffer.add(char)
                char == '.' -> numberBuffer.add(char)
                Character.isLetter(char) -> {

                    if(numberBuffer.isNotEmpty()) {

                        emptyNumberBufferAsLiteral()
                        result.add(Token("Operator", "*"))

                    }

                    letterBuffer.add(char)

                }
                isOperator(char) -> {

                    emptyNumberBufferAsLiteral()
                    emptyLetterBufferAsVariables()
                    result.add(Token("Operator", char.toString()))

                }
                isLeftParenthesis(char) -> {

                    if(letterBuffer.isNotEmpty()) {

                        result.add(Token("Function", letterBuffer.joinToString("")))
                        letterBuffer = ArrayList()

                    } else if(numberBuffer.isNotEmpty()) {

                        emptyNumberBufferAsLiteral()
                        result.add(Token("Operator", "*"))

                    }
                    result.add(Token("Left Parenthesis", char.toString()))

                }
                isRightParenthesis(char) -> {

                    emptyLetterBufferAsVariables()
                    emptyNumberBufferAsLiteral()
                    result.add(Token("Right Parenthesis", char.toString()))

                }
                isComma(char) -> {

                    emptyNumberBufferAsLiteral()
                    emptyLetterBufferAsVariables()
                    result.add(Token("Function Argument Separator", char.toString()))

                }
            }
        }
        if (numberBuffer.isNotEmpty()) {
            emptyNumberBufferAsLiteral()
        }
        if (letterBuffer.isEmpty()) {
            emptyLetterBufferAsVariables()
        }
        return result
    }
}