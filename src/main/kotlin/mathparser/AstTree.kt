package mathparser

import java.util.*

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

                opStack.pop()


            }
        }
        while(opStack.isNotEmpty()) {

            val rightChildNode = outStack.pop()
            val leftChildNode = outStack.pop()
            outStack.push(AstNode(opStack.pop(), leftChildNode, rightChildNode))
        }
        if (outStack.isNotEmpty()){
            root = outStack.pop()
        }
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