package pw.cucumber.dbs

import org.junit.Test
import kotlin.test.assertEquals

class HelloTest {

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
        s1.addRow("A", "1,2,=A0,=A1".split(","))
        assertEquals("[[1.00000, 2.00000, 1.00000, 2.00000]]", s1.getRows().toString())

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
    fun dependecy() {
        val s1 = Spreadsheet()
        s1.addRow("A", "5,3,=A0,=A2".split(","))
        assertEquals("[[5.00000, 3.00000, 5.00000, 5.00000]]", s1.getRows().toString())


    }


}
