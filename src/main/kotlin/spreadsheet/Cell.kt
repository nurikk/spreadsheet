package spreadsheet

import mathparser.AstTree
import mathparser.Tokenizer

class Cell(private var value: String, private var cellName: String) {
    private var calculatedValue:Double = Double.NaN

    private fun isExpression():Boolean {
        return value.first() == '='
    }

    init {
        if (!isExpression()) {
            calculatedValue = value.toDouble()
        }
    }

    @Throws(Exception::class)
    private fun evalExpression(spreadsheet: Spreadsheet):Double {
        var exString = toString()
        var tokenizer = Tokenizer(exString)
        var deps = tokenizer.getVariableNames()

        //Very dumb
        while (deps.isNotEmpty()) {
            deps.forEach {
                val depCell = spreadsheet.getCell(it)
                exString = exString.replace(it, depCell.toString())
            }

            tokenizer = Tokenizer(exString)
            deps = tokenizer.getVariableNames()
            if (deps.contains(cellName)){
                throw Exception("Circular dependence detected")
            }
        }

        val ast = AstTree(tokenizer.tokens)
        deps.forEach { miss ->
            ast.setVariable(miss, spreadsheet.getValue(miss).toString())
        }

        calculatedValue = ast.getValue()
        return calculatedValue

    }

    fun getValue(spreadsheet: Spreadsheet):Double {
        return if(!calculatedValue.isNaN()) {
            calculatedValue
        } else {
            evalExpression(spreadsheet)
        }

    }

    override fun toString(): String {
        if (!calculatedValue.isNaN()) {
            return calculatedValue.toString()
        }
        return if (isExpression()) value.substring(1) else value
    }
}