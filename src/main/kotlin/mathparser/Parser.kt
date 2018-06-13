package mathparser



fun main(args: Array<String>) {
    val tokenizer = Tokenizer("A5 + 5A1")

    tokenizer.tokens.forEach {
        System.out.println(it)
    }

    val tree = AstTree(tokenizer.tokens)


    System.out.printf("%s\n", tree.toString())

    System.out.printf("value: %s", tree.getValue())

}