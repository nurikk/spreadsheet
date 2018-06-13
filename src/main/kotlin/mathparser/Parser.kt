package mathparser



fun main(args: Array<String>) {
    val tokenizer = Tokenizer()
    val tokens = tokenizer.tokenize("A5 + 5A1")
    tokens.forEach {
        System.out.println(it)
    }

    val tree = AstTree(tokens)


    System.out.printf("%s\n", tree.toString())

    System.out.printf("value: %s", tree.getValue())

}