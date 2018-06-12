package pw.cucumber.dbs

import org.mariuszgromada.math.mxparser.Expression


class Cell(private var value: String, private var cellName: String) {

    private fun isExpression():Boolean {
        return value.first() == '='
    }

    @Throws(Exception::class)
    private fun evalExpression(spreadsheet:Spreadsheet):Double {
        var exString = toString()
        var exp = Expression(exString)
        var deps = exp.missingUserDefinedArguments.filter { spreadsheet.getCell(it).isExpression() }

        while (deps.isNotEmpty()){
            deps.forEach {
                exString = exString.replace(it, spreadsheet.getCell(it).toString())
            }
            exp = Expression(exString)
            deps = exp.missingUserDefinedArguments.filter { spreadsheet.getCell(it).isExpression() }
            if (deps.contains(cellName)){
                throw Exception("Circular dependence detected")
            }
        }

        exp.missingUserDefinedArguments.forEach { miss ->
            exp.defineConstant(miss, spreadsheet.getValue(miss))
        }

        return exp.calculate()
    }

    fun getValue(spreadsheet: Spreadsheet):Double {
        return if (this.isExpression()) {
            evalExpression(spreadsheet)
        } else {
            value.toDouble()
        }
    }

    override fun toString(): String {
        return value.substring(1)
    }
}