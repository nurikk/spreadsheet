package pw.cucumber.dbs


import java.lang.Character.isDigit
import java.lang.Character.isLetter
import java.util.*
import kotlin.collections.ArrayList

class Token (val type: String = "", val value: String ="") {
    private val assoc: HashMap<String, String> = HashMap()
    init {
        assoc["^"] = "right"
        assoc["*"] = "left"
        assoc["/"] = "left"
        assoc["+"] = "left"
        assoc["-"] = "left"
    }

    private val prec: HashMap<String, Int> = HashMap()
    init {
        prec["^"] = 4
        prec["*"] = 3
        prec["/"] = 3
        prec["+"] = 2
        prec["-"] = 2
    }

    fun precedence() :Int {
        return prec.getOrDefault(value, 0)
    }

    fun associativity(): String{
        return assoc.getOrDefault(value, "")
    }

    override fun toString(): String {
        return "$type ($value)"
    }


}
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
                isDigit(char) -> numberBuffer.add(char)
                char == '.' -> numberBuffer.add(char)
                isLetter(char) -> {

                    if(numberBuffer.isNotEmpty()) {

                        emptyNumberBufferAsLiteral()
                        result.add( Token("Operator", "*"))

                    }

                    letterBuffer.add(char)

                }
                isOperator(char) -> {

                    emptyNumberBufferAsLiteral()
                    emptyLetterBufferAsVariables()
                    result.add( Token("Operator", char.toString()))

                }
                isLeftParenthesis(char) -> {

                    if(letterBuffer.isNotEmpty()) {

                        result.add( Token("Function", letterBuffer.joinToString("")))
                        letterBuffer = ArrayList()

                    } else if(numberBuffer.isNotEmpty()) {

                        emptyNumberBufferAsLiteral()
                        result.add( Token("Operator", "*"))

                    }
                    result.add( Token("Left Parenthesis", char.toString()))

                }
                isRightParenthesis(char) -> {

                    emptyLetterBufferAsVariables()
                    emptyNumberBufferAsLiteral()
                    result.add( Token("Right Parenthesis", char.toString()))

                }
                isComma(char) -> {

                    emptyNumberBufferAsLiteral()
                    emptyLetterBufferAsVariables()
                    result.add( Token("Function Argument Separator", char.toString()))

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




class AstNode(private val token:Token, private val leftChildNode: AstNode? = null, private val rightChildNode: AstNode? = null) {

    override fun toString(): String {
        return token.toString()
    }

    fun toStr(count:Int = 0): String {

        if (leftChildNode == null && rightChildNode == null) {
            return "${this.token.value}\n${"\t".repeat(count+1)}"
        }
        return this.token.value + "\t=>" + leftChildNode?.toStr(count + 1) + "\n" + "\t".repeat(count + 1) + "=>" + rightChildNode?.toStr(count + 1)
    }

    fun getValue(variables: Map<String, String>):Double {
        return when (token.type) {
            "Variable" -> variables.getOrDefault(token.value, "0.0").toDouble()
            "Literal" -> token.value.toDouble()
            "Operator" -> {
                return when (token.value) {
                    "+" -> leftChildNode!!.getValue(variables) + rightChildNode!!.getValue(variables)
                    "-" -> leftChildNode!!.getValue(variables) - rightChildNode!!.getValue(variables)
                    "*" -> leftChildNode!!.getValue(variables) * rightChildNode!!.getValue(variables)
                    "/" -> leftChildNode!!.getValue(variables) / rightChildNode!!.getValue(variables)
                    else -> Double.NaN
                }
            }
            else -> Double.NaN
        }

    }
}

class AstTree(tokens: ArrayList<Token>) {
    private val variables = mutableMapOf<String, String>()
    private val opStack = Stack<Token>()
    private val outStack = Stack<AstNode>()
    private var root: AstNode = AstNode(Token())

    init {
        tokens.forEach { v ->
            if(v.type === "Literal" || v.type === "Variable" ) {
                outStack.push(AstNode(v, null, null))
            }

            else if(v.type === "Function") {
                opStack.push(v)
            }
            else if(v.type === "Function Argument Separator") {


                while(opStack.size > 2 && opStack.peek().type !== "Left Parenthesis") {

                    val rightChildNode = outStack.pop()
                    val leftChildNode = outStack.pop()
                    outStack.push(AstNode(opStack.pop(), leftChildNode, rightChildNode))
                }
                /*if(opStack.length == 0){
                    console.log("Mismatched parentheses")
                    return
                }*/
            }

            else if(v.type == "Operator") {

                while (opStack.isNotEmpty() && opStack.peek() != null && (opStack.peek().type === "Operator")
                        && ((v.associativity() === "left" && v.precedence() <= opStack.peek().precedence())

                                || (v.associativity() === "right" && v.precedence() < opStack.peek().precedence()))) {

                    val rightChildNode = outStack.pop()
                    val leftChildNode = outStack.pop()
                    outStack.push(AstNode(opStack.pop(), leftChildNode, rightChildNode))
                }

                opStack.push(v)
            }


            else if(v.type === "Left Parenthesis") {
                opStack.push(v)
            }

            else if(v.type === "Right Parenthesis") {

                while(opStack.peek() != null && opStack.peek().type !== "Left Parenthesis") {
                    val rightChildNode = outStack.pop()
                    val leftChildNode = outStack.pop()
                    outStack.push(AstNode(opStack.pop(), leftChildNode, rightChildNode))
                }
                /*if(opStack.length == 0){
                    console.log("Unmatched parentheses")
                    return
                }*/

                opStack.pop()


                if(opStack.peek() != null  && opStack.peek().type === "Function") {
                    val rightChildNode = outStack.pop()
                    val leftChildNode = outStack.pop()
                    outStack.push(AstNode(opStack.pop(), leftChildNode, rightChildNode))
                }
            }
        }
        while(opStack.isNotEmpty()) {

            val rightChildNode = outStack.pop()
            val leftChildNode = outStack.pop()
            outStack.push(AstNode(opStack.pop(), leftChildNode, rightChildNode))
        }

        root = outStack.pop()

    }

    override fun toString(): String {
        return root.toStr()
    }

    fun getValue():Double {
        return root.getValue(variables)
    }

    fun setVariable(name:String, value: String) {
        variables[name] = value
    }

}

fun main(args: Array<String>) {
    val tokenizer = Tokenizer()
    val tokens = tokenizer.tokenize("A5 + 5A1")
    tokens.forEach {
        System.out.println(it)
    }

    val tree = AstTree(tokens)


    System.out.printf("%s\n", tree.toString())

    System.out.printf("value: %s", tree.getValue())











}