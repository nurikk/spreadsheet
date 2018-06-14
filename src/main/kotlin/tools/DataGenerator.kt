package tools

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

val random = Random()
fun rand(from: Int, to: Int) : Int {
    return random.nextInt(to - from) + from
}

fun generateFormula(rows: List<Char>, cols: Int = 1000):String {
    val numCells = rand(1, 5)
    val cells = ArrayList<String>()
    val oprator = Arrays.asList("+", "*", "/", "-").shuffled()[0]


    while (cells.size < numCells) {
        val colName = rows.shuffled()[0]
        val colNum =  +rand(1, cols)
        cells.add("$colName$colNum")
    }
    return "=" + cells.joinToString(oprator)
}

fun writeToFile(outFile:String, data:ArrayList<ArrayList<String>>){
    File(outFile).printWriter().use { out ->
        data.forEach {
            out.println(it.joinToString(","))
        }
    }
}

fun generateData(rows: List<Char>, cols: Int = 1000, formulas: Int = 50):ArrayList<ArrayList<String>> {
    val result = ArrayList<ArrayList<String>>()

    for (row in rows) {
        val rowValue = ArrayList<String>()
        for (col in 0..cols) {
            val rnd = rand(0, 100)
            if (rnd >= formulas) {
                rowValue.add(rand(0, 10000).toString())
            } else {
                rowValue.add(generateFormula(rows, cols))
            }

        }
        result.add(rowValue)
    }
    return result
}

fun main(args: Array<String>) {

    writeToFile("./mocks/10cols.csv", generateData(('A'..'Z').toList(), 10, 10))
    writeToFile("./mocks/100cols.csv", generateData(('A'..'Z').toList(), 100, 10))
    writeToFile("./mocks/1000cols.csv", generateData(('A'..'Z').toList(), 1000, 10))
    writeToFile("./mocks/10000cols.csv", generateData(('A'..'Z').toList(), 10000, 10))
    writeToFile("./mocks/100000cols.csv", generateData(('A'..'Z').toList(), 100000, 10))


}
