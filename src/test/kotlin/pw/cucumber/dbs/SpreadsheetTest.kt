package pw.cucumber.dbs

import org.junit.Test
import java.io.File
import java.io.InputStream
import kotlin.test.*


class SpreadsheetTest {

    @Test
    fun simpleValues() {
        val s1 = Spreadsheet()
        s1.addRow("A", "1,2,3,4".split(","))
        assertEquals("[[1.00000, 2.00000, 3.00000, 4.00000]]", s1.getRows().toString())

        val s2 = Spreadsheet()
        s2.addRow("A", "1,2,3,4".split(","))
        s2.addRow("B", "5,4,3,2,1".split(","))
        assertEquals("[[1.00000, 2.00000, 3.00000, 4.00000], [5.00000, 4.00000, 3.00000, 2.00000, 1.00000]]", s2.getRows().toString())
    }

    @Test
    fun references() {
        val s1 = Spreadsheet()
        s1.addRow("A", "1,2,=A0+A1,=A2-4,=A0".split(","))
        assertEquals("[[1.00000, 2.00000, 1.00000, 1.00000]]", s1.getRows().toString())

        val s2 = Spreadsheet()
        s2.addRow("A", "=A1,3,2,1".split(","))
        assertEquals("[[3.00000, 3.00000, 2.00000, 1.00000]]", s2.getRows().toString())
    }

    @Test
    fun expressions() {
        val s1 = Spreadsheet()
        s1.addRow("A", "5,3,=A0/A1,=A0*A1".split(","))
        assertEquals("[[5.00000, 3.00000, 1.66667, 15.00000]]", s1.getRows().toString())
    }

    @Test
    fun dep1() {
        val s1 = Spreadsheet()
        s1.addRow("A", "1,2,3,4,=A0+A1+A2".split(","))
        assertEquals("[[1.00000, 2.00000, 3.00000, 4.00000, 6.00000]]", s1.getRows().toString())


        val s2 = Spreadsheet()
        s2.addRow("A", "1,2,3,4,5".split(","))
        s2.addRow("B", "=A0+A1,=B0+B4,3,4,5".split(","))
        assertEquals("[[1.00000, 2.00000, 3.00000, 4.00000, 5.00000], [3.00000, 8.00000, 3.00000, 4.00000, 5.00000]]", s2.getRows().toString())
    }

    @Test
    fun dependency() {
        val s1 = Spreadsheet()
        s1.addRow("A", "5,3,=A0,=A2".split(","))
        assertEquals("[[5.00000, 3.00000, 5.00000, 5.00000]]", s1.getRows().toString())

        val s2 = Spreadsheet()
        s2.addRow("A", "4,3,=A0/A1,1".split(","))
        s2.addRow("B", "=A2*B1,3,2,1".split(","))
        assertEquals("[[4.00000, 3.00000, 1.33333, 1.00000], [4.00000, 3.00000, 2.00000, 1.00000]]", s2.getRows().toString())


        val s3 = Spreadsheet()
        s3.addRow("A", "1,=A0,=A1,=A2".split(","))

        assertEquals("[[1.00000, 1.00000, 1.00000, 1.00000]]", s3.getRows().toString())
    }

    @Test
    fun circularDependency() {
        assertFailsWith(Exception::class) {
            val s1 = Spreadsheet()
            s1.addRow("A", "=A2,3,=A0,1".split(","))
            assertEquals("[[5.00000, 3.00000, 5.00000, 5.00000]]", s1.getRows().toString())
        }

        assertFailsWith(Exception::class) {
            val s2 = Spreadsheet()
            s2.addRow("A", "=A1,=A2,=A0,1".split(","))
            assertEquals("[[5.00000, 3.00000, 5.00000, 5.00000]]", s2.getRows().toString())
        }
    }

    @Test
    fun errorFormulas() {
        val s1 = Spreadsheet()
        s1.addRow("A", "=A2/0,3,1,1".split(","))
        assertEquals("[[NaN, 3.00000, 1.00000, 1.00000]]", s1.getRows().toString())


        val s2 = Spreadsheet()
        s2.addRow("A", "1,=A2)*0,3,1,1".split(","))
        assertEquals("[[1.00000, NaN, 3.00000, 1.00000, 1.00000]]", s2.getRows().toString())

    }



    @Test
    fun bigSpreadsheet() {



        val inputStream: InputStream = File("./mocks/100cols.csv").inputStream()
        val lineList = mutableListOf<String>()
        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
        val alphabet = ('A'..'Z').toList()
        val spreadsheet = Spreadsheet()

        lineList.forEachIndexed{ index, element ->
            spreadsheet.addRow(alphabet[index].toString(), element.split(","))
        }

        assertEquals(spreadsheet.getRows().size, 1000)




    }


}
