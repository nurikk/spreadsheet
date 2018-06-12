package pw.cucumber.dbs

import org.mariuszgromada.math.mxparser.Expression


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
    private fun evalExpression(spreadsheet:Spreadsheet):Double {
        var exString = toString()

        var exp = Expression(exString)

        var deps = exp.missingUserDefinedArguments

        while (deps.isNotEmpty()) {
            deps.forEach {
//                System.out.printf("dep: %s exString:\n", it, exString)
                val depCell = spreadsheet.getCell(it)
                exString = exString.replace(it, depCell.toString())
            }
            exp = Expression(exString)
            deps = exp.missingUserDefinedArguments
            if (deps.contains(cellName)){
                throw Exception("Circular dependence detected")
            }
        }

        exp.missingUserDefinedArguments.forEach { miss ->
            exp.defineConstant(miss, spreadsheet.getValue(miss))
        }
        calculatedValue = exp.calculate()
        return calculatedValue

    }

    fun getValue(spreadsheet: Spreadsheet):Double {
        if(!calculatedValue.isNaN()) {
//            System.out.printf("use precomputed value %s %s \n", value, calculatedValue)
            return calculatedValue
        } else {
            return evalExpression(spreadsheet)
        }

    }

    override fun toString(): String {
        if (!calculatedValue.isNaN()) {
            return calculatedValue.toString()
        }
        return if (isExpression()) return value.substring(1) else return value
    }
}