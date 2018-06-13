package spreadsheet

import mathparser.AstTree
import mathparser.Tokenizer
import java.util.*
import kotlin.properties.Delegates


class Cell(private var value: String, private var cellName: String) {
    var ast: AstTree by Delegates.notNull()
    var tokenizer: Tokenizer by Delegates.notNull()

    init {
        if (isExpression()) {
            this.tokenizer = Tokenizer(value.substring(1))
            this.ast = AstTree(tokenizer.tokens)
        }
    }

    private fun isExpression():Boolean {
        return value.first() == '='
    }


    fun getDependecies(): List<String> {
        if (isExpression()){
            return tokenizer.getVariableNames()
        } else {
            return emptyList()
        }

    }

    @Throws(Exception::class)
    private fun evalExpression(spreadsheet: Spreadsheet):Double {

        val deps = getDependecies().toMutableList()
        val depsToCheck = Stack<String>()
        val checkedDeps = mutableListOf<String>()

        depsToCheck.addAll(deps)
        while (depsToCheck.isNotEmpty()){
            if (depsToCheck.contains(cellName)) {
                throw Exception("circular dependency")
            }
            val dep = depsToCheck.pop()
            val subDeps = spreadsheet.getCell(dep).getDependecies().filter { !checkedDeps.contains(it) }
            depsToCheck.addAll(subDeps)
            checkedDeps.add(dep)

        }

        deps.forEach { miss ->
            ast.setVariable(miss, spreadsheet.getValue(miss).toString())
        }

        return ast.getValue()

    }

    fun getValue(spreadsheet: Spreadsheet):Double {
        if (isExpression()){
            return evalExpression(spreadsheet)
        } else {
            return value.toDouble()
        }

    }

    override fun toString(): String {
        return if (isExpression()) value.substring(1) else value
    }
}