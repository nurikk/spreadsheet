package pw.cucumber.dbs

import java.util.*

class Spreadsheet {

    private val cols = mutableMapOf<String, ArrayList<Cell>>()

    fun addRow(name:String, cells:List<String>) {

        cols[name] = ArrayList(cells.mapIndexed { index, s -> Cell(s, "$name$index") })

    }
    fun getCell(cell:String): Cell {
        val cellName = cell[0].toString()
        val cellIndex = cell.substring(1).toInt()
        val row = cols.getOrDefault(cellName, ArrayList())
        return row.getOrElse(cellIndex){Cell("0", "XX")}
    }
    fun getValue(cell:String):Double {

        return getCell(cell).getValue(this)
    }
    fun getRowNames():List<String>{
        return cols.keys.map { it }.sorted()
    }

    fun getRows():List<List<String>> {
        return getRowNames().map { getRow(it) }

    }
    fun getRow(name:String):List<String> {
        return cols.getOrDefault(name, ArrayList()).map { "%.5f".format(it.getValue(this)) }
    }
}