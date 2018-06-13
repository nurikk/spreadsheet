package mathparser

class AstNode(private val token: Token, private val leftChildNode: AstNode? = null, private val rightChildNode: AstNode? = null) {

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