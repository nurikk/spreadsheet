package spreadsheet


import java.io.File
import java.io.InputStream

import org.apache.commons.cli.Options

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.MissingOptionException

import kotlinx.coroutines.experimental.*


open class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val options = Options()
            options.addRequiredOption("i", "input", true, "input file")
            options.addRequiredOption("o", "output", true, "output file")

            val parser = DefaultParser()
            try {
                val cmd = parser.parse(options, args)
                val inFile = cmd.getOptionValue('i')
                val outFile = cmd.getOptionValue('o')



                val inputStream: InputStream = File(inFile).inputStream()
                val lineList = mutableListOf<String>()
                inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
                val alphabet = ('A'..'Z').toList()
                val spreadsheet = Spreadsheet()

                lineList.forEachIndexed{ index, element ->
                    spreadsheet.addRow(alphabet[index].toString(), element.split(","))
                }

                File(outFile).printWriter().use { out ->

                    val deferred = spreadsheet.getRowNames().map { n ->
                        async {
                            spreadsheet.getRow(n)
                        }
                    }
                    runBlocking {
                        val rows = deferred.map { it.await() } //
                        rows.forEach { out.println(it.joinToString(","))}
                    }
                }


            } catch (e: Throwable) {
                    System.out.println(e.message)
            }




        }
    }
}




