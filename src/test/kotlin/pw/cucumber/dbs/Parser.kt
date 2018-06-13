package pw.cucumber.dbs

import org.junit.Test
import kotlin.test.*


class ParserTest {

    @Test
    fun tokenizer() {
        val tokenizer = Tokenizer()
        val tokens = tokenizer.tokenize("3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3")
        assertEquals("[Literal (3), Operator (+), Literal (4), Operator (*), Literal (2), Operator (/), Left Parenthesis ((), Literal (1), Operator (-), Literal (5), Right Parenthesis ()), Operator (^), Literal (2), Operator (^), Literal (3)]", tokens.toString())

        val tokens2 = tokenizer.tokenize("89sin(45) + 2.2x/7")
        assertEquals("[Literal (89), Operator (*), Function (sin), Left Parenthesis ((), Literal (45), Right Parenthesis ()), Operator (+), Literal (2.2), Operator (*), Variable (x), Operator (/), Literal (7)]", tokens2.toString())
    }



    @Test
    fun astBuilder() {
        val tokens = ArrayList<Token>()
        tokens.add(Token("Literal", "5"))
        tokens.add(Token("Operator", "*"))
        tokens.add(Token("Literal", "10"))
        val ast = AstTree(tokens)
        assertEquals("*\t=>5\n" +
                "\t\t\n" +
                "\t=>10\n" +
                "\t\t", ast.toString())


    }



}