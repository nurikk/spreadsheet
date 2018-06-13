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

        val tokens3 = tokenizer.tokenize("A5 + B1")
        assertEquals("[Variable (A5), Operator (+), Variable (B1)]", tokens3.toString())

        val tokens4 = tokenizer.tokenize("4A5 + B1")
        assertEquals("[Literal (4), Operator (*), Variable (A5), Operator (+), Variable (B1)]", tokens4.toString())

        val tokens5 = tokenizer.tokenize("A3*(A0+1)")
        assertEquals("[Variable (A3), Operator (*), Left Parenthesis ((), Variable (A0), Operator (+), Literal (1), Right Parenthesis ())]", tokens5.toString())
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


    @Test
    fun astExcutor() {
        val tokens = ArrayList<Token>()
        tokens.add(Token("Literal", "5"))
        tokens.add(Token("Operator", "*"))
        tokens.add(Token("Literal", "10"))
        val ast = AstTree(tokens)
        assertEquals(50.0, ast.getValue())




        val tokens2 = ArrayList<Token>()
        tokens2.add(Token("Literal", "3"))
        tokens2.add(Token("Operator", "+"))
        tokens2.add(Token("Literal", "4"))
        tokens2.add(Token("Operator", "*"))
        tokens2.add(Token("Literal", "2"))
        tokens2.add(Token("Operator", "/"))
        tokens2.add(Token("Left Parenthesis", "("))
        tokens2.add(Token("Literal", "1"))
        tokens2.add(Token("Operator", "-"))
        tokens2.add(Token("Literal", "5"))
        tokens2.add(Token("Right Parenthesis", ")"))
        tokens2.add(Token("Operator", "*"))
        tokens2.add(Token("Literal", "3"))


        val ast2 = AstTree(tokens2)
        assertEquals(-3.0, ast2.getValue())
    }


    @Test
    fun variablesInAstExecutor() {
        val tokens = ArrayList<Token>()
        tokens.add(Token("Variable", "A4"))
        tokens.add(Token("Operator", "*"))
        tokens.add(Token("Literal", "10"))
        val ast = AstTree(tokens)
        ast.setVariable("A4", "5")
        assertEquals(50.0, ast.getValue())




        val tokens2 = ArrayList<Token>()
        tokens2.add(Token("Variable", "A4"))
        tokens2.add(Token("Operator", "+"))
        tokens2.add(Token("Variable", "B1"))
        val ast2 = AstTree(tokens2)
        ast2.setVariable("A4", "5")
        assertEquals(5.0, ast2.getValue())

        ast2.setVariable("B1", "2")
        assertEquals(7.0, ast2.getValue())
    }


}