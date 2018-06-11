package pw.cucumber.dbs

import org.mariuszgromada.math.mxparser.Expression
import kotlin.math.withSign

class Cell(var value: String) {


    private fun isExpression():Boolean {
        return this.value.first() === '='
    }

    private fun evalExpression(spreadsheet:Spreadsheet):Double {
        val exp = this.value.substring(1)

        val e = Expression(exp)
        val missing = e.missingUserDefinedArguments
        missing.forEach { miss ->
            e.defineConstant(miss, spreadsheet.getValue(miss))
        }
        return e.calculate()
    }

    fun getValue(spreadsheet: Spreadsheet):Double {
        if (this.isExpression()) {
            return evalExpression(spreadsheet)
        } else {

            return value.toDouble()
        }
    }
}