package pw.cucumber.dbs

class Spreadsheet {

    private val cols = mutableMapOf<String, ArrayList<Cell>>()

    fun addRow(name:String, cells:List<String>) {

        cols[name] = ArrayList(cells.map { Cell(it) })

    }
    fun getValue(cell:String):Double {

        val cellName = cell[0].toString()
        val cellIndex = cell.substring(1).toInt()
        val row = cols.getOrDefault(cellName, ArrayList())
        val reqCell = row.getOrElse(cellIndex){Cell("0")}
        return reqCell.getValue(this)
    }
    fun getRowNames():List<String>{
        return cols.keys.map { it }.sorted()
    }

    fun getRows():List<List<String>> {
        return cols.keys.map { getRow(it) }

    }
    fun getRow(name:String):List<String> {
        return cols.getOrDefault(name, ArrayList()).map { "%.5f".format(it.getValue(this)) }
    }
}