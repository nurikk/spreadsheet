package pw.cucumber.dbs


import java.io.File
import java.io.InputStream





fun main(args: Array<String>) {




    val inputStream: InputStream = File("./in.txt").inputStream()
    val lineList = mutableListOf<String>()
    inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
    val alphabet = ('A'..'Z').toList()
    val spreadsheet = Spreadsheet()

    lineList.forEachIndexed{ index, element ->
        spreadsheet.addRow(alphabet[index].toString(), element.split(","))
    }

    System.out.printf("getRows: %s", spreadsheet.getRows().toString())

//    spreadsheet.getRowNames().forEach({rowName ->
//        System.out.printf("rowName: %s\n", spreadsheet.getRow(rowName).toString())
//    })


}

