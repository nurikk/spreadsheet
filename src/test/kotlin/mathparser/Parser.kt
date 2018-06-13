package mathparser

import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

import kotlin.test.*


class ParserTest {

    @Test
    fun tokenizer() {
        val tokenizer = Tokenizer("3 + 4 * 2 / ( 1 - 5 ) * 2 / 3")

        assertEquals("[Literal (3), Operator (+), Literal (4), Operator (*), Literal (2), Operator (/), Left Parenthesis ((), Literal (1), Operator (-), Literal (5), Right Parenthesis ()), Operator (*), Literal (2), Operator (/), Literal (3)]", tokenizer.toString())

        val tokenizer2 = Tokenizer("89sin(45) + 2.2x/7")
        assertEquals("[Literal (89), Operator (*), Function (sin), Left Parenthesis ((), Literal (45), Right Parenthesis ()), Operator (+), Literal (2.2), Operator (*), Variable (x), Operator (/), Literal (7)]", tokenizer2.toString())

        val tokenizer3 = Tokenizer("A5 + B1")
        assertEquals("[Variable (A5), Operator (+), Variable (B1)]", tokenizer3.toString())

        val tokenizer4 = Tokenizer("4A5 + B1")
        assertEquals("[Literal (4), Operator (*), Variable (A5), Operator (+), Variable (B1)]", tokenizer4.toString())

        val tokenizer5 = Tokenizer("A3*(A0+1)")
        assertEquals("[Variable (A3), Operator (*), Left Parenthesis ((), Variable (A0), Operator (+), Literal (1), Right Parenthesis ())]", tokenizer5.toString())
        assertEquals(Arrays.asList("A3", "A0"), tokenizer5.getVariableNames())



        val tokenizer6 = Tokenizer("3 + 4 (*(((* 2 / ( 1 - 5 ) * 2 / 3")
        assertEquals("[]", tokenizer6.toString())
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



        val tokens3 = ArrayList<Token>()
        tokens3.add(Token("Literal", "3"))
        tokens3.add(Token("Operator", "/"))
        tokens3.add(Token("Literal", "0"))
        val ast3 = AstTree(tokens3)
        assertEquals(Double.POSITIVE_INFINITY, ast3.getValue())

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


    @Test
    fun wrongAstExecution(){
        val tokens3 = ArrayList<Token>()
        val ast3 = AstTree(tokens3)
        assertEquals(Double.NaN, ast3.getValue())
    }



}